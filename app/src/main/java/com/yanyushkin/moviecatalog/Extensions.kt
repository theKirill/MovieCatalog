package com.yanyushkin.moviecatalog

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.yanyushkin.moviecatalog.utils.MySnackBar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*

private const val APP_PREFERENCES_KEY = "settings"

/**
 * for work with shared preferences
 */
fun Context.getPreferences(): SharedPreferences = getSharedPreferences(APP_PREFERENCES_KEY, Context.MODE_PRIVATE)

/**
 * check like of movie by id
 */
fun SharedPreferences.hasLike(id: Int) = contains(id.toString())

/**
 * save id of movie
 */
fun SharedPreferences.saveId(id: Int): Unit = edit().putInt(id.toString(), id).apply()

/**
 * remove id of movie
 */
fun SharedPreferences.removeId(id: Int): Unit = edit().remove(id.toString()).apply()

fun View.show() {
    visibility = View.VISIBLE
}

fun View.makeInvisible() {
    visibility = View.INVISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun AppCompatActivity.hideKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(et_search.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    rv_movies.requestFocus()
}

fun AppCompatActivity.showSnackBar(message: String){
    val sb = MySnackBar(layout_main, message)
    sb.show(this)
}