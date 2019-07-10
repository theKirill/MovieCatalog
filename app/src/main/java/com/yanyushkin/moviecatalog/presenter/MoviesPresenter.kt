package com.yanyushkin.moviecatalog.presenter

import com.yanyushkin.moviecatalog.domain.Movie
import com.yanyushkin.moviecatalog.model.MoviesModel
import com.yanyushkin.moviecatalog.view.MainView

class MoviesPresenter : Presenter, LoadingListener {

    private lateinit var mainView: MainView
    private lateinit var model: MoviesModel
    private var error = false

    init {
        if (!::model.isInitialized)
            model = MoviesModel(this)
    }

    /**
     * binding to view
     */
    override fun attach(mainView: MainView) {
        this.mainView = mainView
    }

    /**
     * successful load/refresh/search data
     */
    override fun onLoadingSuccess(screenState: ScreenState, movies: MutableList<Movie>) {
        hideProgress(screenState)
        error = false

        mainView.apply {
            if (screenState == ScreenState.Refreshing)
                clearSearchString()

            setMovies(movies)
        }
    }

    /**
     * empty search result
     */
    override fun onLoadingSuccessEmpty(query: String) {
        hideProgress(ScreenState.Searching)
        error = false

        model.clearMovies()

        mainView.apply {
            setMovies(mutableListOf())
            showNothingFoundLayout(query)
        }
    }

    /**
     * error load/refresh/search data
     */
    override fun onLoadingError(screenState: ScreenState) {
        hideProgress(screenState)
        error = false

        mainView.apply {
            when (screenState) {
                ScreenState.Loading, ScreenState.Searching -> {
                    if (model.getMovies().value == null || model.getMovies().value!!.size == 0) {
                        showErrorLayout()
                        error = true
                    } else {
                        showNoInternetSnackBar()
                    }
                }

                ScreenState.Refreshing -> {
                    showNoInternetSnackBar()
                }

                ScreenState.AdditionalLoading -> {
                    showUpdateButton()
                }
            }
        }
    }

    fun loadData(page: Int) {
        if (page == 1)
            getMovies(ScreenState.Loading, page)
        else
            getMovies(ScreenState.AdditionalLoading, page)
    }

    fun loadDataAfterRotationScreen(query: String): Unit = getMoviesAfterRotationScreen(query)

    fun refreshData(page: Int): Unit = getMovies(ScreenState.Refreshing, page)

    fun searchData(query: String): Unit = getNecessaryMovies(query)

    private fun getMovies(screenState: ScreenState, page: Int) {
        showProgress(screenState)

        model.loadMovies(screenState, page)//get movies from model
    }

    /**
     * get saved movies from model
     */
    private fun getMoviesAfterRotationScreen(query: String) {
        if (!error) {
            if (model.getMovies().value != null && model.getMovies().value!!.size > 0)
                mainView.setMovies(model.getMovies().value!!)
            else
                onLoadingSuccessEmpty(query)
        } else {
            onLoadingError(ScreenState.Loading)
        }
    }

    private fun getNecessaryMovies(query: String) {
        showProgress(ScreenState.Searching)

        model.searchMovies(query)//get movies from model
    }

    private fun showProgress(screenState: ScreenState) {
        mainView.apply {
            when (screenState) {
                ScreenState.Loading -> showLoading()
                ScreenState.Searching -> showSearchLoading()
                ScreenState.AdditionalLoading -> showAdditionalLoading()
                else -> return
            }
        }
    }

    private fun hideProgress(screenState: ScreenState) {
        mainView.apply {
            when (screenState) {
                ScreenState.Loading -> hideLoading()
                ScreenState.Searching -> hideSearchLoading()
                ScreenState.Refreshing -> hideRefreshing()
                ScreenState.AdditionalLoading -> hideAdditionalLoading()
            }
        }
    }
}