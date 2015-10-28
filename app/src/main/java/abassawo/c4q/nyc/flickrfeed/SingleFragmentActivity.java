package abassawo.c4q.nyc.flickrfeed;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class SingleFragmentActivity extends AppCompatActivity {

    public static Intent newIntent(Context context){
        return new Intent(context, SingleFragmentActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_container);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.main_container);

        if(fragment == null){
            fm.beginTransaction().add(R.id.main_container, GalleryFragment.newInstance()).commit();
        }

    }
}