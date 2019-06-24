package com.example.moviecatalog.activity

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import com.example.moviecatalog.App
import com.example.moviecatalog.R
import com.example.moviecatalog.adapters.MoviesAdapter
import com.example.moviecatalog.domain.Movie
import com.example.moviecatalog.network.MoviesResponse
import com.example.moviecatalog.network.Repository
import com.example.moviecatalog.network.ResponseCallback
import com.example.moviecatalog.utils.MySnackBar
import com.example.moviecatalog.utils.OnClickListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var repository: Repository
    private var movies: ArrayList<Movie> = ArrayList()
    private lateinit var adapter: MoviesAdapter
    private var page = 1
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*get repository with dagger*/
        (application as App).getAppComponent().injectsMainActivity(this)

        setSupportActionBar(toolbar)

        initSwipeRefreshListener()

        initRecyclerView()

        getMovies()

        et_search.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if ((event!!.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    hideKeyboard()

                    /*get data*/
                    page = 1

                    val query = et_search.text.toString()

                    if (query.isNotEmpty())
                        getNecessaryMovies(query)
                    else
                        getMovies()

                    return true
                }

                return false
            }
        })
    }

    private fun initSwipeRefreshListener() {
        layout_swipe.setColorSchemeResources(R.color.colorElectricBlue)
        layout_swipe.setOnRefreshListener {
            movies = ArrayList()
            page = 1
            // initData()
        }
    }

    private fun showMainProgress() {
        if (!layout_swipe.isRefreshing)
            layout_pb.visibility = View.VISIBLE
    }

    private fun hideMainProgress() {
        layout_swipe.isRefreshing = false
        layout_pb.visibility = View.GONE
    }

    private fun showSearchProgress() {
        progress_horizontal.visibility = View.VISIBLE
    }

    private fun hideSearchProgress() {
        progress_horizontal.visibility = View.GONE
    }

    private fun showErrorLayout() {
        layout_error.visibility = View.VISIBLE
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(et_search.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        rv_movies.requestFocus()
    }

    private fun showSnackBar(message: String) {
        val sbError = MySnackBar(layout_main, message)
        sbError.show(this@MainActivity)
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

        /*listener of the end of the list (add data, new request from the next page)*/
        rv_movies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val visibleItemsCount = layoutManagerForRV.childCount//how many elements on the screen
                val totalItemsCount = layoutManagerForRV.itemCount//how many elements total
                val positionOfFirstVisibleItem =
                    layoutManagerForRV.findFirstVisibleItemPosition()//position of the 1st element

                if (!isLoading && ((visibleItemsCount + positionOfFirstVisibleItem) >= totalItemsCount)) {
                    /* if () {
                         page++
                         //getMovies()
                     }*/
                }
            }
        })
        hideMainProgress()
    }

    private fun getMovies() {
        isLoading = true
        showMainProgress()

        repository.getMovies(object : ResponseCallback<MoviesResponse> {

            override fun onError() {
                hideMainProgress()
                showErrorLayout()

                showSnackBar(getString(R.string.errorSnack))
            }

            override fun onSuccess(apiResponse: MoviesResponse) {
                layout_nothing_found.visibility = View.GONE

                if (page == 1)
                    movies = ArrayList()

                apiResponse.movies.forEach {
                    movies.add(it.transform())
                }

                if (page == 1) {
                    adapter.setItems(movies)
                    rv_movies.adapter = adapter
                } else {
                    adapter.addItems()
                }

                hideMainProgress()
                isLoading = false
            }
        }, page)
    }

    private fun getNecessaryMovies(query: String) {
        isLoading = true
        showMainProgress()

        repository.getNecessaryMovies(object : ResponseCallback<MoviesResponse> {

            override fun onError() {
                hideMainProgress()
                showErrorLayout()

                showSnackBar(getString(R.string.errorSnack))
            }

            @SuppressLint("SetTextI18n")
            override fun onSuccess(apiResponse: MoviesResponse) {
                layout_nothing_found.visibility = View.GONE
                movies = ArrayList()

                if (apiResponse.movies.isEmpty()) {
                    layout_nothing_found.visibility = View.VISIBLE
                    tv_nothing_found.text = "По запросу \"$query\" ничего не найдено"
                } else {

                    apiResponse.movies.forEach {
                        movies.add(it.transform())
                    }

                    adapter.setItems(movies)

                    rv_movies.adapter = adapter
                    isLoading = false
                }

                hideMainProgress()
            }
        }, query)
    }
}
