package com.example.moviecatalog.network

import com.example.moviecatalog.domain.Movie
import com.example.moviecatalog.translateDate
import com.example.moviecatalog.translatePosterURL
import com.google.gson.annotations.SerializedName

data class MoviesResponse(
    @SerializedName("results")
    val movies: List<MovieFromServer>
)

data class MovieFromServer(
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