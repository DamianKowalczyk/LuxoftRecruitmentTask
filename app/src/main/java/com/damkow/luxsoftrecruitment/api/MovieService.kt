package com.damkow.luxsoftrecruitment.api

import com.damkow.luxsoftrecruitment.BuildConfig
import com.damkow.luxsoftrecruitment.dto.MoviesResult
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {

    @GET("movie/now_playing")
    suspend fun searchNowPlayingMovies(
        @Query("page") page: Int,
    ): MoviesResult

    @GET("search/movie")
    suspend fun searchMoviesWithQuery(
        @Query("query") query: String,
        @Query("page") page: Int,
    ): MoviesResult

    companion object {
        fun create(): MovieService {
            val logger = HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) Level.BODY else Level.NONE
            }

            val authInterceptor = OAuthInterceptor("Bearer", BuildConfig.TOKEN)

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .addInterceptor(authInterceptor)
                .build()

            return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MovieService::class.java)
        }
    }
}