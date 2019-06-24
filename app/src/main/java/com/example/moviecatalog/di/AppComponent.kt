package com.example.moviecatalog.di

import com.example.moviecatalog.activity.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class])
interface AppComponent {

    fun injectsMainActivity(mainActivity: MainActivity)
}