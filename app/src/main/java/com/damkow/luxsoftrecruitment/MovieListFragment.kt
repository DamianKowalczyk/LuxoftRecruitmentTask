package com.damkow.luxsoftrecruitment

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.*
import androidx.fragment.app.replace
import androidx.lifecycle.lifecycleScope
import com.damkow.luxsoftrecruitment.adapter.MovieAdapter
import com.damkow.luxsoftrecruitment.databinding.FragmentMovieListBinding
import com.damkow.luxsoftrecruitment.dto.Movie
import com.damkow.luxsoftrecruitment.viewmodels.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MovieListFragment : Fragment(), OnMovieClickListener {

    private val adapter = MovieAdapter(this)
    private val viewModel: MoviesViewModel by activityViewModels()
    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMovieListBinding.inflate(inflater, container, false)
        context ?: return binding.root

        binding.moviesList.adapter = adapter

        search()

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_movie_search, menu)

        val searchView = SearchView(requireContext())
        val searchAutoComplete =
            searchView.findViewById<SearchView.SearchAutoComplete>(R.id.search_src_text)

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line, arrayOf(
                "Venom", "No time to die", "Red Notice", "Eternals", "Free Guy"
            )
        )

        searchAutoComplete.setAdapter(adapter)

        searchAutoComplete.setOnItemClickListener { adapterView, _, index, _ ->
            val autocompleteItem = adapterView.getItemAtPosition(index) as String

            searchView.setQuery(autocompleteItem, false)
            search(autocompleteItem)
        }


        menu.findItem(R.id.action_search).apply {
            actionView = searchView
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                Log.d("TAG", "onQueryTextSubmit: ")
                search(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                Log.d("TAG", "onQueryTextChange: ")
                return false
            }
        })
    }

    private fun search(query: String? = null) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.searchMovies(query).collectLatest {
                adapter.submitData(it)
            }
        }
    }

    override fun onMovieClicked(item: Movie) {
        viewModel.selectedMovie(item)

        requireActivity().supportFragmentManager.commit {
            replace<MovieDetailsFragment>(R.id.fragment_container_view)
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }

    override fun onFavouriteClicked(item: Movie, position: Int) {
        val isFavourite = item.isFavourite

        if (isFavourite) {
            viewModel.removeMovieFromFavourite(item)
            item.isFavourite = false
        } else {
            viewModel.markMovieAsFavourite(item)
            item.isFavourite = true
        }

        adapter.refresh()
        adapter.notifyItemChanged(position)
    }
}