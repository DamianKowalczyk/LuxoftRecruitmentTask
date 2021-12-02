package com.damkow.luxsoftrecruitment.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.damkow.luxsoftrecruitment.api.MovieService
import com.damkow.luxsoftrecruitment.dto.Movie
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MoviesRepository @Inject constructor(
    private val service: MovieService,
    private val favouriteMovieDao: FavouriteMovieDao
) {

    suspend fun setMovieAsFavourite(movieId: Int) {
        favouriteMovieDao.insertFavourite(FavouriteMovie(movieId))
    }

    suspend fun removeMovieFromFavourites(movieId: Int) {
        favouriteMovieDao.removeFavourite(movieId)
    }

    fun getFavouriteMovieIds(): Flow<List<FavouriteMovie>> {
        return favouriteMovieDao.getFavourite()
    }

    fun getSearchResultStreamWithQuery(query: String?): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = { UnsplashPagingSource(service, query) }
        ).flow
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 30
    }
}