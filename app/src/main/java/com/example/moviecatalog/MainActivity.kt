package com.example.moviecatalog

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import com.example.moviecatalog.adapters.MoviesAdapter
import com.example.moviecatalog.models.Movie
import com.example.moviecatalog.network.MoviesApi
import com.example.moviecatalog.network.MoviesResponse
import com.example.moviecatalog.network.Repository
import com.example.moviecatalog.network.ResponseCallback
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var repository: Repository
    private var movies: ArrayList<Movie> = ArrayList()
    private lateinit var adapter: MoviesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*get repository with dagger*/
        (application as App).getAppComponent().injectsMainActivity(this)

        initRecyclerView()

        getMovies()
    }

    private fun initAdapter() {
        /*Create adapter with listener of click on element*/
        adapter = MoviesAdapter(movies)
    }

    private fun initRecyclerView() {
        val layoutManagerForRV = LinearLayoutManager(this)
        layoutManagerForRV.orientation = LinearLayout.VERTICAL
        rv_movies.layoutManager = layoutManagerForRV

        rv_movies.removeAllViews()

       /* rv_movies.apply {
            /*Set a adapter for rv*/
            initAdapter()
            rv_movies.adapter = adapter
        }*/
    }

    private fun getMovies() {
        repository.getMovies(object : ResponseCallback<MoviesResponse> {
            override fun onEnd(apiResponse: MoviesResponse) {

                apiResponse.movies.forEach {
                    movies.add(it.transform())
                }

                initAdapter()
                rv_movies.adapter = adapter
            }
        })
    }
}
