package com.ericjohnson.moviecatalogue.fragment;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ericjohnson.moviecatalogue.R;
import com.ericjohnson.moviecatalogue.adapter.MoviesCursorAdapter;
import com.ericjohnson.moviecatalogue.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.provider.BaseColumns._ID;
import static com.ericjohnson.moviecatalogue.db.DatabaseContract.CONTENT_URI;
import static com.ericjohnson.moviecatalogue.db.DatabaseContract.MoviesColumns.POSTER;
import static com.ericjohnson.moviecatalogue.db.DatabaseContract.MoviesColumns.RELEASEDATE;
import static com.ericjohnson.moviecatalogue.db.DatabaseContract.MoviesColumns.TITLE;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavouriteFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.progress_bar)
    ProgressBar pbMovies;

    @BindView(R.id.tv_empty_result)
    TextView tvEmptyResult;

    @BindView(R.id.rv_movie_list)
    RecyclerView rvMovieList;

    private Cursor movieList;

    private MoviesCursorAdapter adapter;

    Unbinder unbinder;

    public FavouriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        unbinder = ButterKnife.bind(this, view);
        tvTitle.setText(getString(R.string.label_favourite_movie));

        adapter = new MoviesCursorAdapter(getActivity());
        adapter.setCursor(movieList);
        rvMovieList.setHasFixedSize(true);
        rvMovieList.setLayoutManager(new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false));
        rvMovieList.setItemAnimator(new DefaultItemAnimator());
        rvMovieList.setAdapter(adapter);

        getLoaderManager().initLoader(Constants.FAVORITE_MOVIE_TYPE, null, this);
        return view;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {_ID, TITLE, POSTER, RELEASEDATE};
        return new CursorLoader(getContext(), CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        pbMovies.setVisibility(View.GONE);
        if (data != null && data.getCount() > 0) {
            tvEmptyResult.setVisibility(View.GONE);
            tvTitle.setVisibility(View.VISIBLE);
            rvMovieList.setVisibility(View.VISIBLE);
            adapter.setCursor(data);
        } else {
            tvEmptyResult.setVisibility(View.VISIBLE);
            tvEmptyResult.setText(R.string.label_empty_favorite_movies);
            rvMovieList.setVisibility(View.GONE);
            tvTitle.setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        adapter.setCursor(null);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
