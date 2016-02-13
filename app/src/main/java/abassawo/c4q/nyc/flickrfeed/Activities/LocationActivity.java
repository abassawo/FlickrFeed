package abassawo.c4q.nyc.flickrfeed.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * Created by c4q-Abass on 2/10/16.
 */
public class LocationActivity  extends AppCompatActivity {
    private static final int REQUEST_ERROR = 0;

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServicesAvailable();
    }

    public void checkPlayServicesAvailable(){
        int errorCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if(errorCode != ConnectionResult.SUCCESS){
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(errorCode, this, REQUEST_ERROR, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    //services unavailable;
                    finish();
                }
            });
        }
    }
}
