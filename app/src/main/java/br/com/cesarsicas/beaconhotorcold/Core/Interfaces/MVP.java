package br.com.cesarsicas.beaconhotorcold.Core.Interfaces;

import android.content.Context;

import java.util.ArrayList;

import br.com.cesarsicas.beaconhotorcold.Core.Domains.Beacon;

/**
 * Created by julio on 17/06/17.
 */

public interface MVP {

    interface Model{
        public void retrieveBeacons();
        public void retrieveBeacons(Boolean isBeaconsFiltered ,String beaconDeviceAddress);
        public void onPause();
        public void onResume();
        public void scanResult(ArrayList<Beacon>beacons);
    }

    interface View{
        String BEACONS_KEY = "beacons";

        public void updateListView();
        public void updateItemView( int posicao );
        public void removeItemView(Beacon beacon);
        public void addItemView(Beacon beacon);
    }

    interface Presenter{
        public void onPause();
        public void onResume();

        public void setView(MVP.View view);
        public Context getContext();
        public ArrayList<Beacon> getBeacons();
        public void retrieveBeacons();
        public void updateListView(ArrayList<Beacon>beacons);
        public void updateItemView( Beacon b );
        public void removeItemView(Beacon beacon);
        public void addItemView(Beacon beacon);

    }
}
