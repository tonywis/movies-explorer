package com.tonywis.movieexplorer.models.contents;

import com.google.gson.reflect.TypeToken;
import com.tonywis.movieexplorer.models.contents.lists.Genre;
import com.tonywis.movieexplorer.models.contents.lists.ReleaseDate;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Tony on 03/02/2018.
 */

public class MovieDetails {
    public int id;
    public String title;
    public String backdrop_path;
    public List<Genre> genres;
    public String overview;
    public long budget;
    public float vote_average;

    public String release_date;

    public static Type typeAnswerOf() {
        return new TypeToken<MovieDetails>() {
        }.getType();
    }
}
