package com.damkow.luxsoftrecruitment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.damkow.luxsoftrecruitment.OnMovieClickListener
import com.damkow.luxsoftrecruitment.databinding.ListItemMovieBinding
import com.damkow.luxsoftrecruitment.dto.Movie

class MovieAdapter(private val clickListener: OnMovieClickListener) :
    PagingDataAdapter<Movie, MovieAdapter.MovieViewHolder>(MovieDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            clickListener,
            ListItemMovieBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        if (movie != null) {
            holder.bind(movie)
        }
    }

    class MovieViewHolder(
        clickListener: OnMovieClickListener,
        private val binding: ListItemMovieBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setClickListener {
                binding.movie?.let {
                    clickListener.onMovieClicked(it)
                }
            }

            binding.setFavClickListener {
                val absoluteAdapterPosition = this.absoluteAdapterPosition
                binding.movie?.let {
                    clickListener.onFavouriteClicked(it, absoluteAdapterPosition)
                }
            }
        }

        fun bind(item: Movie) {
            binding.apply {
                movie = item
                executePendingBindings()
            }
        }
    }
}

private class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }
}