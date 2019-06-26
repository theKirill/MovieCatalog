package com.yanyushkin.moviecatalog.view

interface ContentView {

    /*show main progress bar*/
    fun showLoading()

    /**hide main progress bar*/
    fun hideLoading()

    /**hide swipe refresh layout*/
    fun hideRefreshing()

    /*show error layout*/
    fun showNoInternetSnackbar()
}