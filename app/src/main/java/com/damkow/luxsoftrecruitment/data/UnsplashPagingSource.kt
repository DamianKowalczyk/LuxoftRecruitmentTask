package com.damkow.luxsoftrecruitment.data

import androidx.paging.PagingSource
import com.damkow.luxsoftrecruitment.api.MovieService
import com.damkow.luxsoftrecruitment.dto.Movie

private const val STARTING_PAGE_INDEX = 1

class UnsplashPagingSource(
    private val service: MovieService,
    private val query: String?,
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: STARTING_PAGE_INDEX

        return try {
            val response =
                if (query == null) service.searchNowPlayingMovies(page) else service.searchMoviesWithQuery(
                    query, page
                )
            val movies = response.results
            LoadResult.Page(
                data = movies,
                prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (page == response.totalPages) null else page + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

}
