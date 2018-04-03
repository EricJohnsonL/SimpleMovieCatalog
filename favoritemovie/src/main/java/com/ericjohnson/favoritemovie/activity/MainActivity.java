package com.ericjohnson.favoritemovie.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ericjohnson.favoritemovie.R;
import com.ericjohnson.favoritemovie.adapter.MoviesCursorAdapter;
import com.ericjohnson.favoritemovie.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.provider.BaseColumns._ID;
import static com.ericjohnson.favoritemovie.db.DatabaseContract.CONTENT_URI;
import static com.ericjohnson.favoritemovie.db.DatabaseContract.MoviesColumns.POSTER;
import static com.ericjohnson.favoritemovie.db.DatabaseContract.MoviesColumns.RELEASEDATE;
import static com.ericjohnson.favoritemovie.db.DatabaseContract.MoviesColumns.TITLE;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.progress_bar)
    ProgressBar pbMovies;

    @BindView(R.id.tv_empty_result)
    TextView tvEmptyResult;

    @BindView(R.id.rv_movie_list)
    RecyclerView rvMovieList;

    private Cursor movieList;

    private MoviesCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        adapter = new MoviesCursorAdapter(this);
        adapter.setCursor(movieList);
        rvMovieList.setHasFixedSize(true);
        rvMovieList.setLayoutManager(new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false));
        rvMovieList.setItemAnimator(new DefaultItemAnimator());
        rvMovieList.setAdapter(adapter);

        getSupportLoaderManager().initLoader(Constants.FAVORITE_MOVIE_TYPE, null, this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {_ID, TITLE, POSTER, RELEASEDATE};
        return new CursorLoader(this, CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        pbMovies.setVisibility(View.GONE);
        if (data != null && data.getCount() > 0) {
            tvEmptyResult.setVisibility(View.GONE);
            rvMovieList.setVisibility(View.VISIBLE);
            adapter.setCursor(data);
        } else {
            tvEmptyResult.setVisibility(View.VISIBLE);
            tvEmptyResult.setText(R.string.label_empty_favorite_movies);
            rvMovieList.setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        adapter.setCursor(null);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getSupportLoaderManager().destroyLoader(Constants.FAVORITE_MOVIE_TYPE);
    }
}
