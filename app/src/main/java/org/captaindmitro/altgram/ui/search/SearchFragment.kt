package org.captaindmitro.altgram.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import org.captaindmitro.altgram.adapters.HomeAdapter
import org.captaindmitro.altgram.adapters.SearchAdapter
import org.captaindmitro.altgram.databinding.FragmentSearchBinding
import org.captaindmitro.altgram.ui.viewmodels.SearchViewModel
import org.captaindmitro.altgram.utils.UiState

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private val searchViewModel: SearchViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rv = binding.searchRv

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {

                searchViewModel.feed.collect { uiState ->
                    when (uiState) {
                        is UiState.Loading -> {
                            Log.i("Main", "Loading")
                        }
                        is UiState.Empty -> {
                            Log.i("Main", "Empty")
                        }
                        is UiState.Error -> {
                            Log.i("Main", "Error")
                        }
                        is UiState.Success -> {
                            rv.adapter = SearchAdapter(
                                uiState.data
                            )
                            rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                        }
                    }
                }

            }
        }


    }
}