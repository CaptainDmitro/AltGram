package org.captaindmitro.altgram.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import kotlinx.coroutines.launch
import org.captaindmitro.altgram.R
import org.captaindmitro.altgram.databinding.FragmentProfileBinding
import org.captaindmitro.altgram.ui.home.HomeAdapter
import org.captaindmitro.altgram.ui.viewmodels.LoginViewModel
import org.captaindmitro.altgram.ui.viewmodels.ProfileViewModel
import org.captaindmitro.altgram.utils.UiState

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val args: ProfileFragmentArgs by navArgs()
    private val loginViewModel: LoginViewModel by activityViewModels()
    private val profileViewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val avatar = binding.userAvatar
        val userName = binding.userName
        val userSubscriptions = binding.userSubscriptions
        val userFollows = binding.userFollows
        val userContent = binding.userContent
        val accountEditButton = binding.userSettings
        val followButton = binding.userFollowButton

        accountEditButton.visibility = if (args.isSelf) View.VISIBLE else View.GONE
        followButton.visibility = if (args.isSelf) View.GONE else View.VISIBLE

        avatar.load(R.drawable.ic_launcher_background) {
            transformations(CircleCropTransformation())
        }

        accountEditButton.setOnClickListener {
            val actions = ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment()
            findNavController().navigate(actions)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { loginViewModel.currentUser.collect { userName.text = it?.displayName.toString() } }
                launch { profileViewModel.subscriptions.collect { userSubscriptions.text = "$it\nfollowers" } }
                launch { profileViewModel.followedOn.collect { userFollows.text =  "$it\nfollowing" } }
                launch { profileViewModel.contentCount.collect { userContent.text =  "$it\nposts" } }
                launch {
                    profileViewModel.posts.collect { profileState ->
                        when (profileState) {
                            is UiState.Empty -> { binding.message.apply { visibility = View.VISIBLE; text = "No posts found" } }
                            is UiState.Loading -> { binding.message.apply { visibility = View.VISIBLE; text = "Loading..." } }
                            is UiState.Success -> {
                                binding.message.visibility = View.GONE
                                val recyclerView = binding.rv
                                recyclerView.layoutManager = GridLayoutManager(context, 3)
                                recyclerView.adapter = HomeAdapter(List(1000) { "Item $it" })
                            }
                            is UiState.Error -> { binding.message.apply { visibility = View.VISIBLE; text = "${profileState.error}" } }
                        }
                    }
                }
            }
        }
    }
}