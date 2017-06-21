package br.com.cesarsicas.beaconhotorcold.Core.Service;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelUuid;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import br.com.cesarsicas.beaconhotorcold.Core.Domains.Beacon;
import br.com.cesarsicas.beaconhotorcold.Core.Main.Constants;
import br.com.cesarsicas.beaconhotorcold.Core.Main.GlobalConfig;
import br.com.cesarsicas.beaconhotorcold.Core.Utils.BeaconUtils;
import br.com.cesarsicas.beaconhotorcold.Core.Utils.DialogUtils;
import br.com.cesarsicas.beaconhotorcold.Core.Utils.TlmValidator;
import br.com.cesarsicas.beaconhotorcold.Core.Utils.UidValidator;
import br.com.cesarsicas.beaconhotorcold.Core.Utils.UrlValidator;
import br.com.cesarsicas.beaconhotorcold.Core.Interfaces.MVP;

/**
 * Created by julio on 17/06/17.
 */

public class BeaconService {

    private static final String TAG = "EddystoneValidator";


    // An aggressive scan for nearby devices that reports immediately.
    private static final ScanSettings SCAN_SETTINGS =
            new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).setReportDelay(0)
                    .build();

    private static final Handler handler = new Handler(Looper.getMainLooper());

    // The Eddystone Service UUID, 0xFEAA.
    private static final ParcelUuid EDDYSTONE_SERVICE_UUID =
            ParcelUuid.fromString("0000FEAA-0000-1000-8000-00805F9B34FB");


    private BluetoothLeScanner scanner;
    private List<ScanFilter> scanFilters;
    private ScanCallback scanCallback;
    private  ArrayList<Beacon> arrayList = new ArrayList<>();

    /* device address */
    private Map<String, Beacon> deviceToBeaconMap = new HashMap<>();

    private int onLostTimeoutMillis;
    Context context;
    private MVP.Model model;


    public BeaconService(Context context, MVP.Model model){
        this.model = model;
        this.context = context;
    }


    public void startScan(){

        init();

        scanFilters = new ArrayList<>();
        scanFilters.add(new ScanFilter.Builder().setServiceUuid(EDDYSTONE_SERVICE_UUID).build());
        scanCallback = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                ScanRecord scanRecord = result.getScanRecord();
                if (scanRecord == null) {
                    return;
                }

                String deviceAddress = result.getDevice().getAddress();
                Beacon beacon;

                if (!deviceToBeaconMap.containsKey(deviceAddress)) {
                    beacon = new Beacon(deviceAddress, result.getRssi());
                    deviceToBeaconMap.put(deviceAddress, beacon);
                    arrayList.add(beacon);
                } else {
                    deviceToBeaconMap.get(deviceAddress).lastSeenTimestamp = System.currentTimeMillis();
                    deviceToBeaconMap.get(deviceAddress).rssi = result.getRssi();

                }

                byte[] serviceData = scanRecord.getServiceData(EDDYSTONE_SERVICE_UUID);
                validateServiceData(deviceAddress, serviceData);
            }

            @Override
            public void onScanFailed(int errorCode) {
                switch (errorCode) {
                    case SCAN_FAILED_ALREADY_STARTED:
                        logErrorAndShowToast("SCAN_FAILED_ALREADY_STARTED");
                        break;
                    case SCAN_FAILED_APPLICATION_REGISTRATION_FAILED:
                       logErrorAndShowToast("SCAN_FAILED_APPLICATION_REGISTRATION_FAILED");
                        break;
                    case SCAN_FAILED_FEATURE_UNSUPPORTED:
                        logErrorAndShowToast("SCAN_FAILED_FEATURE_UNSUPPORTED");
                        break;
                    case SCAN_FAILED_INTERNAL_ERROR:
                        logErrorAndShowToast("SCAN_FAILED_INTERNAL_ERROR");
                        break;
                    default:
                        logErrorAndShowToast("Scan failed, unknown error code");
                        break;
                }
            }
        };

        onLostTimeoutMillis = GlobalConfig.ON_LOST_TIMEOUT_SECS * 1000;


    }


    public void stopScan(){
        if (scanner != null) {
            scanner.stopScan(scanCallback);
        }
    }


    public void resumeScan(){
        handler.removeCallbacksAndMessages(null);

        int timeoutMillis = GlobalConfig.ON_LOST_TIMEOUT_SECS * 1000;

        if (timeoutMillis > 0) {  // 0 is special and means don't remove anything.
            onLostTimeoutMillis = timeoutMillis;
            setOnLostRunnable();
        }

        if (scanner != null) {
            scanner.startScan(scanFilters, SCAN_SETTINGS, scanCallback);
        }
    }

    private void setOnLostRunnable() {
        Runnable removeLostDevices = new Runnable() {
            @Override
            public void run() {
                long time = System.currentTimeMillis();
                Iterator<Map.Entry<String, Beacon>> itr = deviceToBeaconMap.entrySet().iterator();
                while (itr.hasNext()) {
                    Beacon beacon = itr.next().getValue();
                    if ((time - beacon.lastSeenTimestamp) > onLostTimeoutMillis) {
                        itr.remove();
                        arrayList.remove(beacon);
                    }
                }
                handler.postDelayed(this, onLostTimeoutMillis);
            }
        };
        handler.postDelayed(removeLostDevices, onLostTimeoutMillis);
    }


    // Attempts to create the scanner.
    private void init() {
        BluetoothManager manager = (BluetoothManager) context.getApplicationContext()
                .getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter btAdapter = manager.getAdapter();

        if (btAdapter == null) {
            finishAlertDialog("Bluetooth Error", "Bluetooth not detected on device");
        }
        else{
            scanner = btAdapter.getBluetoothLeScanner();
        }
    }


    // Checks the frame type and hands off the service data to the validation module.
    private void validateServiceData(String deviceAddress, byte[] serviceData) {
        Beacon beacon = deviceToBeaconMap.get(deviceAddress);

        if (serviceData == null) {
            String err = "Null Eddystone service data";
            beacon.frameStatus.nullServiceData = err;
            logDeviceError(deviceAddress, err);
            return;
        }
        Log.v(TAG, deviceAddress + " " + BeaconUtils.toHexString(serviceData));
        switch (serviceData[0]) {
            case Constants.UID_FRAME_TYPE:
                UidValidator.validate(deviceAddress, serviceData, beacon);
                break;
            case Constants.TLM_FRAME_TYPE:
                TlmValidator.validate(deviceAddress, serviceData, beacon);
                break;
            case Constants.URL_FRAME_TYPE:
                UrlValidator.validate(deviceAddress, serviceData, beacon);
                break;
            default:
                String err = String.format("Invalid frame type byte %02X", serviceData[0]);
                beacon.frameStatus.invalidFrameType = err;
                logDeviceError(deviceAddress, err);

        }

        model.scanResult(arrayList);
    }



    public void finishAlertDialog(String title, String message) {
        DialogUtils.showFinishingAlertDialog((Activity) context, title, message);

    }

    public void logErrorAndShowToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        Log.e(TAG, message);
    }

    public void logDeviceError(String deviceAddress, String err) {
        Log.e(TAG, deviceAddress + ": " + err);
    }


}
