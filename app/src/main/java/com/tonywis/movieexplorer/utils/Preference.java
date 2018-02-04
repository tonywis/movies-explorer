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

    /**
     *
     * @param c
     * @return SharedPreferences
     */
    private static SharedPreferences get(Context c) {
        return PreferenceManager.getDefaultSharedPreferences(c);
    }

    /**
     *
     * @param context
     * @param key
     * @return String datas
     */
    private static String getPref(Context context, String key) {
        return get(context).getString(key, null);
    }

    /**
     *
     * @param context
     * @param key
     * @param value
     */
    private static void setPref(Context context, String key, String value) {
        get(context).edit().putString(key, value).apply();
    }

    /**
     *
     * @param context
     * @return ResultsDiscoverMovies
     */
    public static ResultsDiscoverMovies getDiscoverMovies(Context context) {
        String acc = getPref(context, Preference.KEY_DISCOVER_MOVIES);
        ResultsDiscoverMovies discoverMovies = null;
        if (acc != null) {
            Gson gson = new Gson();
            discoverMovies = gson.fromJson(acc, ResultsDiscoverMovies.class);
        }
        return discoverMovies;
    }

    /**
     *
     * @param context
     * @param discoverMovies
     */
    public static void setDiscoverMovies(Context context, ResultsDiscoverMovies discoverMovies) {
        if (discoverMovies != null)
            setPref(context, KEY_DISCOVER_MOVIES, new Gson().toJson(discoverMovies));
        else
            setPref(context, KEY_DISCOVER_MOVIES, null);

    }

}
