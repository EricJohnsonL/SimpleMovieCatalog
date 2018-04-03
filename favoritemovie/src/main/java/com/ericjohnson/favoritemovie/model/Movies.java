package com.ericjohnson.favoritemovie.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.ericjohnson.favoritemovie.db.DatabaseContract;

import static android.provider.BaseColumns._ID;
import static com.ericjohnson.favoritemovie.db.DatabaseContract.MoviesColumns.TITLE;
import static com.ericjohnson.favoritemovie.db.DatabaseContract.getColumnInt;
import static com.ericjohnson.favoritemovie.db.DatabaseContract.getColumnString;

/**
 * Created by EricJohnson on 3/1/2018.
 */

public class Movies implements Parcelable {

    private int id;

    private String title;

    private String poster;

    private String releaseDate;

    public Movies() {
    }

    public Movies(int id, String title, String poster, String releaseDate) {
        this.id = id;
        this.title = title;
        this.poster = poster;
        this.releaseDate = releaseDate;
    }



    public Movies(Cursor cursor){
        this.id = getColumnInt(cursor, _ID);
        this.title = getColumnString(cursor, TITLE);
        this.poster = getColumnString(cursor, DatabaseContract.MoviesColumns.POSTER);
        this.releaseDate = getColumnString(cursor, DatabaseContract.MoviesColumns.RELEASEDATE);

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.poster);
        dest.writeString(this.releaseDate);
    }

    protected Movies(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.poster = in.readString();
        this.releaseDate = in.readString();
    }

    public static final Creator<Movies> CREATOR = new Creator<Movies>() {
        @Override
        public Movies createFromParcel(Parcel source) {
            return new Movies(source);
        }

        @Override
        public Movies[] newArray(int size) {
            return new Movies[size];
        }
    };
}
