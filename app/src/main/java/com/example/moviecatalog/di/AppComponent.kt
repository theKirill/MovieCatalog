package com.example.moviecatalog.di

import com.example.moviecatalog.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class])
interface AppComponent {

    fun injectsMainActivity(mainActivity: MainActivity)
}