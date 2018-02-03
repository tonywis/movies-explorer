package com.tonywis.movieexplorer.ui;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.tonywis.movieexplorer.models.contents.lists.Genre;
import com.tonywis.movieexplorer.models.contents.lists.ReleaseDatesGlobal;
import com.tonywis.movieexplorer.utils.FormatHelper;
import com.tonywis.movieexplorer.utils.Utility;
import com.tonywis.movieexplorer.utils.requests.APIHelper;
import com.tonywis.movieexplorer.utils.requests.TaskComplete;

import java.util.Calendar;
import java.util.List;

public class MovieDetailsActivity extends AppCompatActivity {

    public ProgressBar mLoadingMovieDetails;
    public static final String EXTRA_MOVIE_ID = BuildConfig.APPLICATION_ID+".EXTRA_MOVIE_ID";
    private Toolbar toolbar;

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

        FloatingActionButton fab = findViewById(R.id.movie_details_fab_vote_average);

        if (id > -1) {
            APIHelper.getMovieDetails(getApplicationContext(), id, new TaskComplete<MovieDetails>() {
                @Override
                public void run() {
                    final MovieDetails movie = this.getResult();
                    if (movie != null) {
                        APIHelper.getMovieReleaseDates(getApplicationContext(), movie.id, new TaskComplete<ResultsReleaseDates>() {
                            @Override
                            public void run() {
                                ResultsReleaseDates resultsReleaseDates = this.getResult();
                                if (resultsReleaseDates != null) {
                                    List<ReleaseDatesGlobal> listReleaseDatesGlobal = resultsReleaseDates.results;
                                    for (ReleaseDatesGlobal releaseDatesGlobal : listReleaseDatesGlobal) {
                                        if (releaseDatesGlobal.iso_3166_1.compareTo("FR") == 0) {
                                            movie.release_date = releaseDatesGlobal.release_dates.get(0).release_date;
                                        }
                                    }
                                }
                                refreshView(movie);
                            }
                        });
                        //Preference.setUpcomingMovies(getApplicationContext(), movie);
                        //generateRecyclerView(movie)
                    } else {
                        //movie = Preference.getUpcomingMovies(get)
                        //if (resultsUpcomingMovies != null)
                        //generateRecyclerView(resultsUpcomingMovies)
                    }
                }
            });
        }
    }

    public void refreshView(MovieDetails movie) {
        mLoadingMovieDetails.setVisibility(View.GONE);
        toolbar.setTitle(movie.title);
        ImageView backdrop = findViewById(R.id.movie_details_backdrop);
        TextView title = findViewById(R.id.movie_details_title);
        TextView releaseDate = findViewById(R.id.movie_details_release_date);
        TextView genres = findViewById(R.id.movie_details_genres);
        TextView synopsis = findViewById(R.id.movie_details_synopsis);
        FloatingActionButton voteAverage = findViewById(R.id.movie_details_fab_vote_average);
        voteAverage.setImageBitmap(Utility.textAsBitmap(String.valueOf(movie.vote_average), Color.WHITE));

        Picasso.with(getApplicationContext())
                .load(BuildConfig.URL_TMDB_IMAGES+movie.backdrop_path)
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
                String strReleaseDate = FormatHelper.formatCalDateFormatToString(getApplicationContext(), calReleaseDate);
                if (strReleaseDate != null)
                    releaseDate.setText(strReleaseDate);
            }
        }
    }
}
