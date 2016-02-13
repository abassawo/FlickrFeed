package abassawo.c4q.nyc.flickrfeed.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import abassawo.c4q.nyc.flickrfeed.services.PollService;
import abassawo.c4q.nyc.flickrfeed.model.QueryPrefs;

/**
 * Created by c4q-Abass on 10/28/15.
 */
public class StartupReceiver extends BroadcastReceiver {
    private static final String TAG = "StartupReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Received broadcast intent " + intent.getAction());

        boolean isOn = QueryPrefs.isAlarmOn(context);
        PollService.setServiceAlarm(context, isOn);
    }
}
