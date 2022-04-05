package org.captaindmitro.altgram.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import org.captaindmitro.altgram.adapters.HomeAdapter
import org.captaindmitro.altgram.adapters.PostsAdapter
import org.captaindmitro.altgram.databinding.FragmentHomeBinding
import org.captaindmitro.altgram.ui.viewmodels.HomeViewModel
import org.captaindmitro.altgram.ui.viewmodels.LoginViewModel
import org.captaindmitro.altgram.utils.UiState

class HomeFragment : Fragment() {
    private val loginViewModel: LoginViewModel by activityViewModels()
    private val homeViewModel: HomeViewModel by activityViewModels()

    private lateinit var binding: FragmentHomeBinding
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                launch {
                    loginViewModel.currentUser.collect { username ->
                        if (username == null) {
                            val action = HomeFragmentDirections.actionHomeFragmentToLoginFragment()
                            findNavController().navigate(action)
                        }
                    }
                }
                launch {
                    homeViewModel.posts.collect { uiState ->
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
                                recyclerView = binding.rv
                                recyclerView.adapter = HomeAdapter(
                                    uiState.data
                                )
                                recyclerView.layoutManager = GridLayoutManager(context, 3)
                            }
                        }
                    }
                }
            }
        }
    }

}