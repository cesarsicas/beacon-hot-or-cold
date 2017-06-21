package br.com.cesarsicas.beaconhotorcold.Core.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * Created by julio on 03/06/17.
 */

public class DialogUtils {

    // Pops an AlertDialog that quits the app on OK.
    public static void showFinishingAlertDialog(final Activity activity, String title, String message) {
        new AlertDialog.Builder(activity).setTitle(title).setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        activity.finish();
                    }
                }).show();
    }
}
