package com.example.moviecatalog

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
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

        initRecyclerView()

        initSwipeRefreshListener()
        showProgress()
        getMovies()
    }

    private fun initSwipeRefreshListener() {
        layout_swipe.setColorSchemeResources(R.color.colorElectricBlue)
        layout_swipe.setOnRefreshListener {
            movies = ArrayList()
            page = 1
            // initData()
        }
    }

    private fun showProgress() {
        if (!layout_swipe.isRefreshing)
            layout_pb.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        layout_swipe.isRefreshing = false
        layout_pb.visibility = View.INVISIBLE
    }

    private fun showErrorLayout() {
        layout_error.visibility = View.VISIBLE
    }

    private fun initAdapter() {
        /*Create adapter with listener of click on element*/
        adapter = MoviesAdapter(movies, object : OnClickListener {
            override fun onCardViewClick(position: Int) {
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

                if (!isLoading /*&& isHasInternet*/) {
                    if ((visibleItemsCount + positionOfFirstVisibleItem) >= totalItemsCount) {
                        page++
                        getMovies()
                    }
                }
            }
        })
        hideProgress()
    }

    private fun getMovies() {
        repository.getMovies(object : ResponseCallback<MoviesResponse> {

            override fun onError() {
                hideProgress()
                showErrorLayout()

                showSnackBar(getString(R.string.errorSnack))
            }

            override fun onSuccess(apiResponse: MoviesResponse) {
                if (page == 1)
                    movies = ArrayList()

                apiResponse.movies.forEach {
                    movies.add(it.transform())
                }

                if (page == 1) {
                    adapter.setItems(movies)
                }
                else {
                    /*don`t move to top of the RV, because we add data to end*/
                    adapter.addItems()
                }

                //initAdapter()
                rv_movies.adapter = adapter

                hideProgress()

                progress_horizontal.visibility = View.VISIBLE
            }
        }, page)
    }

    private fun showSnackBar(message: String) {
        val sbError = MySnackBar(layout_main, message)
        sbError.show(this@MainActivity)
    }
}
