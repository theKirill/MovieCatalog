package com.yanyushkin.moviecatalog.extensions

import android.content.Context
import android.content.SharedPreferences

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
