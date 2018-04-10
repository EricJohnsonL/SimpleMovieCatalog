package com.ericjohnson.moviecatalogue.activity;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Loader;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ericjohnson.moviecatalogue.BuildConfig;
import com.ericjohnson.moviecatalogue.R;
import com.ericjohnson.moviecatalogue.db.MoviesHelper;
import com.ericjohnson.moviecatalogue.loader.MovieDetailAsynctaskLoader;
import com.ericjohnson.moviecatalogue.model.Genre;
import com.ericjohnson.moviecatalogue.model.MovieDetail;
import com.ericjohnson.moviecatalogue.utils.DateUtil;
import com.ericjohnson.moviecatalogue.utils.Keys;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ericjohnson.moviecatalogue.db.DatabaseContract.CONTENT_URI;
import static com.ericjohnson.moviecatalogue.db.DatabaseContract.MoviesColumns.POSTER;
import static com.ericjohnson.moviecatalogue.db.DatabaseContract.MoviesColumns.RELEASEDATE;
import static com.ericjohnson.moviecatalogue.db.DatabaseContract.MoviesColumns.TITLE;
import static com.ericjohnson.moviecatalogue.db.DatabaseContract.MoviesColumns._ID;

public class MovieDetailActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<MovieDetail> {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.pb_movie_detail)
    ProgressBar pbMovieDetail;

    @BindView(R.id.tv_movie_detail_error)
    TextView tvMovieDetailError;

    @BindView(R.id.iv_poster_detail)
    ImageView ivPosterDetail;

    @BindView(R.id.tv_title_detail)
    TextView tvTitleDetail;

    @BindView(R.id.tv_label_release_date)
    TextView tvLabelReleaseDate;

    @BindView(R.id.tv_release_date_detail)
    TextView tvReleaseDateDetail;

    @BindView(R.id.tv_label_genre)
    TextView tvLabelGenre;

    @BindView(R.id.tv_genre)
    TextView tvGenre;

    @BindView(R.id.tv_label_language)
    TextView tvLabelLanguage;

    @BindView(R.id.tv_language)
    TextView tvLanguage;

    @BindView(R.id.tv_rating_detail)
    TextView tvRatingDetail;

    @BindView(R.id.tv_synopsis)
    TextView tvSynopsis;

    @BindView(R.id.sv_movie_detail)
    ScrollView svMovieDetail;

    @BindView(R.id.ib_favourite)
    ImageButton ibFavourite;

    private boolean isFavourited = false;

    private int id;

    private String imageUrl, releaseDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        MoviesHelper moviesHelper = new MoviesHelper(this);
        moviesHelper.open();

        tvLabelReleaseDate.setPaintFlags(tvLabelReleaseDate.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        tvLabelLanguage.setPaintFlags(tvLabelLanguage.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        tvLabelGenre.setPaintFlags(tvLabelGenre.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        id = getIntent().getIntExtra(Keys.KEY_MOVIE_ID, 0);
        final String title = getIntent().getStringExtra(Keys.KEY_TITLE);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        getMovieDetail(id);
        ibFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFavourited) {
                    ContentValues values = new ContentValues();
                    values.put(_ID, id);
                    values.put(TITLE, tvTitleDetail.getText().toString());
                    values.put(POSTER, imageUrl);
                    values.put(RELEASEDATE, releaseDate);

                    getContentResolver().insert(CONTENT_URI, values);
                    isFavourited = true;
                    ibFavourite.setBackground(getResources().getDrawable(R.drawable.ic_favorite));
                    Toast.makeText(MovieDetailActivity.this, R.string.label_added_to_favourite,
                            Toast.LENGTH_SHORT).show();
                } else {
                    if (getIntent().getData() != null) {
                        getContentResolver().delete(getIntent().getData(), null, null);
                        isFavourited = false;
                        ibFavourite.setBackground(getResources().getDrawable(R.drawable.ic_favorite_border));
                        Toast.makeText(MovieDetailActivity.this, R.string.label_removed_from_favourite,
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public Loader<MovieDetail> onCreateLoader(int id, Bundle args) {
        int movieId = args.getInt(Keys.KEY_MOVIE_ID);
        return new MovieDetailAsynctaskLoader(this, movieId);
    }

    @Override
    public void onLoadFinished(Loader<MovieDetail> loader, MovieDetail data) {
        if (data != null) {
            pbMovieDetail.setVisibility(View.GONE);
            svMovieDetail.setVisibility(View.VISIBLE);

            tvTitleDetail.setText(!TextUtils.isEmpty(data.getTitle()) ? data.getTitle() : "");
            tvReleaseDateDetail.setText(!TextUtils.isEmpty(data.getReleaseDate()) ? DateUtil.getReadableDate(data.getReleaseDate()) : "-");
            tvLanguage.setText(!TextUtils.isEmpty(data.getLanguage()) ? data.getLanguage() : "-");
            tvSynopsis.setText(!TextUtils.isEmpty(data.getOverview()) ? data.getOverview() : "-");

            ivPosterDetail.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_image));

            imageUrl = data.getPoster();
            releaseDate = data.getReleaseDate();

            if (data.getPoster() != null) {
                Glide.with(this).load(BuildConfig.IMAGE_URL + data.getPoster()).into(ivPosterDetail);
            }

            String rating;
            if (data.getVoteAverage() > 0) {
                DecimalFormat decimalFormat = new DecimalFormat("0.0");
                rating = decimalFormat.format(data.getVoteAverage());
                if (data.getVoteAverage() > 7.5) {
                    tvRatingDetail.setTextColor(ContextCompat.getColor(this, R.color.green));
                } else {
                    tvRatingDetail.setTextColor(ContextCompat.getColor(this, R.color.yellow));
                }
            } else {
                rating = "-";
                tvRatingDetail.setTextColor(ContextCompat.getColor(this, R.color.black));
            }
            tvRatingDetail.setText(rating);

            if (data.getGenres() != null) {
                StringBuilder genres = new StringBuilder();
                for (Genre genre : data.getGenres()) {
                    genres.append(genre.getGenreName()).append(", ");
                }
                genres.deleteCharAt(genres.length() - 2);
                tvGenre.setText(genres.toString());
            } else {
                tvGenre.setText("-");
            }

            Uri uri = getIntent().getData();
            if (uri != null) {
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                if (cursor != null) {
                    if (cursor.getCount() == 0) {
                        ibFavourite.setBackground(getResources().getDrawable(R.drawable.ic_favorite_border));
                        isFavourited = false;
                    } else {
                        ibFavourite.setBackground(getResources().getDrawable(R.drawable.ic_favorite));
                        isFavourited = true;
                    }
                    cursor.close();
                }
            }

        } else {
            pbMovieDetail.setVisibility(View.GONE);
            tvMovieDetailError.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<MovieDetail> loader) {

    }

    private void getMovieDetail(int id) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            Bundle bundle = new Bundle();
            bundle.putInt(Keys.KEY_MOVIE_ID, id);
            getLoaderManager().initLoader(0, bundle, this);
            if (pbMovieDetail.getVisibility() == View.GONE) {
                pbMovieDetail.setVisibility(View.VISIBLE);
                tvMovieDetailError.setVisibility(View.GONE);
            }
        } else {
            pbMovieDetail.setVisibility(View.GONE);
            tvMovieDetailError.setVisibility(View.VISIBLE);
            tvMovieDetailError.setText(R.string.label_no_internet_connection);
        }
    }
}
