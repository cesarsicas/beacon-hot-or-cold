package br.com.cesarsicas.beaconhotorcold.Core.Utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.util.Log;

import com.orhanobut.logger.Logger;

import java.io.IOException;

/**
 * Created by julio on 5/31/17.
 */

public class SongUtils {


    /**
     * Play a sample with the Android MediaPLayer.
     *
     * @param resid Resource ID if the sample to play.
     */
    public static void playSample(int resid, Context context, MediaPlayer mediaPlayer) {
        AssetFileDescriptor afd = context.getResources().openRawResourceFd(resid);

        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getDeclaredLength());
            mediaPlayer.prepare();
            mediaPlayer.start();
            mediaPlayer.setLooping(false);
            afd.close();

        } catch (IllegalArgumentException e) {
            Logger.wtf("Unable to play audio queue do to exception:" + e.getMessage());
        } catch (IllegalStateException e) {
            Logger.wtf("Unable to play audio queue do to exception:" + e.getMessage());
        } catch (IOException e) {
            Logger.wtf("Unable to play audio queue do to exception:" + e.getMessage());
        }
    }



    public static void changeSampleSpeed(MediaPlayer mediaPlayer, Long distance) {

        if(mediaPlayer != null && mediaPlayer.isPlaying()){
            if(distance >= 0 && distance<= 5) {
                mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(2.0f));
            }
            else if(distance >= 5 && distance <= 10) {
                mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(1.0f));
            }

            else{
                mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(0.1f));

            }
        }

        Logger.wtf("Actual Distance ===> " + distance);

        Logger.wtf("Actual speed ===> " + mediaPlayer.getPlaybackParams().getSpeed());

    }

}
