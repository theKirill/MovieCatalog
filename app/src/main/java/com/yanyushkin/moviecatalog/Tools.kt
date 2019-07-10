package com.yanyushkin.moviecatalog

import java.util.*

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

