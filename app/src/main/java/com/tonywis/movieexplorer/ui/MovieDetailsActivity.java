package com.tonywis.movieexplorer.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;
import com.tonywis.movieexplorer.BuildConfig;
import com.tonywis.movieexplorer.R;
import com.tonywis.movieexplorer.models.contents.MovieDetails;
import com.tonywis.movieexplorer.models.contents.ResultsDiscoverMovies;
import com.tonywis.movieexplorer.models.contents.ResultsReleaseDates;
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

public class MovieDetailsActivity extends AppCompatActivity {

    public ProgressBar mLoadingMovieDetails;
    public static final String EXTRA_MOVIE_ID = BuildConfig.APPLICATION_ID+".EXTRA_MOVIE_ID";
    private Toolbar toolbar;
    private Movie movie = null;
    private ResultsDiscoverMovies resultsDiscoverMovies;
    private ShareActionProvider mShareActionProvider;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        mLoadingMovieDetails = findViewById(R.id.movie_details_loading);
        mLoadingMovieDetails.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        id = intent.getIntExtra(EXTRA_MOVIE_ID, -1);

        toolbar = findViewById(R.id.movie_details_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (id > -1) {
            // Try display temp datas
            resultsDiscoverMovies = Preference.getDiscoverMovies(getApplicationContext());
            if (resultsDiscoverMovies != null) {
                movie = resultsDiscoverMovies.getMovieById(id);
                if (movie != null) {
                    // Temp
                    if (movie.movieDetails == null) {
                        movie.movieDetails = new MovieDetails();
                        movie.movieDetails.id = movie.id;
                        movie.movieDetails.title = movie.title;
                        movie.movieDetails.overview = movie.overview;
                        movie.movieDetails.vote_average = movie.vote_average;
                        Preference.setDiscoverMovies(getApplicationContext(), resultsDiscoverMovies);
                    }
                    refreshView(movie.movieDetails);
                }
            }
            // get real datas
            APIHelper.getMovieDetails(getApplicationContext(), id, new TaskComplete<MovieDetails>() {
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
                                        if (releaseDatesGlobal.iso_3166_1.equalsIgnoreCase(Utility.getBasicCodeLanguage())) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movie_details, menu);
        MenuItem item = menu.findItem(R.id.menu_movie_details_item_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        setShareMovieIntent();
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        supportFinishAfterTransition();
        return true;
    }


    private void setShareMovieIntent() {
        if (mShareActionProvider != null) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT,
                    String.format(BuildConfig.URL_TMDB_WEBSITE, id, Utility.getFullCodeLanguage()));
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    /**
     *
     * @param movie
     */
    public void refreshView(MovieDetails movie) {
        mLoadingMovieDetails.setVisibility(View.GONE);
        if (movie != null) {
            findViewById(R.id.movie_details_error_message).setVisibility(View.GONE);
            AppCompatImageView backdrop = findViewById(R.id.movie_details_backdrop);
            AppCompatTextView title = findViewById(R.id.movie_details_title);
            AppCompatTextView releaseDate = findViewById(R.id.movie_details_release_date);
            AppCompatTextView genres = findViewById(R.id.movie_details_genres);
            AppCompatTextView synopsis = findViewById(R.id.movie_details_synopsis);
            FloatingActionButton voteAverage = findViewById(R.id.movie_details_fab_vote_average);

            toolbar.setTitle(movie.title);
            title.setText(movie.title);
            voteAverage.setImageBitmap(Utility.textAsBitmap(String.valueOf(movie.vote_average), Color.WHITE));
            synopsis.setText(movie.overview);
            Picasso.with(getApplicationContext())
                    .load(BuildConfig.URL_TMDB_IMAGES + movie.backdrop_path)
                    .error(R.drawable.ic_placeholder)
                    .into(backdrop);

            if (movie.genres != null) {
                StringBuilder strGenre = new StringBuilder();
                for (Genre g : movie.genres) {
                    if (strGenre.length() > 0)
                        strGenre.append(", ");
                    strGenre.append(g.name);
                }
                genres.setText(strGenre);
            }

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
