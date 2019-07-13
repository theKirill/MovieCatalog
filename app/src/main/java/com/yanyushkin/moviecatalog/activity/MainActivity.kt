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
import com.yanyushkin.moviecatalog.extensions.*
import com.yanyushkin.moviecatalog.presenter.MoviesPresenter
import com.yanyushkin.moviecatalog.utils.OnClickListener
import com.yanyushkin.moviecatalog.view.MainView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainView {

    @Inject
    lateinit var presenter: MoviesPresenter
    private var movies: MutableList<Movie> = mutableListOf()
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
            search_et.setText(savedInstanceState.getString(TEXT_SEARCH_KEY))
            page = savedInstanceState.getInt(PAGE_KEY)

            /**
             * after screen rotation load saved movies by the ViewModel
             */
            if (savedInstanceState.containsKey(ROTATION_KEY) && savedInstanceState.getBoolean(ROTATION_KEY)) {
                rotationScreen = true

                positionOfFirstVisibleItem =
                    savedInstanceState.getInt(SCROLL_POSITION_KEY) //look where we stopped before the change of orientation

                presenter.loadDataAfterRotationScreen(search_et.text.toString())
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

                outState.apply {
                    clear()
                    putBoolean(ROTATION_KEY, rotationScreen)

                    val layoutManagerForRV = movies_rv.layoutManager as LinearLayoutManager
                    putInt(SCROLL_POSITION_KEY, layoutManagerForRV.findFirstVisibleItemPosition())
                }
            }
        }

        /**
         * remember text of search
         */
        outState?.let {

            outState.apply {
                putString(TEXT_SEARCH_KEY, search_et.text.toString())
                putInt(PAGE_KEY, page)
            }
        }
    }

    override fun showLoading() {
        layout_error.hide()
        layout_nothing_found.hide()
        layout_container_data.hide()
        layout_pb.show()
    }

    override fun hideLoading(): Unit = layout_pb.hide()

    override fun hideRefreshing() {
        layout_swipe.isRefreshing = false
    }

    override fun setMovies(movies: MutableList<Movie>) {
        isLoading = false
        this.movies = movies

        adapter.apply {
            setItems(movies)
            notifyDataSetChanged()
        }

        if (page == 1 || rotationScreen) {
            movies_rv.adapter = adapter
            movies_rv.scrollToPosition(positionOfFirstVisibleItem)

            rotationScreen = false
        }

        layout_container_data.show()
    }

    override fun showNoInternetSnackBar() {
        isLoading = false
        layout_container_data.show()
        showSnackBar(getString(R.string.error_message))
    }

    override fun showAdditionalLoading() {
        additional_update_btn.hide()
        additional_movies_pb.show()
    }

    override fun hideAdditionalLoading(): Unit = additional_movies_pb.hide()

    override fun showUpdateButton() {
        isLoading = false
        additional_update_btn.show()
    }

    override fun showErrorLayout() {
        isLoading = false
        layout_pb.hide()
        layout_nothing_found.hide()
        layout_container_data.hide()
        layout_error.show()
    }

    override fun showSearchLoading() {
        layout_error.hide()
        layout_nothing_found.hide()
        layout_pb.hide()
        layout_container_data.hide()
        search_pb.show()
    }

    override fun hideSearchLoading(): Unit = search_pb.makeInvisible()

    @SuppressLint("SetTextI18n")
    override fun showNothingFoundLayout(query: String) {
        isLoading = false
        layout_nothing_found.show()
        nothing_found_tv.text =
            "${getString(R.string.notFoundFirstPart_text)} \"$query\" ${getString(R.string.notFoundSecondPart_text)}"
    }

    override fun clearSearchString(): Unit = search_et.setText("")

    private fun initSwipeRefreshListener() {
        layout_swipe.apply {
            setColorSchemeResources(R.color.colorElectricBlue)

            setOnRefreshListener {
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
    }

    private fun initRecyclerView() {
        if (isLandscapeOrientation())
            setLayoutManagerForRV()

        movies_rv.apply {
            removeAllViews()
            /*Set a adapter for rv*/
            initAdapter()
            movies_rv.adapter = adapter
        }

        initScrollListenerForRV()
    }

    private fun isLandscapeOrientation(): Boolean =
        resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    private fun setLayoutManagerForRV() {
        val layoutManagerForRV = GridLayoutManager(this, 2)
        movies_rv.layoutManager = layoutManagerForRV
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

    private fun initScrollListenerForRV() {
        val layoutManagerForRV = movies_rv.layoutManager as LinearLayoutManager
        layoutManagerForRV.orientation = LinearLayout.VERTICAL

        movies_rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val visibleItemsCount = layoutManagerForRV.childCount//how many elements on the screen
                val totalItemsCount = layoutManagerForRV.itemCount//how many elements total
                positionOfFirstVisibleItem =
                    layoutManagerForRV.findFirstVisibleItemPosition()//position of the 1st element

                if (!isLoading && search_et.text.toString().isEmpty()) {
                    if ((visibleItemsCount + positionOfFirstVisibleItem) >= totalItemsCount) {
                        page++
                        isLoading = true
                        presenter.loadData(page)
                    } else {
                        additional_update_btn.hide()
                    }
                }
            }
        })
    }

    private fun loadOrSearchData() {
        val query = search_et.text.toString()
        isLoading = true

        if (query.isNotEmpty())
            presenter.searchData(query)
        else
            presenter.loadData(page)
    }

    private fun initKeyListenerOnKeyBoard() {
        search_et.setOnKeyListener(object : View.OnKeyListener {
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
        update_btn.setOnClickListener {
            positionOfFirstVisibleItem = 0
            page = 1

            loadOrSearchData()
        }

        additional_update_btn.setOnClickListener {
            presenter.loadData(page)
        }
    }
}