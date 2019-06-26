package com.yanyushkin.moviecatalog

import android.app.Application
import com.yanyushkin.moviecatalog.di.AppComponent
import com.yanyushkin.moviecatalog.di.DaggerAppComponent

class App : Application() {
    lateinit var component: AppComponent

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.builder().build()
    }

    fun getAppComponent(): AppComponent = component
}