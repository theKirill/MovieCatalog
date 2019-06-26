package com.yanyushkin.moviecatalog.view

import com.yanyushkin.moviecatalog.domain.Movie

interface MainView : ContentView, SearchingView {

    fun hasContent(): Boolean

    fun setMovies(movies: ArrayList<Movie>)

    /*show error layout*/
    fun showErrorLayout()
}