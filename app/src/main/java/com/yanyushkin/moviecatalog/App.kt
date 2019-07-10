package com.yanyushkin.moviecatalog

import android.app.Application
import com.yanyushkin.moviecatalog.di.AppComponent
import com.yanyushkin.moviecatalog.di.DaggerAppComponent
import com.yanyushkin.moviecatalog.di.NetworkModule
import com.yanyushkin.moviecatalog.di.PresenterModule

class App : Application() {

    companion object {
        lateinit var component: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        component = DaggerAppComponent.builder()
            .networkModule(NetworkModule())
            .presenterModule(PresenterModule())
            .build()
    }
}