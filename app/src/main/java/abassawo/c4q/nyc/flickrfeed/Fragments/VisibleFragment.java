package abassawo.c4q.nyc.flickrfeed.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import abassawo.c4q.nyc.flickrfeed.services.PollService;

/**
 * Created by c4q-Abass on 10/28/15.
 */
public class VisibleFragment extends Fragment {
    private static final String TAG = "VisibleFragment";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        //Dynamic broadcast receiver we will call registerReceiver
        IntentFilter filter = new IntentFilter(PollService.ACTION_SHOW_NOTIFICATION);
        getActivity().registerReceiver(mNotifyReceiver, filter, PollService.PERM_PRIVATE, null);
    }


    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(mNotifyReceiver);
    }

    private BroadcastReceiver mNotifyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
          Log.i(TAG, "Canceling notification");
            setResultCode(Activity.RESULT_CANCELED);
        }
    };




}
