package com.ericjohnson.moviecatalogue.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.ericjohnson.moviecatalogue.R;
import com.ericjohnson.moviecatalogue.service.SchedulerTask;
import com.ericjohnson.moviecatalogue.utils.Keys;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tv_upcoming)
    TextView tvUpcoming;

    @BindView(R.id.sw_upcoming)
    Switch swUpcoming;

    @BindView(R.id.ll_language)
    LinearLayout llLanguage;

    private SchedulerTask schedulerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.label_setting));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        llLanguage.setOnClickListener(this);

        schedulerTask = new SchedulerTask(SettingActivity.this);

        if (getPreferences(Context.MODE_PRIVATE).getString(Keys.PREF_UPCOMING_NOTIF,
                getString(R.string.label_off)).equals(getString(R.string.label_on))) {
            swUpcoming.setChecked(true);
            schedulerTask.createPeriodicTask();
        }


        swUpcoming.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    schedulerTask.createPeriodicTask();
                    getPreferences(Context.MODE_PRIVATE).edit().putString(Keys.PREF_UPCOMING_NOTIF,
                            getString(R.string.label_on)).apply();
                } else {
                    schedulerTask.cancelPeriodicTask();
                    getPreferences(Context.MODE_PRIVATE).edit().putString(Keys.PREF_UPCOMING_NOTIF,
                            getString(R.string.label_off)).apply();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_language:
                Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(intent);
        }
    }
}


