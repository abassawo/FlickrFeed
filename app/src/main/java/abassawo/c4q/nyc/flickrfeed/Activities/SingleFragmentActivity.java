package abassawo.c4q.nyc.flickrfeed.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.squareup.picasso.Picasso;

import abassawo.c4q.nyc.flickrfeed.R;
import abassawo.c4q.nyc.flickrfeed.fragments.FavesFragment;
import abassawo.c4q.nyc.flickrfeed.fragments.GalleryFragment;
import abassawo.c4q.nyc.flickrfeed.fragments.PhotoDetailFragment;
import abassawo.c4q.nyc.flickrfeed.model.GalleryItem;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by c4q-Abass on 2/12/16.
 */


public class SingleFragmentActivity extends AppCompatActivity implements GalleryFragment.OnFragmentInteractionListener{

@Bind(R.id.toolbar)
Toolbar mToolbar;


        public Fragment getFragment() {
            return new FavesFragment();
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_faves);
            ButterKnife.bind(this);
            setupActionBar(mToolbar);
            loadFragment(getFragment());
        }

        public void loadFragment(Fragment fragment) {
            FragmentManager fm = getSupportFragmentManager();
            if (fm.findFragmentById(R.id.detail_main_content) == null)
                fm.beginTransaction().add(R.id.detail_main_content, fragment).commit();
            else fm.beginTransaction().replace(R.id.detail_main_content, fragment).commit();
        }


        public void setupActionBar(Toolbar toolbar) {
            setSupportActionBar(toolbar);
            ActionBar ab = getSupportActionBar();
            ab.setLogo(R.mipmap.ic_flickr);
            ab.setDisplayShowTitleEnabled(true);
            ab.setTitle("Favorites");
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM |
                    ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
            ab.setDefaultDisplayHomeAsUpEnabled(true);
            ab.setDisplayHomeAsUpEnabled(true);
        }

        @Override
        public void onQuerySubmitted(String query) {
            //left blank
        }

        @Override
        public void onRecentQuery() {
            //left blank
        }
}
