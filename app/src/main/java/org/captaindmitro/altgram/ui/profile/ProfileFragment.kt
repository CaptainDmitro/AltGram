package org.captaindmitro.altgram.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
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
import kotlinx.coroutines.*
import org.captaindmitro.altgram.R
import org.captaindmitro.altgram.adapters.HomeAdapter
import org.captaindmitro.altgram.databinding.FragmentProfileBinding
import org.captaindmitro.altgram.ui.viewmodels.LoginViewModel
import org.captaindmitro.altgram.ui.viewmodels.ProfileViewModel
import org.captaindmitro.altgram.utils.UiState

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val args: ProfileFragmentArgs by navArgs()
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private val loginViewModel: LoginViewModel by activityViewModels()
    private val uploadAvatarAction = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        Log.i("Main", "Selected avatar: $uri")
        uri?.let {
            profileViewModel.updateAvatar(uri)
        }
    }

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
        val userName = binding.postUserName
        val userSubscriptions = binding.userSubscriptions
        val userFollows = binding.userFollows
        val userContent = binding.userContent
        val accountEditButton = binding.userSettings
        val followButton = binding.userFollowButton


        accountEditButton.visibility = if (args.isSelf) View.VISIBLE else View.GONE
        followButton.visibility = if (args.isSelf) View.GONE else View.VISIBLE

        if (args.isSelf) {
            avatar.setOnClickListener {
                uploadAvatarAction.launch("image/*")
            }
        }

        followButton.setOnClickListener {
            profileViewModel.subscribeOn(args.userId!!)
            profileViewModel.updateFollowers()
            it.visibility = View.GONE
        }

        accountEditButton.setOnClickListener {
            val actions = ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment()
            findNavController().navigate(actions)
        }

        val recyclerView = binding.rv
        val message = binding.message
        recyclerView.layoutManager = GridLayoutManager(context, 3)
        val adapter = HomeAdapter()
        recyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                launch {
                    if (args.isSelf) {
                        loginViewModel.currentUser.collect {
                            profileViewModel.fetchProfile(it!!.uid)
                        }
                    } else {
                        profileViewModel.fetchProfile(args.userId!!)
                    }
                }

                launch { profileViewModel.avatar.collect {
                    avatar.load(it.ifEmpty { R.drawable.ic_avatar_placeholder }) {
                        transformations(CircleCropTransformation())
                    }
                } }
                launch { profileViewModel.userName.collect { userName.text = it } }
                launch { profileViewModel.subscriptions.collect { userSubscriptions.text = "${it.size}\nfollowing" } }
                launch { profileViewModel.followedOn.collect { userFollows.text =  "$it\nfollowers" } }
                launch { profileViewModel.contentCount.collect { userContent.text =  "$it\nposts" } }
                launch {
                    profileViewModel.posts.collect { uiState ->
                        when (uiState) {
                            is UiState.Empty -> { message.apply { visibility = View.VISIBLE; text = "No posts found" } }
                            is UiState.Loading -> { message.apply { visibility = View.VISIBLE; text = "Loading..." } }
                            is UiState.Success -> {
                                message.visibility = View.GONE
                                adapter.submitDataSet(uiState.data)
                            }
                            is UiState.Error -> { message.apply { visibility = View.VISIBLE; text = "${uiState.error}" } }
                        }
                    }
                }
            }
        }
    }
}