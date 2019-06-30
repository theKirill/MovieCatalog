package com.yanyushkin.moviecatalog.view

import com.yanyushkin.moviecatalog.domain.Movie

interface MainView : ContentView, SearchingView {

    fun setMovies(movies: ArrayList<Movie>)

    fun showErrorLayout()
}