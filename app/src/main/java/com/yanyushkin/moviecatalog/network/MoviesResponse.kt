package com.yanyushkin.moviecatalog.network

import com.google.gson.annotations.SerializedName
import com.yanyushkin.moviecatalog.domain.Movie
import com.yanyushkin.moviecatalog.translateDate
import com.yanyushkin.moviecatalog.translatePosterURL

data class MoviesResponse(
    @SerializedName("results")
    val movies: List<MovieFromServer>
)
