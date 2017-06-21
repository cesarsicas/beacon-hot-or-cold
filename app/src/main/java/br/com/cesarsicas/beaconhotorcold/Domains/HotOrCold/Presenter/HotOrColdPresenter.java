package br.com.cesarsicas.beaconhotorcold.Domains.HotOrCold.Presenter;

import android.content.Context;

import java.util.ArrayList;

import br.com.cesarsicas.beaconhotorcold.Core.Domains.Beacon;
import br.com.cesarsicas.beaconhotorcold.Core.Interfaces.MVP;
import br.com.cesarsicas.beaconhotorcold.Domains.HotOrCold.Model.HotOrColdModel;



public class HotOrColdPresenter implements MVP.Presenter{
    private MVP.Model model;
    private MVP.View view;
    private ArrayList<Beacon> beaconsArrayList = new ArrayList<Beacon>();
    private String beaconAddress;


    public HotOrColdPresenter(String beaconAddress){
        model = new HotOrColdModel(this);
        this.beaconAddress = beaconAddress;
    }

    @Override
    public void setView(MVP.View view) {
        this.view = view;
    }

    @Override
    public Context getContext() {

        return (Context) view;
    }


    @Override
    public void onPause() {
        model.onPause();
    }

    @Override
    public void onResume() {
        model.onResume();
    }


    @Override
    public void retrieveBeacons() {
        model.retrieveBeacons(true,beaconAddress);
    }

    @Override
    public void updateListView(ArrayList<Beacon> beacons) {
        beaconsArrayList.clear();
        beaconsArrayList.addAll(beacons);
        view.updateListView();
    }

    @Override
    public void updateItemView(Beacon b) {

    }

    @Override
    public void removeItemView(Beacon beacon) {
        view.removeItemView(beacon);
    }

    @Override
    public void addItemView(Beacon beacon) {
        beaconsArrayList.clear();
        view.addItemView(beacon);
    }


    @Override
    public ArrayList<Beacon> getBeacons() {
        return beaconsArrayList;
    }
}
