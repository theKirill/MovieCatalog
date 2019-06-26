package com.yanyushkin.moviecatalog.di

import com.yanyushkin.moviecatalog.presenter.MoviesPresenter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PresenterModule {

    @Singleton
    @Provides
    fun provideMoviesPresenter(): MoviesPresenter = MoviesPresenter()
}