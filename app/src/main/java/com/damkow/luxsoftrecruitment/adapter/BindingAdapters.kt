package com.damkow.luxsoftrecruitment.adapter

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.damkow.luxsoftrecruitment.R
import com.damkow.luxsoftrecruitment.util.UrlUtil

@BindingAdapter("movieImageFromUrl")
fun bindMovieImage(image: ImageView, imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        Glide.with(image.context)
            .load(UrlUtil.videoImageUrl(imageUrl))
            .into(image)
    }
}

@BindingAdapter("favouriteIcon")
fun setFavouriteImage(image: ImageView, isFavourite: Boolean) {
    val iconResId =
        if (isFavourite) R.drawable.ic_star_full else R.drawable.ic_star_border
    image.setImageResource(iconResId)
}

@BindingAdapter("votes")
fun setMovieVotes(textView: TextView, vote: Double) {
    textView.text = vote.toString()
}