package com.yanyushkin.moviecatalog.activity

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.KeyEvent
import android.view.View
import android.widget.LinearLayout
import com.yanyushkin.moviecatalog.*
import com.yanyushkin.moviecatalog.adapter.MoviesAdapter
import com.yanyushkin.moviecatalog.domain.Movie
import com.yanyushkin.moviecatalog.presenter.MoviesPresenter
import com.yanyushkin.moviecatalog.utils.OnClickListener
import com.yanyushkin.moviecatalog.view.MainView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainView {

    @Inject
    lateinit var presenter: MoviesPresenter
    private var movies: ArrayList<Movie> = ArrayList()
    private lateinit var adapter: MoviesAdapter
    private var rotationScreen = false
    private val ROTATION_KEY = "rotation"
    private val SCROLL_POSITION_KEY = "position"
    private var positionOfFirstVisibleItem = 0
    private var TEXT_SEARCH_KEY = "text"
    private var PAGE_KEY = "page"
    private var page = 1
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        App.component.injectsMainActivity(this)//get presenter with dagger

        setSupportActionBar(toolbar)

        initSwipeRefreshListener()

        initRecyclerView()

        presenter.attach(this)

        if (savedInstanceState != null) {
            et_search.setText(savedInstanceState.getString(TEXT_SEARCH_KEY))
            page = savedInstanceState.getInt(PAGE_KEY)

            /**
             * after screen rotation load saved movies by the ViewModel
             */
            if (savedInstanceState.containsKey(ROTATION_KEY) && savedInstanceState.getBoolean(ROTATION_KEY)) {
                rotationScreen = true

                positionOfFirstVisibleItem =
                    savedInstanceState.getInt(SCROLL_POSITION_KEY) //look where we stopped before the change of orientation

                presenter.loadDataAfterRotationScreen(et_search.text.toString())
            } else {
                loadOrSearchData()
            }
        } else {
            loadOrSearchData()
        }

        initKeyListenerOnKeyBoard()

        initButtonsUpdateClickListener()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        if (movies.size > 0) {
            rotationScreen = true

            /**
             * remember the screen rotation
             */
            outState?.let {
                outState.clear()
                outState.putBoolean(ROTATION_KEY, rotationScreen)

                val layoutManagerForRV = rv_movies.layoutManager as LinearLayoutManager
                outState.putInt(SCROLL_POSITION_KEY, layoutManagerForRV.findFirstVisibleItemPosition())
            }
        }

        /**
         * remember text of search
         */
        outState?.let {
            outState.putString(TEXT_SEARCH_KEY, et_search.text.toString())
            outState.putInt(PAGE_KEY, page)
        }
    }

    override fun showLoading() {
        layout_error.hide()
        layout_nothing_found.hide()
        container_data.hide()
        layout_pb.show()
    }

    override fun hideLoading(): Unit = layout_pb.hide()

    override fun hideRefreshing() {
        layout_swipe.isRefreshing = false
    }

    override fun setMovies(movies: ArrayList<Movie>) {
        isLoading = false
        this.movies = movies
        adapter.setItems(this.movies)
        adapter.notifyDataSetChanged()

        if (page == 1 || rotationScreen) {
            rv_movies.adapter = adapter
            rv_movies.scrollToPosition(positionOfFirstVisibleItem)

            rotationScreen = false
        }

        container_data.show()
    }

    override fun showNoInternetSnackBar() {
        isLoading = false
        container_data.show()
        showSnackBar(getString(R.string.errorSnack))
    }

    override fun showAdditionalLoading() {
        button_additional_update.hide()
        progress_additional_movies.show()
    }

    override fun hideAdditionalLoading(): Unit = progress_additional_movies.hide()

    override fun showUpdateButton() {
        isLoading = false
        button_additional_update.show()
    }

    override fun showErrorLayout() {
        isLoading = false
        layout_pb.hide()
        layout_nothing_found.hide()
        container_data.hide()
        layout_error.show()
    }

    override fun showSearchLoading() {
        layout_error.hide()
        layout_nothing_found.hide()
        layout_pb.hide()
        container_data.hide()
        progress_search.show()
    }

    override fun hideSearchLoading(): Unit = progress_search.makeInvisible()

    @SuppressLint("SetTextI18n")
    override fun showNothingFoundLayout(query: String) {
        isLoading = false
        layout_nothing_found.show()
        tv_nothing_found.text =
            "${getString(R.string.notFoundFirstPart)} \"$query\" ${getString(R.string.notFoundSecondPart)}"
    }

    override fun clearSearchString(): Unit = et_search.setText("")

    private fun initSwipeRefreshListener() {
        layout_swipe.setColorSchemeResources(R.color.colorElectricBlue)

        layout_swipe.setOnRefreshListener {
            positionOfFirstVisibleItem = 0
            page = 1

            if (layout_nothing_found.visibility != View.VISIBLE && layout_error.visibility != View.VISIBLE) {
                isLoading = true
                presenter.refreshData(page)
            } else {
                hideRefreshing()
            }
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

    private fun isLandscapeOrientation(): Boolean =
        resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    private fun setLayoutManagerForRV() {
        val layoutManagerForRV = GridLayoutManager(this, 2)
        rv_movies.layoutManager = layoutManagerForRV
    }

    private fun initScrollListenerForRV() {
        val layoutManagerForRV = rv_movies.layoutManager as LinearLayoutManager
        layoutManagerForRV.orientation = LinearLayout.VERTICAL

        rv_movies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val visibleItemsCount = layoutManagerForRV.childCount//how many elements on the screen
                val totalItemsCount = layoutManagerForRV.itemCount//how many elements total
                positionOfFirstVisibleItem =
                    layoutManagerForRV.findFirstVisibleItemPosition()//position of the 1st element

                if (!isLoading && et_search.text.toString().isEmpty()) {
                    if ((visibleItemsCount + positionOfFirstVisibleItem) >= totalItemsCount) {
                        page++
                        isLoading = true
                        presenter.loadData(page)
                    } else {
                        button_additional_update.hide()
                    }
                }
            }
        })
    }

    private fun initRecyclerView() {
        if (isLandscapeOrientation())
            setLayoutManagerForRV()

        rv_movies.removeAllViews()

        rv_movies.apply {
            /*Set a adapter for rv*/
            initAdapter()
            rv_movies.adapter = adapter
        }

        initScrollListenerForRV()
    }

    private fun loadOrSearchData() {
        val query = et_search.text.toString()
        isLoading = true

        if (query.isNotEmpty())
            presenter.searchData(query)
        else
            presenter.loadData(page)
    }

    private fun initKeyListenerOnKeyBoard() {
        et_search.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                event?.let {
                    if ((event.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_BACK)) {

                        hideKeyboard()

                        positionOfFirstVisibleItem = 0
                        page = 1

                        loadOrSearchData()

                        return true
                    }
                }
                return false
            }
        })
    }

    private fun initButtonsUpdateClickListener() {
        button_update.setOnClickListener {
            positionOfFirstVisibleItem = 0
            page = 1

            loadOrSearchData()
        }

        button_additional_update.setOnClickListener {
            presenter.loadData(page)
        }
    }
}