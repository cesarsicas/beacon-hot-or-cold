package br.com.cesarsicas.beaconhotorcold.Domains.Main.View.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import br.com.cesarsicas.beaconhotorcold.Core.Domains.Beacon;
import br.com.cesarsicas.beaconhotorcold.Core.Interfaces.MVP;
import br.com.cesarsicas.beaconhotorcold.Domains.Main.Presenter.MainPresenter;
import br.com.cesarsicas.beaconhotorcold.Domains.Main.View.Adapters.BeaconArrayAdapter;
import br.com.cesarsicas.beaconhotorcold.R;


public class MainActivity extends Activity implements MVP.View {
    MVP.Presenter presenter;
    private static final String TAG = "MainActivity";
    BeaconArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(presenter == null){
            presenter = new MainPresenter();
        }

        presenter.setView(this);
        presenter.retrieveBeacons();

    }


    @Override
    protected void onStart() {
        super.onStart();

        arrayAdapter = new BeaconArrayAdapter(this, R.layout.beacon_list_item, presenter.getBeacons());

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(arrayAdapter);
        listView.setEmptyView(findViewById(R.id.placeholder));

    }


    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void updateListView() {
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateItemView(int posicao) {
        //...
    }

    @Override
    public void removeItemView(Beacon beacon) {
        arrayAdapter.remove(beacon);
    }

    @Override
    public void addItemView(Beacon beacon) {

        arrayAdapter.add(beacon);
    }

}
