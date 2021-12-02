package com.damkow.luxsoftrecruitment.di

import android.content.Context
import com.damkow.luxsoftrecruitment.data.AppDatabase
import com.damkow.luxsoftrecruitment.data.FavouriteMovieDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideMovieDao(appDatabase: AppDatabase): FavouriteMovieDao {
        return appDatabase.movieDao()
    }

}