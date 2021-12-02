package com.damkow.luxsoftrecruitment.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_movies")
data class FavouriteMovie(
    @PrimaryKey @ColumnInfo(name = "id") val movieId: Int,
)