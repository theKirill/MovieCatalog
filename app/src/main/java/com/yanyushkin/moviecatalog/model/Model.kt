package com.yanyushkin.moviecatalog.model

import com.yanyushkin.moviecatalog.presenter.ScreenState

interface Model {

    fun loadMovies(screenState: ScreenState)
    fun searchMovies(query: String)
}