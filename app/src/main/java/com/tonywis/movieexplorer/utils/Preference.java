package com.tonywis.movieexplorer.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.tonywis.movieexplorer.models.contents.ResultsUpcomingMovies;

/**
 * Created by Tony on 03/02/2018.
 */

public class Preference {
    public static final String KEY_UPCOMING_MOVIES = "UPCOMING_MOVIES";

    private static SharedPreferences get(Context c) {
        return PreferenceManager.getDefaultSharedPreferences(c);
    }

    private static String getPref(Context c, String key) {
        return get(c).getString(key, null);
    }

    private static void setPref(Context c, String key, String value) {
        get(c).edit().putString(key, value).apply();
    }

    public static ResultsUpcomingMovies getUpcomingMovies(Context c) {
        String acc = getPref(c, Preference.KEY_UPCOMING_MOVIES);
        ResultsUpcomingMovies upcomingMovies = null;
        if (acc != null) {
            Gson gson = new Gson();
            upcomingMovies = gson.fromJson(acc, ResultsUpcomingMovies.class);
        }
        return upcomingMovies;
    }

    public static void setUpcomingMovies(Context c, ResultsUpcomingMovies upcomingMovies) {
        if (upcomingMovies != null)
            setPref(c, KEY_UPCOMING_MOVIES, new Gson().toJson(upcomingMovies));
        else
            setPref(c, KEY_UPCOMING_MOVIES, null);
    }

}
