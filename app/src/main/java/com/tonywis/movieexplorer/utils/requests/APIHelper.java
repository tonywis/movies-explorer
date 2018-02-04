package com.tonywis.movieexplorer.utils.requests;

import android.content.Context;

import com.android.volley.Request;
import com.tonywis.movieexplorer.BuildConfig;
import com.tonywis.movieexplorer.models.contents.MovieDetails;
import com.tonywis.movieexplorer.models.contents.ResultsReleaseDates;
import com.tonywis.movieexplorer.models.contents.ResultsDiscoverMovies;
import com.tonywis.movieexplorer.utils.FormatHelper;
import com.tonywis.movieexplorer.utils.Utility;

import java.util.Calendar;

/**
 * Created by Tony on 03/02/2018.
 */

public class APIHelper {

    public static void getDiscoverMovies(Context c, TaskComplete<ResultsDiscoverMovies> taskcomplete) {
        APIRequest<ResultsDiscoverMovies> apiRequest = new APIRequest<>(c, ResultsDiscoverMovies.typeAnswerOf(), taskcomplete);
        //https://api.themoviedb.org/3/discover/movie?api_key=067d98083acb45e4b48466969f675cae&language=fr-FR&sort_by=popularity.desc&include_adult=false&include_video=false&page=1&release_date.gte=2018-02-04&with_release_type=3%7C2
        String url = BuildConfig.URL_TMDB_API+"/discover/movie?api_key="+BuildConfig.API_KEY_TMDB
                +"&language="+Utility.getCodeLanguage()
                +"&sort_by=popularity.desc&release_date.gte="+ FormatHelper.formatCalDateFormatToString(c, Calendar.getInstance(), true)
                +"&with_release_type=3%7C2page=1";
        apiRequest.setMethod(Request.Method.GET);
        apiRequest.execute(url);
    }

    public static void getMovieDetails(Context c, boolean showMissingInternetConnexion, int id, TaskComplete<MovieDetails> taskcomplete) {
        APIRequest<MovieDetails> apiRequest = new APIRequest<>(c, MovieDetails.typeAnswerOf(), taskcomplete);
        String url = BuildConfig.URL_TMDB_API+"/movie/"+id+"?api_key="+BuildConfig.API_KEY_TMDB+"&language="+ Utility.getCodeLanguage();
        apiRequest.setMethod(Request.Method.GET);
        apiRequest.setShowMissingInternetConnexion(showMissingInternetConnexion);
        apiRequest.execute(url);
    }

    public static void getMovieReleaseDates(Context c, int id, TaskComplete<ResultsReleaseDates> taskcomplete) {
        APIRequest<ResultsReleaseDates> apiRequest = new APIRequest<>(c, ResultsReleaseDates.typeAnswerOf(), taskcomplete);
        String url = BuildConfig.URL_TMDB_API+"/movie/"+id+"/release_dates?api_key="+BuildConfig.API_KEY_TMDB;
        apiRequest.setMethod(Request.Method.GET);
        apiRequest.execute(url);
    }
}
