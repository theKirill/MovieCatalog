package com.yanyushkin.moviecatalog.view

interface SearchingView {

    /**
     * show horizontal progress
     */
    fun showSearchLoading()

    /**
     * hide horizontal progress
     */
    fun hideSearchLoading()

    fun showNothingFoundLayout(query: String)
}