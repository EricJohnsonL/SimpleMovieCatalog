package com.ericjohnson.moviecatalogue.model;

/**
 * Created by EricJohnson on 3/1/2018.
 */

public class Movies {

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

}
