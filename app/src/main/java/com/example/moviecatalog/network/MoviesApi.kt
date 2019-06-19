package com.example.moviecatalog.network

import retrofit2.Call
import retrofit2.http.GET

const val BASE_URL =
    "https://api.themoviedb.org/3/discover/"

interface MoviesApi {
    @GET("movie?api_key=6ccd72a2a8fc239b13f209408fc31c33&language=ru&region=ru&sort_by=release_date.desc&year=2019/")
    fun getMovies(): Call<MoviesResponse>
}