package com.tonywis.movieexplorer.models.contents;

import com.google.gson.reflect.TypeToken;
import com.tonywis.movieexplorer.models.contents.lists.Movie;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Tony on 03/02/2018.
 */

public class ResultsUpcomingMovies {
    public List<Movie> results;

    public static Type typeAnswerOf() {
        return new TypeToken<ResultsUpcomingMovies>() {
        }.getType();
    }
}
