package com.ericjohnson.moviecatalogue.service;

import android.content.Context;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;

/**
 * Created by EricJohnson on 3/16/2018.
 */

public class SchedulerTask {

    private GcmNetworkManager gcmNetworkManager;

    public SchedulerTask(Context context) {
        gcmNetworkManager = GcmNetworkManager.getInstance(context);
    }

    public void createPeriodicTask() {
        Task task = new PeriodicTask.Builder()
                .setService(UpcomingMovieServices.class)
                .setPeriod(10)
                .setRequiredNetwork(PeriodicTask.NETWORK_STATE_CONNECTED)
                .setTag(UpcomingMovieServices.TAG)
                .setPersisted(true)
                .build();
        gcmNetworkManager.schedule(task);
    }

    public void cancelPeriodicTask() {
        if (gcmNetworkManager != null) {
            gcmNetworkManager.cancelTask(UpcomingMovieServices.TAG,
                    UpcomingMovieServices.class);
        }
    }
}
