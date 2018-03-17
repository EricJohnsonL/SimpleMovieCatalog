package com.ericjohnson.moviecatalogue.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.ericjohnson.moviecatalogue.BuildConfig;
import com.ericjohnson.moviecatalogue.model.Genre;
import com.ericjohnson.moviecatalogue.model.MovieDetail;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by EricJohnson on 3/3/2018.
 */

public class MovieDetailAsynctaskLoader extends AsyncTaskLoader<MovieDetail> {

    private int id;

    public MovieDetailAsynctaskLoader(Context context, int id) {
        super(context);
        this.id = id;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public MovieDetail loadInBackground() {
        SyncHttpClient client = new SyncHttpClient();

        final MovieDetail[] movieDetail = new MovieDetail[1];

        final String TAG=MovieDetailAsynctaskLoader.class.getSimpleName();

        String url = "https://api.themoviedb.org/3/movie/" + id + "?api_key=" +
                BuildConfig.API_KEY + "&language=en-US";

        client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                setUseSynchronousMode(true);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    ArrayList<Genre> genres = new ArrayList<>();
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    String title = responseObject.getString("title");
                    String poster = responseObject.getString("poster_path");
                    String releaseDate = responseObject.getString("release_date");
                    String language = responseObject.getString("original_language");
                    String overview = responseObject.getString("overview");
                    float rating = (float) responseObject.getDouble("vote_average");
                    JSONArray genresArray = responseObject.getJSONArray("genres");
                    if (genresArray.length() > 0) {
                        for (int i = 0; i < genresArray.length(); i++) {
                            JSONObject genreResult = genresArray.getJSONObject(i);
                            String genreName = genreResult.getString("name");
                            Genre genre = new Genre(genreName);
                            genres.add(genre);
                        }
                    } else {
                        genres = null;
                    }
                    Log.d(TAG, result);
                    movieDetail[0] = new MovieDetail(title, language, overview, poster, genres,
                            releaseDate, rating);

                } catch (JSONException e) {
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
        return movieDetail[0];
    }

    @Override
    protected void onReset() {
        super.onReset();
    }
}
