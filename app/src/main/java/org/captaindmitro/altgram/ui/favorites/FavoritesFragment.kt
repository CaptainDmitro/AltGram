package org.captaindmitro.altgram.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import org.captaindmitro.altgram.adapters.CommentsAdapter
import org.captaindmitro.altgram.adapters.FavoritesAdapter
import org.captaindmitro.altgram.databinding.FragmentFavoritesBinding
import org.captaindmitro.altgram.ui.viewmodels.FavoritesViewModel
import org.captaindmitro.altgram.utils.UiState

class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding
    private val favoritesViewModel: FavoritesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val uiStateMessage = binding.favoritesUiStateMessage
        val rv = binding.favoritesRv
        val adapter = FavoritesAdapter(onUnsubscribe = { userId -> favoritesViewModel.unsubscribeFrom(userId) })
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(context)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                favoritesViewModel.updateSubscriptions()
                launch { favoritesViewModel.subscriptions.collect { uiState ->
                    when (uiState) {
                        is UiState.Empty -> { uiStateMessage.visibility = View.VISIBLE; uiStateMessage.text = "No subscriptions yet" }
                        is UiState.Loading -> { uiStateMessage.text = "Loading..."  }
                        is UiState.Error -> { uiStateMessage.text = "Error ${uiState.error}"  }
                        is UiState.Success -> {
                            uiStateMessage.visibility = View.GONE
                            adapter.submitDataSet(uiState.data)
                        }
                    }
                } }
            }
        }
    }
}