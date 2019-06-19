package com.example.moviecatalog

import java.util.*

fun translateDate(badDate: String): String {
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