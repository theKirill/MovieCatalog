package com.yanyushkin.moviecatalog.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.yanyushkin.moviecatalog.App
import com.yanyushkin.moviecatalog.domain.Movie
import com.yanyushkin.moviecatalog.network.MoviesResponse
import com.yanyushkin.moviecatalog.network.Repository
import com.yanyushkin.moviecatalog.network.ResponseCallback
import javax.inject.Inject

class MoviesViewModel(private val application: App) : ViewModel() {
    private lateinit var movies: MutableLiveData<ArrayList<Movie>>
    @Inject
    lateinit var repository: Repository

    init {
        if (!::movies.isInitialized) {
            movies = MutableLiveData()
            loadMoviesFromServer()
        }
    }

    fun getMovies(): LiveData<ArrayList<Movie>> = movies
    fun removeMovies(){onCleared()}

    private fun loadMoviesFromServer() {
        val moviesFromServer: ArrayList<Movie> = ArrayList()

        application.getAppComponent().injectsMoviesViewModel(this)

        /*make request (async)*/
        repository.getMovies(object : ResponseCallback<MoviesResponse> {

            override fun onError() {
                /*hideMainProgress()
                showErrorLayout()

                showSnackBar(Resources.getSystem().getString(R.string.errorSnack))*/
            }

            override fun onSuccess(apiResponse: MoviesResponse) {
                /*layout_nothing_found.visibility = View.GONE

                movies = ArrayList()
*/
                apiResponse.movies.forEach {
                    moviesFromServer.add(it.transform())
                }

              /* adapter.setItems(movies)
                rv_movies.adapter = adapter

                hideMainProgress()
                isLoading = false*/
            }
        })
    }

    override fun onCleared() {
        super.onCleared()

        movies.value!!.clear()
    }
}