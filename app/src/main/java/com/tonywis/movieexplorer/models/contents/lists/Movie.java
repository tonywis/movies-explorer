package com.tonywis.movieexplorer.models.contents.lists;

import com.tonywis.movieexplorer.models.contents.MovieDetails;

/**
 * Created by Tony on 03/02/2018.
 */

public class Movie {
    public int id;
    public String title;
    public String poster_path;
    public String overview;
    public float vote_average;

    public MovieDetails movieDetails;
}
