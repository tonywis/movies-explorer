package com.tonywis.movieexplorer.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.tonywis.movieexplorer.R
import com.tonywis.movieexplorer.models.contents.ResultsUpcomingMovies
import com.tonywis.movieexplorer.models.contents.lists.Movie
import com.tonywis.movieexplorer.utils.Utility
import com.tonywis.movieexplorer.utils.requests.APIHelper
import com.tonywis.movieexplorer.utils.requests.TaskComplete
import com.tonywis.movieexplorer.ui.adapters.GridListRecyclerAdapter
import com.tonywis.movieexplorer.utils.Preference
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), GridListRecyclerAdapter.ItemClickListener {

    var mGridListAdapter : GridListRecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loading_main.visibility = View.VISIBLE

        APIHelper.getUpcomingMovies(applicationContext, object : TaskComplete<ResultsUpcomingMovies>() {
            override fun run() {
                var resultsUpcomingMovies = this.result
                if (resultsUpcomingMovies != null) {
                    Preference.setUpcomingMovies(applicationContext, resultsUpcomingMovies);
                    generateRecyclerView(resultsUpcomingMovies)
                }
                else {
                    resultsUpcomingMovies = Preference.getUpcomingMovies(applicationContext)
                    if (resultsUpcomingMovies != null)
                        generateRecyclerView(resultsUpcomingMovies)
                }
            }
        })
    }

    fun generateRecyclerView(resultsUpcomingMovies: ResultsUpcomingMovies) {
        loading_main.visibility = View.GONE
        var recyclerView : RecyclerView = findViewById(R.id.recycler_view)
        var numberColumns : Int = Utility.calculateNoOfColumns(applicationContext)

        recyclerView.layoutManager = GridLayoutManager(applicationContext, numberColumns)
        mGridListAdapter = GridListRecyclerAdapter(applicationContext, resultsUpcomingMovies.results)
        mGridListAdapter!!.setClickListener(this)

        recyclerView.adapter = mGridListAdapter
    }

    override fun onItemClick(view: View?, position: Int) {
        var m : Movie = mGridListAdapter!!.getItem(position)
        Log.d("Movie Selected", m.title)
        var intent = Intent(applicationContext, MovieDetailsActivity::class.java)
        intent.putExtra(MovieDetailsActivity.EXTRA_MOVIE_ID, m.id)
        startActivity(intent)
    }
}
