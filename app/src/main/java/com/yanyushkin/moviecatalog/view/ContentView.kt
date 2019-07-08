package com.yanyushkin.moviecatalog.view

interface ContentView {

    /**
     * show main progress bar
     */
    fun showLoading()

    /**
     * hide main progress bar
     */
    fun hideLoading()

    /**
     * hide swipe refresh layout
     */
    fun hideRefreshing()

    fun showNoInternetSnackBar()

    /**
     * show additional progress bar
     */
    fun showAdditionalLoading()

    /**
     * hide additional progress bar
     */
    fun hideAdditionalLoading()

    /**
     * show update button
     */
    fun showUpdateButton()
}