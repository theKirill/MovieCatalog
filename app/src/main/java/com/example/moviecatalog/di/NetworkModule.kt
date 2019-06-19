package com.example.moviecatalog.di

import com.example.moviecatalog.BuildConfig
import com.example.moviecatalog.network.BASE_URL
import com.example.moviecatalog.network.MoviesApi
import com.example.moviecatalog.network.Repository
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {
    @Singleton
    @Provides
    fun provideOkHttp(): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            okHttpClientBuilder.addInterceptor(interceptor)
        }
        return okHttpClientBuilder.build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()


    @Singleton
    @Provides
    fun provideEventsApi(retrofit: Retrofit): MoviesApi = retrofit.create(MoviesApi::class.java)


    @Singleton
    @Provides
    fun provideRepository(moviesApi: MoviesApi): Repository = Repository(moviesApi)
}