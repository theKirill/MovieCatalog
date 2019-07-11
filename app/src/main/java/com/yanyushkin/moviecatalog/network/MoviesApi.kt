package com.yanyushkin.moviecatalog.network

import com.yanyushkin.moviecatalog.BuildConfig
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_URL =
    "https://api.themoviedb.org/3/"

interface MoviesApi {

    @GET("discover/movie?api_key=" + BuildConfig.ApiKey + "&language=ru&region=ru&sort_by=release_date.desc&year=2019")
    fun getAllMovies(@Query("page") page: Int): Call<MoviesResponse>

    @GET("search/movie?api_key=" + BuildConfig.ApiKey + "&language=ru&region=ru")
    fun getNecessaryMovies(@Query("query") query: String): Call<MoviesResponse>
}