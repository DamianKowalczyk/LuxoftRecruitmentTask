package com.damkow.luxsoftrecruitment.viewmodels

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.damkow.luxsoftrecruitment.R
import com.damkow.luxsoftrecruitment.data.MoviesRepository
import com.damkow.luxsoftrecruitment.dto.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val repository: MoviesRepository
) : ViewModel() {

    private val _movieToSeeDetails = MutableLiveData<Movie>()
    val selectedMovie: LiveData<Movie>
        get() = _movieToSeeDetails

    val selectedMovieFavIcon: LiveData<Int> =
        Transformations.map(selectedMovie) { movie ->
            if (movie.isFavourite) R.drawable.ic_star_full else R.drawable.ic_star_border
        }


    fun searchMovies(queryString: String?): Flow<PagingData<Movie>> {
        val databaseFlow = repository.getFavouriteMovieIds()

        val newResult: Flow<PagingData<Movie>> =
            repository.getSearchResultStreamWithQuery(queryString).cachedIn(viewModelScope)
                .combine(databaseFlow) { pagingData, favouriteMovies ->
                    pagingData.map { apiMovie ->
                        applySavedSelectionOnMovie(
                            apiMovie,
                            favouriteMovies
                        )
                    }
                }

        return newResult
    }

    private fun applySavedSelectionOnMovie(
        apiMovie: Movie,
        favouriteList: List<com.damkow.luxsoftrecruitment.data.FavouriteMovie>
    ): Movie {
        val isFavourite = favouriteList.any { it.movieId == apiMovie.id }
        apiMovie.isFavourite = isFavourite

        return apiMovie
    }

    fun selectedMovie(movie: Movie) {
        _movieToSeeDetails.value = movie
    }

    fun markMovieAsFavourite(movie: Movie) {
        viewModelScope.launch {
            repository.setMovieAsFavourite(movie.id)
        }
    }

    fun removeMovieFromFavourite(movie: Movie) {
        viewModelScope.launch {
            repository.removeMovieFromFavourites(movie.id)
        }
    }

    fun toggleFavouriteForSelectedMovie() {
        selectedMovie.value?.let {
            val toggled = !it.isFavourite

            if (toggled) {
                markMovieAsFavourite(it)
            } else {
                removeMovieFromFavourite(it)
            }

            _movieToSeeDetails.value =
                selectedMovie.value?.also { it1 -> it1.isFavourite = toggled }
        }
    }

}