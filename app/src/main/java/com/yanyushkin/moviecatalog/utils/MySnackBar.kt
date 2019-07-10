package com.yanyushkin.moviecatalog.utils

import android.content.Context
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.TextView
import com.yanyushkin.moviecatalog.R

class MySnackBar(v: View, message:String) {

    private val sb: Snackbar
    private val snackbarView: View

    init {
        sb = Snackbar.make(v, message, Snackbar.LENGTH_LONG)
        snackbarView = sb.view
    }

    fun show(context: Context) {
        snackbarView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorBackSnackBar))

        val snackTextView = snackbarView.findViewById<TextView>(android.support.design.R.id.snackbar_text)

        snackTextView.apply {
            setTextColor(ContextCompat.getColor(context, R.color.colorWhite))
            textSize = 12.0f
        }

        sb.apply {
            duration = 5000
            show()
        }
    }
}