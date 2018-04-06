package com.ericjohnson.moviecatalogue.model;

public class NotificationItem {

    private int id;

    private Movies movies;

    public NotificationItem(int id, Movies movies) {
        this.id = id;
        this.movies = movies;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Movies getMovies() {
        return movies;
    }

    public void setMovies(Movies movies) {
        this.movies = movies;
    }
}
