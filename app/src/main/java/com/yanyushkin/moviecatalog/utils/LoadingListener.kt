package com.yanyushkin.moviecatalog.utils

import com.yanyushkin.moviecatalog.domain.Movie
import com.yanyushkin.moviecatalog.presenter.ScreenState
import java.util.ArrayList

interface LoadingListener {

    fun onLoadingSuccess(screenState: ScreenState, movies: ArrayList<Movie>)
    fun onLoadingSuccessEmpty(query: String)
    fun onLoadingError(screenState: ScreenState)
}