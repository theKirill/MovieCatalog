package com.yanyushkin.moviecatalog.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(private val moviesApi: MoviesApi) {

    fun getMovies(responseCallback: ResponseCallback<MoviesResponse>) {

        /*async data acquisition*/
        moviesApi.getAllMovies().enqueue(object : Callback<MoviesResponse> {
            override fun onFailure(call: Call<MoviesResponse>, t: Throwable): Unit = responseCallback.onError()

            override fun onResponse(call: Call<MoviesResponse>, response: Response<MoviesResponse>) {
                if (response.isSuccessful) {
                    val moviesResponse = response.body()

                    moviesResponse?.let { responseCallback.onSuccess(moviesResponse) }//take the parsed data
                } else {
                    responseCallback.onError()
                }
            }
        })
    }

    fun getNecessaryMovies(responseCallback: ResponseCallback<MoviesResponse>, query: String) {

        moviesApi.getNecessaryMovies(query).enqueue(object : Callback<MoviesResponse> {
            override fun onFailure(call: Call<MoviesResponse>, t: Throwable): Unit = responseCallback.onError()

            override fun onResponse(call: Call<MoviesResponse>, response: Response<MoviesResponse>) {
                if (response.isSuccessful) {
                    val moviesResponse = response.body()

                    moviesResponse?.let { responseCallback.onSuccess(moviesResponse) }//take the parsed data
                } else {
                    responseCallback.onError()
                }
            }
        })
    }
}