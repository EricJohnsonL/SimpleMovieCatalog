package com.ericjohnson.moviecatalogue.model;

import java.util.ArrayList;

/**
 * Created by EricJohnson on 3/3/2018.
 */

public class MovieDetail {

    private int id;

    private String title;

    private String language;

    private String overview;

    private String poster;

    private ArrayList<Genre> genres;

    private String releaseDate;

    private float voteAverage;

    public MovieDetail(int id, String title, String language, String overview, String poster, ArrayList<Genre> genres, String releaseDate, float voteAverage) {
        this.id = id;
        this.title = title;
        this.language = language;
        this.overview = overview;
        this.poster = poster;
        this.genres = genres;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
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

    public String getLanguage() {
        return language;
    }

    public String getOverview() {
        return overview;
    }

    public String getPoster() {
        return poster;
    }

    public ArrayList<Genre> getGenres() {
        return genres;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public float getVoteAverage() {
        return voteAverage;
    }
}
