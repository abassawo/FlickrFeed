package abassawo.c4q.nyc.flickrfeed.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.List;

import abassawo.c4q.nyc.flickrfeed.fragments.GalleryFragment;
import abassawo.c4q.nyc.flickrfeed.R;
import abassawo.c4q.nyc.flickrfeed.fragments.LocatrFragment;
import abassawo.c4q.nyc.flickrfeed.services.PollService;
import butterknife.Bind;
import butterknife.ButterKnife;

public class MainTabActivity extends LocationActivity implements View.OnClickListener, GalleryFragment.OnFragmentInteractionListener {
    @Bind(R.id.nav_view) NavigationView navView;
    @Bind(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.viewpager) ViewPager mViewPager;
    @Bind(R.id.tabs) TabLayout tabLayout;
    @Bind(R.id.appbar) AppBarLayout appBarLayout;
    private TabAdapter adapter;


    public static Intent newIntent(Context context) {
        return new Intent(context, MainTabActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupNavBar(navView);
        setupViewPager(mViewPager);
        tabLayout.setupWithViewPager(mViewPager);
        ViewPager.OnPageChangeListener listener = new TabLayout.TabLayoutOnPageChangeListener(tabLayout);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                appBarLayout.setExpanded(true);
            }

            @Override
            public void onPageSelected(int position) {
                appBarLayout.setExpanded(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        setupActionBar(mToolbar);
    }


    public void setupViewPager(ViewPager viewPager) {
        adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(GalleryFragment.newInstance(), "Flickr Feed");
        adapter.addFragment(LocatrFragment.getInstance(), "Nearby");
        viewPager.setAdapter(adapter);
    }

    public void showBackgroundPollingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.start_polling)
                .setMessage(getResources().getString(R.string.dialog_msg));
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                PollService.setServiceAlarm(getApplicationContext(), true);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                PollService.setServiceAlarm(getApplicationContext(), false);
                MenuItem menuItem = navView.getMenu().findItem(R.id.menu_item_toggle_polling);
                SwitchCompat toggle = (SwitchCompat) MenuItemCompat.getActionView(menuItem);
                toggle.setChecked(false);
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
    }

    public void setupNavBar(NavigationView nav) {
        nav.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        if (menuItem.getItemId() == R.id.nav_favorites) {
                            startActivity(new Intent(getApplicationContext(), SingleFragmentActivity.class));
                        }
                        return true;
                    }
                });
    }

    public void setupActionBar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setLogo(R.mipmap.ic_flickr);
        ab.setTitle(R.string.app_name);
        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_TITLE |
                ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP);
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    mDrawerLayout.closeDrawers();
                } else
                    mDrawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return true;

    }


    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawers();
        } else {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_view:
                mDrawerLayout.closeDrawers();
                break;
        }
    }

    @Override
    public void onQuerySubmitted(String query) {
        if(query != null)
        tabLayout.getTabAt(0).setText(query);
    }

    @Override
    public void onRecentQuery() {
        tabLayout.getTabAt(0).setText("Recent Uploads");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        MenuItem menuItem = navView.getMenu().findItem(R.id.menu_item_toggle_polling);
        SwitchCompat toggle = (SwitchCompat) MenuItemCompat.getActionView(menuItem);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    showBackgroundPollingDialog();
                }
                else {
                    PollService.setServiceAlarm(getApplicationContext(), false);
                }
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    static class TabAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public TabAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }


        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

}
