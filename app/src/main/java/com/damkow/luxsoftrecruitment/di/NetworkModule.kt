package com.damkow.luxsoftrecruitment.di

import com.damkow.luxsoftrecruitment.api.MovieService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideMovieService(): MovieService {
        return MovieService.create()
    }
}