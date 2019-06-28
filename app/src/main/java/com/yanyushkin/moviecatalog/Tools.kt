package com.yanyushkin.moviecatalog

import android.content.Context
import android.content.SharedPreferences
import android.support.v4.content.ContextCompat.getSystemService
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.yanyushkin.moviecatalog.utils.MySnackBar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*

private const val APP_PREFERENCES_KEY = "settings"

fun translateDate(badDate: String): String {
    if (badDate.isEmpty())
        return ""

    var resDate = ""

    val calendar = Calendar.getInstance()

    calendar.set(
        Calendar.MONTH, Integer.parseInt(
            badDate.substring(
                5,
                7
            )
        ) - 1
    )

    resDate += badDate.substring(8) + " " + calendar.getDisplayName(
        Calendar.MONTH,
        Calendar.LONG_FORMAT,
        Locale("ru")
    ) + " " + badDate.substring(0, 4)

    return resDate
}

fun translatePosterURL(img: String?): String {

    img?.let { return "https://image.tmdb.org/t/p/w500/" + img.substring(1) }

    return ""
}

/*for work with shared preferences (check like of movie)*/
fun Context.getPreferences(): SharedPreferences = getSharedPreferences(APP_PREFERENCES_KEY, Context.MODE_PRIVATE)

fun SharedPreferences.hasLike(id: Int) = contains(id.toString())

fun SharedPreferences.saveId(id: Int): Unit = edit().putInt(id.toString(), id).apply()

fun SharedPreferences.removeId(id: Int): Unit = edit().remove(id.toString()).apply()

fun View.show() {
    visibility = View.VISIBLE
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