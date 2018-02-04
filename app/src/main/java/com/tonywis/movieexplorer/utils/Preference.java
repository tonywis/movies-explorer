package com.tonywis.movieexplorer.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.tonywis.movieexplorer.models.contents.ResultsDiscoverMovies;

/**
 * Created by Tony on 03/02/2018.
 */

public class Preference {
    public static final String KEY_DISCOVER_MOVIES = "DISCOVER_MOVIES";

    private static SharedPreferences get(Context c) {
        return PreferenceManager.getDefaultSharedPreferences(c);
    }

    private static String getPref(Context c, String key) {
        return get(c).getString(key, null);
    }

    private static void setPref(Context c, String key, String value) {
        get(c).edit().putString(key, value).apply();
    }

    public static ResultsDiscoverMovies getDiscoverMovies(Context c) {
        String acc = getPref(c, Preference.KEY_DISCOVER_MOVIES);
        ResultsDiscoverMovies discoverMovies = null;
        if (acc != null) {
            Gson gson = new Gson();
            discoverMovies = gson.fromJson(acc, ResultsDiscoverMovies.class);
        }
        return discoverMovies;
    }

    public static void setDiscoverMovies(Context c, ResultsDiscoverMovies discoverMovies) {
        if (discoverMovies != null)
            setPref(c, KEY_DISCOVER_MOVIES, new Gson().toJson(discoverMovies));
        else
            setPref(c, KEY_DISCOVER_MOVIES, null);

    }

}
