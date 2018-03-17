package com.ericjohnson.moviecatalogue.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;

import com.ericjohnson.moviecatalogue.BuildConfig;
import com.ericjohnson.moviecatalogue.R;
import com.ericjohnson.moviecatalogue.activity.MainActivity;
import com.ericjohnson.moviecatalogue.model.Movies;
import com.ericjohnson.moviecatalogue.utils.DateUtil;
import com.ericjohnson.moviecatalogue.utils.Keys;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by EricJohnson on 3/16/2018.
 */

public class UpcomingMovieServices extends GcmTaskService {

    public static final String TAG = UpcomingMovieServices.class.getSimpleName();

    private ArrayList<Movies> movies = new ArrayList<>();

    public static String TAG_TASK_MOVIES_LOG = "MoviesTask";

    @Override
    public int onRunTask(TaskParams taskParams) {
        int result = 0;
        if (taskParams.getTag().equals(TAG)) {
            getUpdatedMovie();
            result = GcmNetworkManager.RESULT_SUCCESS;
        }
        return result;
    }

    private void getUpdatedMovie() {
        SyncHttpClient client = new SyncHttpClient();
        String url = "https://api.themoviedb.org/3/movie/upcoming?api_key=" + BuildConfig.API_KEY + "&language=en-US";
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray resultsArray = responseObject.getJSONArray("results");
                    for (int i = 0; i < resultsArray.length(); i++) {
                        JSONObject currentMovies = resultsArray.getJSONObject(i);

                        int id = currentMovies.getInt("id");
                        String title = currentMovies.getString("title");
                        String date = currentMovies.getString("release_date");
                        String imagePath = currentMovies.getString("poster_path");

                        if (TextUtils.isEmpty(imagePath)) {
                            imagePath = "-1";
                        }

                        date = TextUtils.isEmpty(date) ? "-" : DateUtil.getReadableDate(date);

                        Movies movie = new Movies(id, title, imagePath, date);
                        movies.add(movie);
                    }
                    Log.d(TAG_TASK_MOVIES_LOG, result);
                    String message = getString(R.string.label_notif_message);
                    int notifId = 100;
                    showNotification(getApplicationContext(), message, notifId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("GetUpcomingMovies", "Failed");
            }
        });
    }

    private void showNotification(Context context, String message, int notifId) {
        Intent notifIntent = new Intent(this, MainActivity.class);
        notifIntent.putExtra(Keys.KEY_UPCOMING_MOVIE, 1);
        PendingIntent pendingIntent = TaskStackBuilder.create(this)
                .addNextIntent(notifIntent)
                .getPendingIntent(110, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle(getString(R.string.app_name))
                .setSmallIcon(R.drawable.ic_local_movies)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.black))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSound(alarmSound);
        notificationManagerCompat.notify(notifId, builder.build());
    }
}
