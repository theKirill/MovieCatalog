package com.yanyushkin.moviecatalog.presenter

import com.yanyushkin.moviecatalog.App
import com.yanyushkin.moviecatalog.utils.LoadingListener
import com.yanyushkin.moviecatalog.domain.Movie
import com.yanyushkin.moviecatalog.model.MoviesModel
import com.yanyushkin.moviecatalog.network.Repository
import com.yanyushkin.moviecatalog.view.MainView
import java.util.ArrayList
import javax.inject.Inject

class MoviesPresenter : Presenter, LoadingListener {

    @Inject
    lateinit var repository: Repository
    private lateinit var mainView: MainView
    private var model: MoviesModel

    init {
        model = MoviesModel(this)

        /*get repository with dagger*/
        App.component.injectsMoviesPresenter(this)
    }

    override fun attach(mainView: MainView) {
        this.mainView = mainView
    }

    override fun detach() {

    }

    fun loadData() = getMovies(ScreenState.Loading)

    fun refreshData() = getMovies(ScreenState.Refreshing)

    fun searchData(query: String) = getNecessaryMovies(query)

    private fun getMovies(screenState: ScreenState) {
        showProgress(screenState)
        model.loadMovies(screenState)//get movies from model
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

    override fun onLoadingSuccess(screenState: ScreenState, movies: ArrayList<Movie>) {
        mainView.setMovies(movies)

        hideProgress(screenState)
    }

    override fun onLoadingSuccessEmpty(query: String) {
        mainView.showNothingFoundLayout(query)

        hideProgress(ScreenState.Searching)
    }

    override fun onLoadingError(screenState: ScreenState) {
        when (screenState) {
            ScreenState.Loading, ScreenState.Searching -> if (!mainView.hasContent()) mainView.showErrorLayout() else mainView.showNoInternetSnackbar()
            ScreenState.Refreshing -> mainView.hideRefreshing()
        }

        hideProgress(screenState)
    }
}