package com.example.moviecatalog.network

interface ResponseCallback<R> {

    fun onSuccess(apiResponse: R)
    fun onError()
}