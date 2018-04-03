package com.ericjohnson.moviecatalogue.adapter;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ericjohnson.moviecatalogue.BuildConfig;
import com.ericjohnson.moviecatalogue.R;
import com.ericjohnson.moviecatalogue.activity.MovieDetailActivity;
import com.ericjohnson.moviecatalogue.listener.CustomOnClickLIstener;
import com.ericjohnson.moviecatalogue.model.Movies;
import com.ericjohnson.moviecatalogue.utils.DateUtil;
import com.ericjohnson.moviecatalogue.utils.Keys;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ericjohnson.moviecatalogue.db.DatabaseContract.CONTENT_URI;

public class MoviesCursorAdapter extends RecyclerView.Adapter<MoviesCursorAdapter.ViewHolder> {

    private Cursor movies;

    private Activity activity;

    public MoviesCursorAdapter(Activity activity) {
        this.activity = activity;
    }

    public void setCursor(Cursor cursor) {
        this.movies = cursor;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Movies movies = getItem(position);
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_image)
                .error(R.drawable.ic_image)
                .fallback(R.drawable.ic_image);

        Glide.with(activity).setDefaultRequestOptions(requestOptions).
                load(BuildConfig.IMAGE_URL+ movies.getPoster()).into(holder.ivPoster);
        holder.tvTitle.setText(movies.getTitle());
        holder.tvReleaseDate.setText(DateUtil.getReadableDate(movies.getReleaseDate()));
        holder.cvMovies.setOnClickListener(new CustomOnClickLIstener(position, new CustomOnClickLIstener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                Intent intent = new Intent(activity, MovieDetailActivity.class);
                Uri uri = Uri.parse(CONTENT_URI + "/" + movies.getId());
                intent.putExtra(Keys.KEY_MOVIE_ID, movies.getId());
                intent.putExtra(Keys.KEY_TITLE, movies.getTitle());
                intent.setData(uri);
                activity.startActivity(intent);
            }
        }));
    }

    private Movies getItem(int position) {
        if (!movies.moveToPosition(position)) {
            throw new IllegalStateException("Position invalid");
        }
        return new Movies(movies);
    }

    @Override
    public int getItemCount() {
        if (movies == null) return 0;
        return movies.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_poster)
        ImageView ivPoster;

        @BindView(R.id.tv_title)
        TextView tvTitle;

        @BindView(R.id.tv_release_date)
        TextView tvReleaseDate;

        @BindView(R.id.cv_movies)
        CardView cvMovies;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
