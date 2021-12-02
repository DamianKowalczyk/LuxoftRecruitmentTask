package com.damkow.luxsoftrecruitment

import com.damkow.luxsoftrecruitment.dto.Movie

interface OnMovieClickListener {
    fun onMovieClicked(item: Movie)

    fun onFavouriteClicked(item: Movie, position: Int)
}
