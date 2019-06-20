package com.example.moviecatalog.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(private val moviesApi: MoviesApi) {

    fun getMovies(responseCallback: ResponseCallback<MoviesResponse>) {

        /*async data acquisition*/
        moviesApi.getMovies().enqueue(object : Callback<MoviesResponse> {
            override fun onFailure(call: Call<MoviesResponse>, t: Throwable) {
            }

            override fun onResponse(call: Call<MoviesResponse>, response: Response<MoviesResponse>) {
                if (response.isSuccessful) {
                    val moviesResponse = response.body()

                    moviesResponse?.let { responseCallback.onEnd(moviesResponse) }//take the parsed data
                }
            }
        })
    }
}