package br.com.cesarsicas.beaconhotorcold.Domains.HotOrCold.Model;

import java.util.ArrayList;

import br.com.cesarsicas.beaconhotorcold.Core.Domains.Beacon;
import br.com.cesarsicas.beaconhotorcold.Core.Interfaces.MVP;
import br.com.cesarsicas.beaconhotorcold.Core.Service.BeaconService;


public class HotOrColdModel implements MVP.Model {
    private MVP.Presenter presenter;
    BeaconService service;
    Boolean isBeaconsFiltered;
    String beaconDeviceAddress;

    public HotOrColdModel(MVP.Presenter presenter){
        this.presenter = presenter;
    }

    @Override
    public void retrieveBeacons() {
        service = new BeaconService(presenter.getContext(), this);
        service.startScan();
    }

    @Override
    public void retrieveBeacons(Boolean isBeaconsFiltered ,String beaconDeviceAddress) {
        this.isBeaconsFiltered = isBeaconsFiltered;
        this.beaconDeviceAddress = beaconDeviceAddress;
        service = new BeaconService(presenter.getContext(), this);
        service.startScan();
    }

    @Override
    public void onPause() {
        service.stopScan();
    }

    @Override
    public void onResume() {
        service.resumeScan();
    }

    @Override
    public void scanResult(ArrayList<Beacon> beacons) {
        if(isBeaconsFiltered){
            for (Beacon beacon: beacons) {
                if(new String(this.beaconDeviceAddress).equals(beacon.deviceAddress)){
                    presenter.addItemView(beacon);
                }
            }
        }
        else{
            presenter.updateListView(beacons);
        }
    }
}
