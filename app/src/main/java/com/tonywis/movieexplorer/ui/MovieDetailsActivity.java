package com.tonywis.movieexplorer.ui;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tonywis.movieexplorer.BuildConfig;
import com.tonywis.movieexplorer.R;
import com.tonywis.movieexplorer.models.contents.MovieDetails;
import com.tonywis.movieexplorer.models.contents.ResultsReleaseDates;
import com.tonywis.movieexplorer.models.contents.ResultsDiscoverMovies;
import com.tonywis.movieexplorer.models.contents.lists.Genre;
import com.tonywis.movieexplorer.models.contents.lists.Movie;
import com.tonywis.movieexplorer.models.contents.lists.ReleaseDatesGlobal;
import com.tonywis.movieexplorer.utils.FormatHelper;
import com.tonywis.movieexplorer.utils.Preference;
import com.tonywis.movieexplorer.utils.Utility;
import com.tonywis.movieexplorer.utils.requests.APIHelper;
import com.tonywis.movieexplorer.utils.requests.TaskComplete;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MovieDetailsActivity extends AppCompatActivity {

    public ProgressBar mLoadingMovieDetails;
    public static final String EXTRA_MOVIE_ID = BuildConfig.APPLICATION_ID+".EXTRA_MOVIE_ID";
    private Toolbar toolbar;
    private Movie movie = null;
    private ResultsDiscoverMovies resultsDiscoverMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        mLoadingMovieDetails = findViewById(R.id.movie_details_loading);
        mLoadingMovieDetails.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        int id = intent.getIntExtra(EXTRA_MOVIE_ID, -1);

        toolbar = findViewById(R.id.movie_details_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (id > -1) {
            resultsDiscoverMovies = Preference.getDiscoverMovies(getApplicationContext());
            if (resultsDiscoverMovies != null) {
                movie = resultsDiscoverMovies.getMovieById(id);
                if (movie != null)
                    refreshView(movie.movieDetails);
            }
            APIHelper.getMovieDetails(getApplicationContext(), true, id, new TaskComplete<MovieDetails>() {
                @Override
                public void run() {
                    final MovieDetails movieDetails = this.getResult();
                    if (movieDetails != null) {
                        movie.movieDetails = movieDetails;
                        Preference.setDiscoverMovies(getApplicationContext(), resultsDiscoverMovies);

                        APIHelper.getMovieReleaseDates(getApplicationContext(), movieDetails.id, new TaskComplete<ResultsReleaseDates>() {
                            @Override
                            public void run() {
                                ResultsReleaseDates resultsReleaseDates = this.getResult();
                                if (resultsReleaseDates != null) {
                                    List<ReleaseDatesGlobal> listReleaseDatesGlobal = resultsReleaseDates.results;
                                    for (ReleaseDatesGlobal releaseDatesGlobal : listReleaseDatesGlobal) {
                                        if (releaseDatesGlobal.iso_3166_1.compareTo(Locale.getDefault().getCountry()) == 0) {
                                            movieDetails.release_date = releaseDatesGlobal.release_dates.get(0).release_date;
                                            Preference.setDiscoverMovies(getApplicationContext(), resultsDiscoverMovies);
                                        }
                                    }
                                }
                                refreshView(movieDetails);
                            }
                        });
                    } else {
                        if (movie == null) {
                            refreshView(null);
                        }
                    }
                }
            });
        }
        else
            refreshView(null);
    }

    public void refreshView(MovieDetails movie) {
        mLoadingMovieDetails.setVisibility(View.GONE);
        if (movie != null) {
            findViewById(R.id.movie_details_error_message).setVisibility(View.GONE);
            toolbar.setTitle(movie.title);
            AppCompatImageView backdrop = findViewById(R.id.movie_details_backdrop);
            TextView title = findViewById(R.id.movie_details_title);
            TextView releaseDate = findViewById(R.id.movie_details_release_date);
            TextView genres = findViewById(R.id.movie_details_genres);
            TextView synopsis = findViewById(R.id.movie_details_synopsis);
            FloatingActionButton voteAverage = findViewById(R.id.movie_details_fab_vote_average);
            voteAverage.setImageBitmap(Utility.textAsBitmap(String.valueOf(movie.vote_average), Color.WHITE));

            Picasso.with(getApplicationContext())
                    .load(BuildConfig.URL_TMDB_IMAGES + movie.backdrop_path)
                    .error(R.drawable.ic_placeholder)
                    .into(backdrop);
            title.setText(movie.title);
            StringBuilder strGenre = new StringBuilder();
            for (Genre g : movie.genres) {
                if (strGenre.length() > 0)
                    strGenre.append(", ");
                strGenre.append(g.name);
            }
            genres.setText(strGenre);
            synopsis.setText(movie.overview);

            releaseDate.setText("");
            if (movie.release_date != null) {
                Calendar calReleaseDate = FormatHelper.formatStringToCal(getApplicationContext(), movie.release_date);
                if (calReleaseDate != null) {
                    String strReleaseDate = FormatHelper.formatCalDateFormatToString(getApplicationContext(), calReleaseDate, false);
                    if (strReleaseDate != null)
                        releaseDate.setText(strReleaseDate);
                }
            }
        }
        else
            findViewById(R.id.movie_details_error_message).setVisibility(View.VISIBLE);
    }
}
