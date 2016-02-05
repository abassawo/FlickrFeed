package abassawo.c4q.nyc.flickrfeed.activities;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import abassawo.c4q.nyc.flickrfeed.fragments.PhotoDetailFragment;
import abassawo.c4q.nyc.flickrfeed.R;

public class WebViewActivity extends SingleFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_container);
    }


    @Override
    public Fragment createfragment() {
        return PhotoDetailFragment.newInstance(getIntent().getData());
    }

    public static Intent newIntent(Context context, Uri photoUri){
        Intent i = new Intent(context, WebViewActivity.class);
        i.setData(photoUri);
        return i;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photo_detail, menu);
        ActionBar ab = (ActionBar) getSupportActionBar();
                ab.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        ab.setDefaultDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        Fragment webview = getSupportFragmentManager().findFragmentById(R.id.main_container);
        if (webview instanceof PhotoDetailFragment) {
            boolean goback = ((PhotoDetailFragment.mWebView.canGoBack()));
            if (!goback)
                super.onBackPressed();
            else
                PhotoDetailFragment.mWebView.goBack();
        }

    }

}
