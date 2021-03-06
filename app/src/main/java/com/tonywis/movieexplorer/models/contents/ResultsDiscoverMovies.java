package com.tonywis.movieexplorer.models.contents;

import com.google.gson.reflect.TypeToken;
import com.tonywis.movieexplorer.models.contents.lists.Movie;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Tony on 03/02/2018.
 */

public class ResultsDiscoverMovies {
    public List<Movie> results;

    /**
     *
     * @param id
     * @return Movie
     */
    public Movie getMovieById(int id) {
        Movie movie = null;

        int i = 0;
        while (i < results.size() && (results.get(i).id != id)) {
            i++;
        }

        if (i < results.size())
            movie = results.get(i);

        return movie;
    }

    /**
     *
     * @return Type
     */
    public static Type typeAnswerOf() {
        return new TypeToken<ResultsDiscoverMovies>() {
        }.getType();
    }
}
