package br.com.cesarsicas.beaconhotorcold.Domains.HotOrCold.View.Activities;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import br.com.cesarsicas.beaconhotorcold.Core.Domains.Beacon;
import br.com.cesarsicas.beaconhotorcold.Core.Interfaces.MVP;
import br.com.cesarsicas.beaconhotorcold.Core.Utils.SongUtils;
import br.com.cesarsicas.beaconhotorcold.Domains.HotOrCold.Presenter.HotOrColdPresenter;
import br.com.cesarsicas.beaconhotorcold.Domains.HotOrCold.View.Adapters.HotOrColdArrayAdapter;
import br.com.cesarsicas.beaconhotorcold.Domains.Main.View.Adapters.BeaconArrayAdapter;
import br.com.cesarsicas.beaconhotorcold.R;

public class HotOrColdActivity extends Activity implements MVP.View{
    public static final String DEVICE_ADDRESS = "deviceaddress";
    MVP.Presenter presenter;
    private static final String TAG = "MainActivity";
    HotOrColdArrayAdapter arrayAdapter;

    public MediaPlayer mediaPlayer;
    private int mInterval = 5000; // 5 seconds by default, can be changed later
    private Handler mHandler;
    private TextView songInterval;

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                Logger.wtf("Play song - interval: "+mInterval);
                playSong();
            } finally {
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };

    String receivedBeaconDeviceAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_or_cold);


        songInterval = (TextView) findViewById(R.id.songInterval);

        receivedBeaconDeviceAddress = getIntent().getStringExtra(this.DEVICE_ADDRESS);


        Logger.wtf(receivedBeaconDeviceAddress);

        if(presenter == null){
            presenter = new HotOrColdPresenter(receivedBeaconDeviceAddress);
        }

        presenter.setView(this);
        presenter.retrieveBeacons();

        mediaPlayer = new MediaPlayer();
        mHandler = new Handler();
        startSongTask();
    }

    @Override
    protected void onStart() {
        super.onStart();

        arrayAdapter = new HotOrColdArrayAdapter(this, R.layout.beacon_list_item, presenter.getBeacons());

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
    public void onDestroy(){
        stopSongTask();
        stopSong();
        super.onDestroy();
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
        //change music speed
        updateSongInterval(beacon);
        arrayAdapter.add(beacon);

    }


    public void stopSongTask(){
        mHandler.removeCallbacks(mStatusChecker);

    }

    public void stopSong(){
        if (mediaPlayer != null ) {
            if(mediaPlayer.isPlaying()){
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
            }

        }
    }

    public void startSongTask(){
        mStatusChecker.run();

    }

    public void playSong(){
        if(mediaPlayer != null){
            SongUtils.playSample(R.raw.shortbeep2, this,mediaPlayer);
        }
    }

    public void updateSongInterval(Beacon beacon){
        Long distance = 2l;
        if(beacon.uidStatus != null ){
           distance = Math.round( distanceFromRssi(beacon.rssi, beacon.uidStatus.txPower));
        }

        this.mInterval = distance.intValue() * 1000;

        this.songInterval.setText(Long.toString(mInterval));

    }

    private double distanceFromRssi(int rssi, int txPower0m) {
        int pathLoss = txPower0m - rssi;
        return Math.pow(10, (pathLoss - 41) / 20.0);
    }
}
