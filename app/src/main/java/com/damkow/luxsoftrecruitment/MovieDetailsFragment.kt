package com.damkow.luxsoftrecruitment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.damkow.luxsoftrecruitment.databinding.FragmentMovieDetailsBinding
import com.damkow.luxsoftrecruitment.viewmodels.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    private lateinit var binding: FragmentMovieDetailsBinding
    private val moviesViewModel: MoviesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        context ?: return binding.root

        binding.apply {
            viewModel = moviesViewModel

            toolbar.setNavigationOnClickListener {
                onBackArrowClicked()
            }

            toolbar.setOnMenuItemClickListener { item ->
                Log.d("TAG", "onMenuItemClicked")
                onMenuItemClicked(item)
            }
        }

        setHasOptionsMenu(true)

        moviesViewModel.selectedMovieFavIcon.observe(viewLifecycleOwner) { favIconResId ->
            setFavouriteButtonIcon(favIconResId)
        }

        return binding.root
    }


    private fun onBackArrowClicked() {
        requireActivity().onBackPressed()
    }

    private fun onMenuItemClicked(item: MenuItem) = when (item.itemId) {
        R.id.action_favourite -> {
            onMarkAsFavouriteButtonClicked()
            true
        }
        else -> false
    }

    private fun onMarkAsFavouriteButtonClicked() {
        moviesViewModel.toggleFavouriteForSelectedMovie()
    }

    private fun setFavouriteButtonIcon(favIconResId: Int) {
        binding.toolbar.menu.findItem(R.id.action_favourite).icon =
            getDrawable(requireContext(), favIconResId)
    }

}