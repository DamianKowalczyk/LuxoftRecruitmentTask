package com.damkow.luxsoftrecruitment.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteMovieDao {
    @Query("SELECT * FROM favourite_movies ORDER BY id")
    fun getFavourite(): Flow<List<FavouriteMovie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavourite(favouriteMovie: FavouriteMovie)

    @Query("DELETE FROM favourite_movies WHERE id = :movieId")
    suspend fun removeFavourite(movieId: Int)

}
