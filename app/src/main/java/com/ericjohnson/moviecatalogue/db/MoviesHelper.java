package com.ericjohnson.moviecatalogue.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.ericjohnson.moviecatalogue.model.Movies;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.ericjohnson.moviecatalogue.db.DatabaseContract.MoviesColumns.POSTER;
import static com.ericjohnson.moviecatalogue.db.DatabaseContract.MoviesColumns.RELEASEDATE;
import static com.ericjohnson.moviecatalogue.db.DatabaseContract.MoviesColumns.TITLE;
import static com.ericjohnson.moviecatalogue.db.DatabaseContract.TABLE_MOVIES;

public class MoviesHelper {

    private static String DATABASE_TABLE = TABLE_MOVIES;
    private Context context;
    private DatabaseHelper dataBaseHelper;

    private SQLiteDatabase database;

    public MoviesHelper(Context context) {
        this.context = context;
    }

    public MoviesHelper open() throws SQLException {
        dataBaseHelper = new DatabaseHelper(context);
        database = dataBaseHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dataBaseHelper.close();
    }

    public ArrayList<Movies> query() {
        ArrayList<Movies> arrayList = new ArrayList<Movies>();
        Cursor cursor = database.query(
                DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                _ID + " ASC",
                null);
        cursor.moveToFirst();
        Movies movies;

        if (cursor.getCount() > 0) {
            do {
                movies = new Movies();
                movies.setId(cursor.getInt(cursor.getColumnIndex(_ID)));
                movies.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
                movies.setPoster(cursor.getString(cursor.getColumnIndex(POSTER)));
                movies.setReleaseDate(cursor.getString(cursor.getColumnIndex(RELEASEDATE)));
                arrayList.add(movies);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public long insert(Movies movies) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(TITLE, movies.getTitle());
        initialValues.put(POSTER, movies.getPoster());
        initialValues.put(RELEASEDATE, movies.getReleaseDate());
        return database.insert(DATABASE_TABLE, null, initialValues);
    }

    public int delete(int id) {
        return database.delete(TABLE_MOVIES, _ID + " = '" + id + "'", null);
    }

    public Cursor queryByIdProvider(String id){
        return database.query(DATABASE_TABLE,null
                ,_ID + " = ?"
                ,new String[]{id}
                ,null
                ,null
                ,null
                ,null);
    }

    public Cursor queryProvider(){
        return database.query(DATABASE_TABLE
                ,null
                ,null
                ,null
                ,null
                ,null
                ,_ID + " ASC");
    }

    public long insertProvider(ContentValues values){
        return database.insert(DATABASE_TABLE,null,values);
    }

    public int deleteProvider(String id){
        return database.delete(DATABASE_TABLE,_ID + " = ?", new String[]{id});
    }

}
