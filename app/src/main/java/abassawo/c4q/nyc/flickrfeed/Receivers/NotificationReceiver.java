package abassawo.c4q.nyc.flickrfeed.receivers;

import android.app.Activity;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import abassawo.c4q.nyc.flickrfeed.services.PollService;

/**
 * Created by c4q-Abass on 10/28/15.
 */
public class NotificationReceiver extends BroadcastReceiver {
    private static final String TAG = "NotificationReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Received result: " + getResultCode());
        if(getResultCode() != Activity.RESULT_OK) {
            //A foreground activity has cancelled the broadcast}
            return;
        } else {
            int requestCode = intent.getIntExtra(PollService.REQUEST_CODE, 0);
            Notification notification = (Notification) intent.getParcelableExtra(PollService.NOTIFICATION);
            NotificationManagerCompat notificationMan = NotificationManagerCompat.from(context);
            notificationMan.notify(requestCode, notification);
        }
    }
}
