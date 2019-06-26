package com.yanyushkin.moviecatalog.activity

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.KeyEvent
import android.view.View
import android.widget.LinearLayout
import com.yanyushkin.moviecatalog.*
import com.yanyushkin.moviecatalog.adapter.MoviesAdapter
import com.yanyushkin.moviecatalog.domain.Movie
import com.yanyushkin.moviecatalog.network.Repository
import com.yanyushkin.moviecatalog.presenter.MoviesPresenter
import com.yanyushkin.moviecatalog.utils.OnClickListener
import com.yanyushkin.moviecatalog.view.MainView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainView {

    @Inject
    lateinit var repository: Repository
    @Inject
    lateinit var presenter: MoviesPresenter
    private var movies: ArrayList<Movie> = ArrayList()
    private lateinit var adapter: MoviesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*get repository and presenter with dagger*/
        App.component.injectsMainActivity(this)

        setSupportActionBar(toolbar)

        initSwipeRefreshListener()

        initRecyclerView()

        presenter.attach(this)
        presenter.loadData()

        et_search.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if ((event!!.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    hideKeyboard()

                    val query = et_search.text.toString()

                    if (query.isNotEmpty())
                        presenter.searchData(query)
                    else
                        presenter.loadData()

                    return true
                }

                return false
            }
        })

        button_update.setOnClickListener {
            val query = et_search.text.toString()

            if (query.isNotEmpty())
                presenter.searchData(query)
            else
                presenter.loadData()
        }
    }


    private fun initSwipeRefreshListener() {
        layout_swipe.setColorSchemeResources(R.color.colorElectricBlue)
        layout_swipe.setOnRefreshListener {
            movies = ArrayList()
            presenter.refreshData()
        }
    }

    private fun initAdapter() {
        /*Create adapter with listener of click on element*/
        adapter = MoviesAdapter(movies, object : OnClickListener {
            override fun onCardViewClick(position: Int) {
                hideKeyboard()

                showSnackBar(movies[position].getTitle)
            }
        })
    }

    private fun initRecyclerView() {
        val layoutManagerForRV = LinearLayoutManager(this)
        layoutManagerForRV.orientation = LinearLayout.VERTICAL
        rv_movies.layoutManager = layoutManagerForRV

        rv_movies.removeAllViews()

        rv_movies.apply {
            /*Set a adapter for rv*/
            initAdapter()
            rv_movies.adapter = adapter
        }
    }

    override fun hasContent(): Boolean = movies.size > 0

    override fun showLoading(): Unit = layout_pb.show()

    override fun hideLoading() {
        layout_pb.hide()
        container_data.show()
    }

    override fun hideRefreshing() {
        layout_swipe.isRefreshing = false
    }

    override fun setMovies(movies: ArrayList<Movie>) {
        hideLoading()
        layout_error.hide()
        this.movies = movies
        adapter.setItems(this.movies)
        rv_movies.adapter = adapter
    }

    override fun showNoInternetSnackbar(): Unit = showSnackBar(getString(R.string.errorSnack))

    override fun showErrorLayout() {
        layout_pb.hide()
        container_data.hide()
        layout_error.show()
    }

    override fun showSearchLoading(): Unit = progress_search.show()

    override fun hideSearchLoading(): Unit = progress_search.hide()

    @SuppressLint("SetTextI18n")
    override fun showNothingFoundLayout(query: String) {
        container_data.hide()
        layout_nothing_found.show()
        tv_nothing_found.text =
            "${getString(R.string.notFoundFirstPart)}\"$query\"${getString(R.string.notFoundSecondPart)}"
    }
}