package com.example.moviecatalog

import android.app.Application
import com.example.moviecatalog.di.AppComponent
import com.example.moviecatalog.di.DaggerAppComponent
import com.example.moviecatalog.di.NetworkModule

class App : Application() {
    lateinit var component: AppComponent

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.builder().build()
    }

    fun getAppComponent(): AppComponent = component
}