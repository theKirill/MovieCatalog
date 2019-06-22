package com.example.moviecatalog.domain

class Movie(
    private val id: Int,
    private val title: String,
    private val posterURL: String,
    private val description: String,
    private val date: String
) {
    val getId: Int
        get() = id

    val getTitle: String
        get() = title

    val getPosterURL: String
        get() = posterURL

    val getDescription: String
        get() = description

    val getDate: String
        get() = date
}