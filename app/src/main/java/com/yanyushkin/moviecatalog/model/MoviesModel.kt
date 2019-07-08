package com.yanyushkin.moviecatalog.model

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.yanyushkin.moviecatalog.App
import com.yanyushkin.moviecatalog.domain.Movie
import com.yanyushkin.moviecatalog.network.MoviesResponse
import com.yanyushkin.moviecatalog.network.Repository
import com.yanyushkin.moviecatalog.network.ResponseCallback
import com.yanyushkin.moviecatalog.presenter.MoviesPresenter
import com.yanyushkin.moviecatalog.presenter.ScreenState
import java.util.ArrayList
import javax.inject.Inject

class MoviesModel(private val presenter: MoviesPresenter) : Model, ViewModel() {

    @Inject
    lateinit var repository: Repository
    private lateinit var movies: MutableLiveData<ArrayList<Movie>>

    init {
        App.component.injectsMoviesModel(this)

        if (!::movies.isInitialized) {
            movies = MutableLiveData()
        }
    }

    override fun loadMovies(screenState: ScreenState, page: Int) {
        repository.getMovies(object : ResponseCallback<MoviesResponse> {

            override fun onError(): Unit = presenter.onLoadingError(screenState)

            override fun onSuccess(apiResponse: MoviesResponse) {
                val moviesFromServer = ArrayList<Movie>()

                apiResponse.movies.forEach {
                    moviesFromServer.add(it.transform())
                }

                if (page == 1)
                    movies.value = moviesFromServer
                else
                    moviesFromServer.forEach {
                        movies.value!!.add(it)
                    }

                presenter.onLoadingSuccess(screenState, movies.value!!)
            }
        }, page)
    }

    override fun searchMovies(query: String) {
        val screenState = ScreenState.Searching

        repository.getNecessaryMovies(object : ResponseCallback<MoviesResponse> {

            override fun onError(): Unit = presenter.onLoadingError(screenState)

            override fun onSuccess(apiResponse: MoviesResponse) {
                val moviesFromServer = ArrayList<Movie>()

                if (apiResponse.movies.isEmpty()) {
                    presenter.onLoadingSuccessEmpty(query)
                } else {
                    apiResponse.movies.forEach {
                        moviesFromServer.add(it.transform())
                    }

                    movies.value = moviesFromServer

                    presenter.onLoadingSuccess(screenState, movies.value!!)
                }
            }
        }, query)
    }

    fun getMovies(): LiveData<ArrayList<Movie>> = movies

    fun clearMovies() {
        movies = MutableLiveData()
    }
}