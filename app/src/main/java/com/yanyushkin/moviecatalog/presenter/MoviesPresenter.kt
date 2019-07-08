package com.yanyushkin.moviecatalog.presenter

import com.yanyushkin.moviecatalog.domain.Movie
import com.yanyushkin.moviecatalog.model.MoviesModel
import com.yanyushkin.moviecatalog.view.MainView
import java.util.ArrayList

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
    override fun onLoadingSuccess(screenState: ScreenState, movies: ArrayList<Movie>) {
        hideProgress(screenState)
        error = false

        if (screenState == ScreenState.Refreshing)
            mainView.clearSearchString()

        mainView.setMovies(movies)
    }

    /**
     * empty search result
     */
    override fun onLoadingSuccessEmpty(query: String) {
        hideProgress(ScreenState.Searching)
        error = false

        model.clearMovies()
        mainView.setMovies(ArrayList())

        mainView.showNothingFoundLayout(query)
    }

    /**
     * error load/refresh/search data
     */
    override fun onLoadingError(screenState: ScreenState) {
        hideProgress(screenState)
        error = false

        when (screenState) {
            ScreenState.Loading, ScreenState.Searching -> {
                if (model.getMovies().value == null || model.getMovies().value!!.size == 0) {
                    mainView.showErrorLayout()
                    error = true
                } else {
                    mainView.showNoInternetSnackBar()
                }
            }

            ScreenState.Refreshing -> {
                mainView.showNoInternetSnackBar()
            }
        }
    }

    fun loadData() = getMovies(ScreenState.Loading)

    fun loadDataAfterRotationScreen(query: String) = getMoviesAfterRotationScreen(query)

    fun refreshData() = getMovies(ScreenState.Refreshing)

    fun searchData(query: String) = getNecessaryMovies(query)

    private fun getMovies(screenState: ScreenState) {
        showProgress(screenState)

        model.loadMovies(screenState)//get movies from model
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
        when (screenState) {
            ScreenState.Loading -> mainView.showLoading()
            ScreenState.Searching -> mainView.showSearchLoading()
            else -> return
        }
    }

    private fun hideProgress(screenState: ScreenState) {
        when (screenState) {
            ScreenState.Loading -> mainView.hideLoading()
            ScreenState.Searching -> mainView.hideSearchLoading()
            ScreenState.Refreshing -> mainView.hideRefreshing()
        }
    }
}