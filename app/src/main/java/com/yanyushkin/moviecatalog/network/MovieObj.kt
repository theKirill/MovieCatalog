package com.yanyushkin.moviecatalog.network

import com.google.gson.annotations.SerializedName
import com.yanyushkin.moviecatalog.domain.Movie
import com.yanyushkin.moviecatalog.translateDate
import com.yanyushkin.moviecatalog.translatePosterURL

data class MovieObj(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("poster_path")
    val posterURL: String,
    @SerializedName("overview")
    val description: String,
    @SerializedName("release_date")
    val date: String
) {
    fun transform(): Movie {

        return Movie(
            this.id,
            this.title,
            translatePosterURL(this.posterURL),
            this.description,
            translateDate(this.date)
        )
    }
}