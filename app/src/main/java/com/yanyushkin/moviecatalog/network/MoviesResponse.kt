package com.yanyushkin.moviecatalog.network

import com.google.gson.annotations.SerializedName

data class MoviesResponse(
    @SerializedName("results")
    val movies: List<MovieObj>
)
