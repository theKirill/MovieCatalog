package com.yanyushkin.moviecatalog.extensions

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager
import com.yanyushkin.moviecatalog.utils.MySnackBar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar_layout.*

fun AppCompatActivity.hideKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(search_et.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    movies_rv.requestFocus()
}

fun AppCompatActivity.showSnackBar(message: String) {
    val sb = MySnackBar(layout_main, message)
    sb.show(this)
}