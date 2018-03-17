package com.ericjohnson.moviecatalogue.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.ericjohnson.moviecatalogue.R;
import com.ericjohnson.moviecatalogue.fragment.NowPlayingFragment;
import com.ericjohnson.moviecatalogue.fragment.SearchFragment;
import com.ericjohnson.moviecatalogue.fragment.UpcomingFragment;
import com.ericjohnson.moviecatalogue.utils.Keys;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.nav_view)
    NavigationView navView;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.fl_container)
    FrameLayout flContainer;

    private ActionBarDrawerToggle toggle;

    private String title;

    private int getDrawerItem;

    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navView.setNavigationItemSelectedListener(this);

        getDrawerItem = getIntent().getIntExtra(Keys.KEY_UPCOMING_MOVIE, 0);

        if (getDrawerItem == 1) {
            currentFragment = new UpcomingFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_container, currentFragment).commit();
            navView.getMenu().getItem(getDrawerItem).setChecked(true);
            title = getString(R.string.label_upcoming_movies);
        } else if (savedInstanceState == null) {
            currentFragment = new NowPlayingFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_container, currentFragment).commit();
            navView.getMenu().getItem(0).setChecked(true);
            title = getString(R.string.label_now_playing);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportActionBar().setTitle(title);
        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    protected void onPause() {
        super.onPause();
        drawerLayout.removeDrawerListener(toggle);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_setting) {
            Intent searchIntent = new Intent(this, SettingActivity.class);
            startActivity(searchIntent);
        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        Fragment fragment = null;

        if (id == R.id.now_playing) {
            fragment = new NowPlayingFragment();
            title = getString(R.string.label_now_playing);
        } else if (id == R.id.upcoming_movies) {
            fragment = new UpcomingFragment();
            title = getString(R.string.label_upcoming_movies);
        } else if (id == R.id.search_movies) {
            fragment = new SearchFragment();
            title = getString(R.string.label_search);
        }

        if (fragment != null && !fragment.getClass().getSimpleName().equals(currentFragment.getClass().getSimpleName())) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_container, fragment).commit();
            getSupportActionBar().setTitle(title);
            currentFragment = fragment;
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
}