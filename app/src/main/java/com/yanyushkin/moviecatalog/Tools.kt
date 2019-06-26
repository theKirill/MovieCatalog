package com.yanyushkin.moviecatalog

import android.content.Context
import android.content.SharedPreferences
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

fun Context.getPreferences(): SharedPreferences = getSharedPreferences(APP_PREFERENCES_KEY, Context.MODE_PRIVATE)

fun SharedPreferences.check(id: Int) = contains(id.toString())

fun SharedPreferences.saveId(id: Int): Unit = edit().putInt(id.toString(), id).apply()

fun SharedPreferences.removeId(id: Int): Unit = edit().remove(id.toString()).apply()