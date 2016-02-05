package abassawo.c4q.nyc.flickrfeed.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import abassawo.c4q.nyc.flickrfeed.fragments.GalleryFragment;
import abassawo.c4q.nyc.flickrfeed.R;

public class SingleFragmentActivity extends AppCompatActivity {


    public static Intent newIntent(Context context){
        return new Intent(context, SingleFragmentActivity.class);
    }

    public Fragment createfragment(){
        return GalleryFragment.newInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_container);
        ActionBar ab = getSupportActionBar();
        setupActionBar(ab);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.main_container);

        if(fragment == null){
            fm.beginTransaction().add(R.id.main_container, createfragment()).commit();
        }

    }

    public void setupActionBar(ActionBar ab){
        ab.setLogo(R.mipmap.ic_flickr);
    }
}