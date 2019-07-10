package com.yanyushkin.moviecatalog.presenter

import com.yanyushkin.moviecatalog.domain.Movie

interface LoadingListener {

    fun onLoadingSuccess(screenState: ScreenState, movies: MutableList<Movie>)

    fun onLoadingSuccessEmpty(query: String)

    fun onLoadingError(screenState: ScreenState)
}