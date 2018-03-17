package com.ericjohnson.moviecatalogue.activity;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ericjohnson.moviecatalogue.R;
import com.ericjohnson.moviecatalogue.loader.MovieDetailAsynctaskLoader;
import com.ericjohnson.moviecatalogue.model.Genre;
import com.ericjohnson.moviecatalogue.model.MovieDetail;
import com.ericjohnson.moviecatalogue.utils.DateUtil;
import com.ericjohnson.moviecatalogue.utils.Keys;
import com.ericjohnson.moviecatalogue.utils.Uri;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    @BindView(R.id.ll_movie_detail)
    LinearLayout llMovieDetail;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        tvLabelReleaseDate.setPaintFlags(tvLabelReleaseDate.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        tvLabelLanguage.setPaintFlags(tvLabelLanguage.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        tvLabelGenre.setPaintFlags(tvLabelGenre.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        int id = getIntent().getIntExtra(Keys.KEY_MOVIE_ID, 0);
        String title = getIntent().getStringExtra(Keys.KEY_TITLE);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getMovieDetail(id);

    }

    @Override
    public Loader<MovieDetail> onCreateLoader(int id, Bundle args) {
        int movieId = args.getInt(Keys.KEY_MOVIE_ID);
        return new MovieDetailAsynctaskLoader(this, movieId);
    }

    @Override
    public void onLoadFinished(Loader<MovieDetail> loader, MovieDetail data) {
        pbMovieDetail.setVisibility(View.GONE);
        llMovieDetail.setVisibility(View.VISIBLE);

        tvTitleDetail.setText(!TextUtils.isEmpty(data.getTitle()) ? data.getTitle() : null);
        tvReleaseDateDetail.setText(!TextUtils.isEmpty(data.getReleaseDate()) ? DateUtil.getReadableDate(data.getReleaseDate()) : "-");
        tvLanguage.setText(!TextUtils.isEmpty(data.getLanguage()) ? data.getLanguage() : "-");
        tvSynopsis.setText(!TextUtils.isEmpty(data.getOverview()) ? data.getOverview() : "-");

        ivPosterDetail.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_image));

        if (data.getPoster() != null) {
            Glide.with(this).load(Uri.imageUrl + data.getPoster()).into(ivPosterDetail);
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
}
