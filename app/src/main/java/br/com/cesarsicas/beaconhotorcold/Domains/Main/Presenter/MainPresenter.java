package br.com.cesarsicas.beaconhotorcold.Domains.Main.Presenter;

import android.content.Context;

import java.util.ArrayList;

import br.com.cesarsicas.beaconhotorcold.Core.Domains.Beacon;
import br.com.cesarsicas.beaconhotorcold.Core.Interfaces.MVP;
import br.com.cesarsicas.beaconhotorcold.Domains.Main.Model.MainModel;

/**
 * Created by julio on 18/06/17.
 */

public class MainPresenter implements MVP.Presenter{
    private MVP.Model model;
    private MVP.View view;
    private ArrayList<Beacon> beaconsArrayList = new ArrayList<Beacon>();


    public MainPresenter(){
        model = new MainModel(this);
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
        model.retrieveBeacons();
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
        view.addItemView(beacon);
    }


    @Override
    public ArrayList<Beacon> getBeacons() {
        return beaconsArrayList;
    }
}
