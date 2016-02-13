package abassawo.c4q.nyc.flickrfeed.services;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.util.List;

import abassawo.c4q.nyc.flickrfeed.activities.MainTabActivity;
import abassawo.c4q.nyc.flickrfeed.model.FlickrFetchr;
import abassawo.c4q.nyc.flickrfeed.model.GalleryItem;
import abassawo.c4q.nyc.flickrfeed.model.QueryPrefs;
import abassawo.c4q.nyc.flickrfeed.R;

/**
 * Created by c4q-Abass on 10/28/15.
 */
public class PollService extends IntentService {

    private static final String TAG = "PollService";
    private static final long POLL_INTERVAL = AlarmManager.INTERVAL_HALF_HOUR;
    public static final String ACTION_SHOW_NOTIFICATION = "abassawo.c4q.nyc.flickrfeed.Activities.MainTabActivity.SHOW_NOTIFICATION";
    public static final String PERM_PRIVATE = "abassawo.c4q.nyc.flickrfeed.Activities.MainTabActivity.PRIVATE";
    private static final int NOTIFICATON_ID = 0;

    public static final String REQUEST_CODE = "REQUEST_CODE";
    public static final String NOTIFICATION = "NOTIFICATION";
    List<GalleryItem> items;

    public static Intent newIntent(Context context){
        return new Intent(context, PollService.class);
    }

    public PollService(){
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(!isNetWorkAvailableAndConnected()) return;
        Log.i(TAG, "Received an intent: " + intent);
        String query = QueryPrefs.getStoredQuery(this);
        String lastResultId = QueryPrefs.getfLastResultId(this);

        if(query == null){
            items = new FlickrFetchr().fetchRecentPhotos();
        } else {
            items = new FlickrFetchr().searchPhotos(query);
        }

        if(items.size() == 0) return;
        String resultId = items.get(0).getId();
        if(resultId.equals(lastResultId)){
            Log.i(TAG, "Old result: " + resultId);
        } else {
            deliverUpdateNotifications();
            Log.i(TAG, "New result: " + resultId);
        }
        QueryPrefs.setLastResultid(this, resultId);


    }

    private boolean isNetWorkAvailableAndConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        boolean isNetWorkAvailable = connectivityManager.getActiveNetworkInfo() != null;
        boolean isNetWorkConnected= isNetWorkAvailable && connectivityManager.getActiveNetworkInfo().isConnected();
        return isNetWorkConnected;
    }

    public static void setServiceAlarm(Context context, boolean isOn){
        Intent intent = PollService.newIntent(context);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);

        AlarmManager alarmMan = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if(isOn){
            Log.d(TAG, "AlarmManager -> PollService Intent");
            alarmMan.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), POLL_INTERVAL, pendingIntent);
        } else {
            alarmMan.cancel(pendingIntent);
            pendingIntent.cancel();
        }
        QueryPrefs.setAlarmOn(context, isOn);
    }

    public static boolean isServiceAlarmOn(Context context){
        Intent intent = PollService.newIntent(context);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_NO_CREATE);
        return pendingIntent != null;
    }

    public void deliverUpdateNotifications(){
        Resources resources = getResources();
        Intent intent = MainTabActivity.newIntent(this);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Bitmap flickrIcon = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.flickrlogo);

        Notification notification = new NotificationCompat.Builder(this)
                .setTicker(resources.getString(R.string.new_pictures_title))
                .setSmallIcon(android.R.drawable.ic_menu_report_image)
                .setContentTitle(resources.getString(R.string.new_pictures_title))
                .setContentText(resources.getString(R.string.new_pictures_text))
                .setLargeIcon(flickrIcon)
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(flickrIcon))
                .setContentIntent(pendingIntent)
                .setAutoCancel(false).build();
        NotificationManagerCompat notifier = NotificationManagerCompat.from(this);
        showbackgroundNotification(NOTIFICATON_ID, notification);

    }

    private void showbackgroundNotification(int requestCode, Notification notification) {
        Intent intent = new Intent(ACTION_SHOW_NOTIFICATION);
        intent.putExtra(REQUEST_CODE, requestCode);
        intent.putExtra(NOTIFICATION, notification);
        sendOrderedBroadcast(intent, PERM_PRIVATE, null, null, Activity.RESULT_OK, null, null);
    }

}
