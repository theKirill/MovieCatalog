package com.yanyushkin.moviecatalog.presenter

import com.yanyushkin.moviecatalog.view.MainView

interface Presenter {

    fun attach(mainView: MainView)
    fun detach()
}