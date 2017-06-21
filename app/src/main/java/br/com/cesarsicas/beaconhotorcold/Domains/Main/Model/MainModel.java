package br.com.cesarsicas.beaconhotorcold.Domains.Main.Model;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import br.com.cesarsicas.beaconhotorcold.Core.Domains.Beacon;
import br.com.cesarsicas.beaconhotorcold.Core.Interfaces.MVP;
import br.com.cesarsicas.beaconhotorcold.Core.Service.BeaconService;

/**
 * Created by julio on 18/06/17.
 */

public class MainModel implements MVP.Model {
    private MVP.Presenter presenter;
    BeaconService service = null;

    public MainModel(MVP.Presenter presenter){
        this.presenter = presenter;
    }

    @Override
    public void retrieveBeacons() {
        service = new BeaconService(presenter.getContext(), this);
        service.startScan();
    }

    @Override
    public void retrieveBeacons(Boolean isBeaconsFiltered, String beaconDeviceAddress) {

    }

    @Override
    public void onPause() {
        if(service != null) {
            service.stopScan();
        }
    }

    @Override
    public void onResume() {
        if(service != null){
            service.resumeScan();
        }

    }

    @Override
    public void scanResult(ArrayList<Beacon> beacons) {
        presenter.updateListView(beacons);
    }
}
