package com.tonywis.movieexplorer.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.tonywis.movieexplorer.R
import com.tonywis.movieexplorer.models.contents.ResultsDiscoverMovies
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
        main_loading.visibility = View.VISIBLE

        APIHelper.getDiscoverMovies(applicationContext, object : TaskComplete<ResultsDiscoverMovies>() {
            override fun run() {
                var resultsDiscoverMovies = this.result
                if (resultsDiscoverMovies != null) {
                    Preference.setDiscoverMovies(applicationContext, resultsDiscoverMovies)
                    generateRecyclerView(resultsDiscoverMovies)
                }
                else {
                    resultsDiscoverMovies = Preference.getDiscoverMovies(applicationContext)
                    if (resultsDiscoverMovies != null)
                        generateRecyclerView(resultsDiscoverMovies)
                }
            }
        })
    }

    fun generateRecyclerView(resultsDiscoverMovies: ResultsDiscoverMovies) {
        main_loading.visibility = View.GONE
        var recyclerView : RecyclerView = findViewById(R.id.recycler_view)
        var numberColumns : Int = Utility.calculateNoOfColumns(applicationContext)

        recyclerView.layoutManager = GridLayoutManager(applicationContext, numberColumns)
        mGridListAdapter = GridListRecyclerAdapter(this@MainActivity, resultsDiscoverMovies.results)
        mGridListAdapter!!.setClickListener(this)

        recyclerView.adapter = mGridListAdapter
    }

    override fun onItemClick(view: View?, position: Int) {
        var m : Movie = mGridListAdapter!!.getItem(position)
        Log.d("Movie Selected", m.title)
        var intent = Intent(applicationContext, MovieDetailsActivity::class.java)
        intent.putExtra(MovieDetailsActivity.EXTRA_MOVIE_ID, m.id)

        var activityOptionsCompat : ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity)
        startActivity(intent, activityOptionsCompat.toBundle())
    }
}
