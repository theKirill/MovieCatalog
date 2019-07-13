package com.yanyushkin.moviecatalog.extensions

import android.view.View

fun View.show() {
    visibility = View.VISIBLE
}

fun View.makeInvisible() {
    visibility = View.INVISIBLE
}

fun View.hide() {
    visibility = View.GONE
}