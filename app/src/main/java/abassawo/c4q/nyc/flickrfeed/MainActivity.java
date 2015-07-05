package abassawo.c4q.nyc.flickrfeed;


import android.content.Intent;
import android.support.v7.media.MediaRouter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.MediaRouteActionProvider;
import android.support.v7.media.MediaRouteSelector;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListView;

import com.google.android.gms.cast.Cast;
import com.google.android.gms.cast.CastDevice;
import com.google.android.gms.cast.CastMediaControlIntent;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import android.graphics.Color;


public class MainActivity extends ActionBarActivity {
    GridView mGridView;
    Button buttonReload;
    ImageAdapter adapter;
    private List flickrList;
    private AsyncLoading flickrReload;
    private String APP_ID = "0";
    private String mActivityTitle;
    private Intent detailIntent = null;
    private String[] mDrawerListItems;
    private FrameLayout frameLayout;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mTitle;



    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int REQUEST_CODE = 1;

    private android.support.v7.media.MediaRouter mMediaRouter;
    private MediaRouteSelector mMediaRouteSelector;
    private android.support.v7.media.MediaRouter.Callback mMediaRouterCallback;
    private CastDevice mSelectedDevice;
    private GoogleApiClient mApiClient;
    private Cast.Listener mCastListener;
    private GoogleApiClient.ConnectionCallbacks mConnectionCallbacks;
    private HelloWorldChannel mHelloWorldChannel;
    private boolean mApplicationStarted;
    private boolean mWaitingForReconnect;
    private String mSessionId;
    private MediaRouter mediaRouter;
    private MediaRouteSelector mediaRouteSelector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();
        setUpNavBar();
        new AsyncLoading().execute();
        setUpListener(true);


        mediaRouter = MediaRouter.getInstance(getApplicationContext());
        mediaRouteSelector = new MediaRouteSelector.Builder().addControlCategory(CastMediaControlIntent.categoryForCast(APP_ID)).build();

    }

    public void setUpNavBar(){
        mActivityTitle = getTitle().toString();
        frameLayout = (FrameLayout)findViewById(R.id.content_frame);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        //mDrawerLayout.setBackgroundColor(Color.CYAN);

        mDrawerList = (ListView) findViewById(R.id.navList);
        mDrawerListItems = getResources().getStringArray(R.array.navItems);
       // mDrawerList.setBackgroundColor(Color.parseColor("black"));

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close){
            //

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Return!");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mDrawerListItems);
        mDrawerList.setAdapter(mAdapter);
                mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        mDrawerLayout.closeDrawer(mDrawerList);
                        break;
                    case 1:
                        //intent = new Intent(MainActivity.this, ZodiacRomance.class);
                        Log.d("case 0", " " + getIntent());
                        //startActivity(intent);
                        //startActivity(intent);
                        mDrawerLayout.closeDrawer(mDrawerList);
                        break;
                    case 2:
                        //intent = new Intent(MainActivity.this, HoroscopeGame.class);
                        Log.d("case 0", " " + getIntent());
                        //startActivity(intent);
                        // startActivity(intent);
                        mDrawerLayout.closeDrawer(mDrawerList);

                        break;
                }

            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

    }

    private void setUpListener(boolean isResumed) {
        if (!isResumed) {
            buttonReload.setOnClickListener(null);
        } else {
            buttonReload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO : Step 6 - start the AsyncTask!
                    new AsyncLoading().execute();

                }
            });
        }
    }

    private void initializeViews() {
        mGridView = (GridView) findViewById(R.id.gridView);
        buttonReload = (Button) findViewById(R.id.button_reload);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = adapter.getItem(position);
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("imageLink", url);
                startActivity(intent);

            }
        });


    }

    public void restoreActionBar() {
       ActionBar actionBar = getSupportActionBar();
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
       actionBar.setTitle(mTitle);

        //getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setIcon(R.mipmap.ic_mustache);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
                //MenuItem mediaRouteMenuItem = menu.findItem(R.id.media_route_menu_item);
        //MediaRouteActionProvider mediaRouteActionProvider = (MediaRouteActionProvider) MenuItemCompat.getActionProvider(mediaRouteMenuItem);
        //mediaRouteActionProvider.setRouteSelector(mediaRouteSelector);
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

            // Activate the navigation drawer toggle
            if (mDrawerToggle.onOptionsItemSelected(item)) {
                return true;
            }

            return super.onOptionsItemSelected(item);
        }


    private class AsyncLoading extends AsyncTask<Void, Void, List<String>> {

        @Override
        protected List<String> doInBackground(Void... params) {
            // TODO : Step 3 - by using FlickrGetter.java, get latest 20 images' Urls from Flickr and return the result.

            try {
                flickrList = new FlickrClient().getBitmapList();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return flickrList;
        }
        @Override
        protected void onPostExecute(List<String> imageList) {
            // TODO : Step 5 - Now we have ImageAdapter and the data(list), post the picture!
            adapter = new ImageAdapter(MainActivity.this, imageList);
            mGridView.setAdapter(adapter);


        }

    }
    class HelloWorldChannel implements Cast.MessageReceivedCallback {

        /**
         * @return custom namespace
         */
        public String getNamespace() {
            return getString(R.string.namespace);
        }

        /*
         * Receive message from the receiver app
         */
        @Override
        public void onMessageReceived(CastDevice castDevice, String namespace,
                                      String message) {
            Log.d(TAG, "onMessageReceived: " + message);
        }



    }



}
