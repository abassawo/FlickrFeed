package abassawo.c4q.nyc.flickrfeed.activities;


import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;

import abassawo.c4q.nyc.flickrfeed.fragments.PhotoDetailFragment;
import abassawo.c4q.nyc.flickrfeed.R;
import abassawo.c4q.nyc.flickrfeed.model.DatabaseHelper;
import abassawo.c4q.nyc.flickrfeed.model.GalleryItem;
import abassawo.c4q.nyc.flickrfeed.model.Serializer;
import butterknife.Bind;
import butterknife.ButterKnife;

public class WebViewActivity extends AppCompatActivity implements PhotoDetailFragment.OnWebviewLoaded, View.OnClickListener{
    public static final String INTENT_EXTRA_GALLERY_ITEM ="extra_key_item" ;
    @Bind(R.id.backdrop) ImageView backDrop;
    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.fave_fab)
    FloatingActionButton faveFab;
    private GalleryItem mItem;


    public Fragment getFragment() {
        return PhotoDetailFragment.newInstance(getIntent().getData());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        setupActionBar(mToolbar);
        mItem = (GalleryItem) getIntent().getSerializableExtra(INTENT_EXTRA_GALLERY_ITEM);
        String url = mItem.getUrl();
        Picasso.with(this).load(url).into(backDrop);
        loadFragment(getFragment());
        faveFab.setOnClickListener(this);
    }


    public void setupActionBar(Toolbar toolbar){
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setLogo(R.mipmap.ic_flickr);
        ab.setDisplayShowTitleEnabled(false);
        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM |
                ActionBar.DISPLAY_SHOW_HOME);
    }


    public static Intent newIntent(Context context, Uri photoUri){
        Intent i = new Intent(context, WebViewActivity.class);
        i.setData(photoUri);
        return i;
    }

    public void loadFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentById(R.id.detail_main_content) == null)
            fm.beginTransaction().add(R.id.detail_main_content, fragment).commit();
        else fm.beginTransaction().replace(R.id.detail_main_content, fragment).commit();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        ActionBar ab = (ActionBar) getSupportActionBar();
        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        ab.setDefaultDisplayHomeAsUpEnabled(true);
        ab.setTitle(R.string.app_name);
        ab.setDisplayHomeAsUpEnabled(true);
        MenuItem actionItem = (MenuItem) menu.findItem(R.id.share_image);
        setupShareMenuItem(actionItem);
        return true;
    }

    public void setupShareMenuItem(MenuItem actionItem ){
        ShareActionProvider actionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(actionItem);
        actionProvider.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, getIntent().getData());
        shareIntent.setType("image/jpeg");
        actionItem.setIcon(android.R.drawable.ic_menu_share);
        actionProvider.setShareIntent(shareIntent);
        actionProvider.setOnShareTargetSelectedListener(new ShareActionProvider.OnShareTargetSelectedListener() {

            @Override
            public boolean onShareTargetSelected(ShareActionProvider source, Intent intent) {
                String packageName = intent.getComponent().getPackageName();
                intent.putExtra(Intent.EXTRA_SUBJECT, "Check out this pic");
                return false;
            }
        });
    }



    @Override
    public void onWebViewLoaded() {
        backDrop.setAlpha(-1);
        backDrop.setColorFilter(R.color.colorPrimary);
    }


    public void onLikeFabClick(GalleryItem item) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Serializer serializer = new Serializer(this, db);
        serializer.addFavorite((GalleryItem) getIntent().getSerializableExtra(WebViewActivity.INTENT_EXTRA_GALLERY_ITEM));
        Toast.makeText(this, "Added to Favorites", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.fave_fab: onLikeFabClick(mItem);
                break;
        }
    }
}
