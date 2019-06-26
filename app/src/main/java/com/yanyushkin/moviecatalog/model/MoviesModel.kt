package com.yanyushkin.moviecatalog.model

import com.yanyushkin.moviecatalog.App
import com.yanyushkin.moviecatalog.domain.Movie
import com.yanyushkin.moviecatalog.network.MoviesResponse
import com.yanyushkin.moviecatalog.network.Repository
import com.yanyushkin.moviecatalog.network.ResponseCallback
import com.yanyushkin.moviecatalog.presenter.MoviesPresenter
import com.yanyushkin.moviecatalog.presenter.ScreenState
import java.util.ArrayList
import javax.inject.Inject

class MoviesModel(private val presenter: MoviesPresenter) : Model {

    @Inject
    lateinit var repository: Repository

    init {
        App.component.injectsMoviesModel(this)
    }

    override fun loadMovies(screenState: ScreenState) {
        repository.getMovies(object : ResponseCallback<MoviesResponse> {

            override fun onError() = presenter.onLoadingError(screenState)

            override fun onSuccess(apiResponse: MoviesResponse) {
                val movies = ArrayList<Movie>()

                apiResponse.movies.forEach {
                    movies.add(it.transform())
                }

                presenter.onLoadingSuccess(screenState, movies)
            }
        })
    }

    override fun searchMovies(query: String) {
        val screenState = ScreenState.Searching

        repository.getNecessaryMovies(object : ResponseCallback<MoviesResponse> {

            override fun onError() = presenter.onLoadingError(screenState)


            override fun onSuccess(apiResponse: MoviesResponse) {
                val movies = ArrayList<Movie>()

                if (apiResponse.movies.isEmpty()) {
                    presenter.onLoadingSuccessEmpty(query)
                } else {
                    apiResponse.movies.forEach {
                        movies.add(it.transform())
                    }
                }

                presenter.onLoadingSuccess(screenState, movies)
            }
        }, query)
    }
}