package com.tonywis.movieexplorer.utils.requests;

import android.content.Context;

import com.android.volley.Request;
import com.tonywis.movieexplorer.BuildConfig;
import com.tonywis.movieexplorer.models.contents.MovieDetails;
import com.tonywis.movieexplorer.models.contents.ResultsReleaseDates;
import com.tonywis.movieexplorer.models.contents.ResultsUpcomingMovies;
import com.tonywis.movieexplorer.utils.Utility;

/**
 * Created by Tony on 03/02/2018.
 */

public class APIHelper {

    public static void getUpcomingMovies(Context c, TaskComplete<ResultsUpcomingMovies> taskcomplete) {
        APIRequest<ResultsUpcomingMovies> apiRequest = new APIRequest<>(c, ResultsUpcomingMovies.typeAnswerOf(), taskcomplete);
        String url = BuildConfig.URL_TMDB_API+"/movie/upcoming?api_key="+BuildConfig.API_KEY_TMDB+"&language="+Utility.getCodeLanguage()+"&page=1";
        apiRequest.setMethod(Request.Method.GET);
        apiRequest.execute(url);
    }

    public static void getMovieDetails(Context c, int id, TaskComplete<MovieDetails> taskcomplete) {
        APIRequest<MovieDetails> apiRequest = new APIRequest<>(c, MovieDetails.typeAnswerOf(), taskcomplete);
        String url = BuildConfig.URL_TMDB_API+"/movie/"+id+"?api_key="+BuildConfig.API_KEY_TMDB+"&language="+ Utility.getCodeLanguage();
        apiRequest.setMethod(Request.Method.GET);
        apiRequest.execute(url);
    }

    public static void getMovieReleaseDates(Context c, int id, TaskComplete<ResultsReleaseDates> taskcomplete) {
        APIRequest<ResultsReleaseDates> apiRequest = new APIRequest<>(c, ResultsReleaseDates.typeAnswerOf(), taskcomplete);
        String url = BuildConfig.URL_TMDB_API+"/movie/"+id+"/release_dates?api_key="+BuildConfig.API_KEY_TMDB;
        apiRequest.setMethod(Request.Method.GET);
        apiRequest.execute(url);
    }
}
