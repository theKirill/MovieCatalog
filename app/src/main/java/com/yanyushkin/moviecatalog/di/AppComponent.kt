package com.yanyushkin.moviecatalog.di

import com.yanyushkin.moviecatalog.activity.MainActivity
import com.yanyushkin.moviecatalog.viewmodel.MoviesViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class])
interface AppComponent {

    fun injectsMainActivity(mainActivity: MainActivity)
    fun injectsMoviesViewModel(moviesViewModel: MoviesViewModel)
}