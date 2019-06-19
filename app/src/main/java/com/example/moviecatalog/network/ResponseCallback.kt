package com.example.moviecatalog.network

interface ResponseCallback<R> {

    fun onEnd(apiResponse: R)
}