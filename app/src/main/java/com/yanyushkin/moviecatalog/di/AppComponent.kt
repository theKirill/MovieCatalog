package com.yanyushkin.moviecatalog.di

import com.yanyushkin.moviecatalog.activity.MainActivity
import com.yanyushkin.moviecatalog.model.MoviesModel
import com.yanyushkin.moviecatalog.presenter.MoviesPresenter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, PresenterModule::class])
interface AppComponent {

    fun injectsMainActivity(mainActivity: MainActivity)

    fun injectsMoviesModel(moviesModel: MoviesModel)
}