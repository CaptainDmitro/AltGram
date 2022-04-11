package org.captaindmitro.altgram.ui.details

import android.content.Intent
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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import kotlinx.coroutines.launch
import org.captaindmitro.altgram.R
import org.captaindmitro.altgram.adapters.CommentsAdapter
import org.captaindmitro.altgram.databinding.FragmentPostDetailsBinding
import org.captaindmitro.altgram.ui.viewmodels.FavoritesViewModel
import org.captaindmitro.altgram.ui.viewmodels.PostDetailsViewModel

class PostDetailsFragment : Fragment() {
    private lateinit var binding: FragmentPostDetailsBinding
    private val args: PostDetailsFragmentArgs by navArgs()
    private val postDetailsViewModel: PostDetailsViewModel by activityViewModels()
    private val favoritesViewModel: FavoritesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userNameTextView = binding.postUserName
        val postImageView = binding.postImage
        val avatarImageView = binding.postUserAvatar
        val descriptionTextView = binding.description
        val likeButton = binding.likeButton
        val shareButton = binding.shareButton
        val commentEditView = binding.leaveComment
        val commentAddButton = binding.commentsAddButton
        val commentRv = binding.commentsRv

        commentRv.layoutManager = LinearLayoutManager(context)
        val adapter = CommentsAdapter()
        commentRv.adapter = adapter

        likeButton.setOnClickListener {

        }

        commentAddButton.setOnClickListener {
            postDetailsViewModel.sendComment(commentEditView.text.toString(), args.postId)
        }

        shareButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            val post = "kek"
            intent.putExtra("title", post)
            startActivity(Intent.createChooser(intent, "Share using!"))
        }

        avatarImageView.setOnClickListener {
            val action = PostDetailsFragmentDirections.actionPostDetailsFragmentToProfileFragment2(isSelf = false, userId = args.postId.substringBefore('/'))
            findNavController().navigate(action)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                launch { postDetailsViewModel.fetchUserProfile(args.postId.substringBefore('/'), args.postId) }
                launch { postDetailsViewModel.fetchPostData(args.postId) }

                launch { postDetailsViewModel.userName.collect { userNameTextView.text = it } }

                launch { postDetailsViewModel.urlToAvatar.collect {
                    Log.i("Main", "Received avatar url $it")
                    avatarImageView.load(it.ifEmpty { R.drawable.ic_avatar_placeholder }) {
                        transformations(CircleCropTransformation())
                        diskCacheKey(it)
                        diskCachePolicy(CachePolicy.ENABLED)
                    }
                } }

                launch { postDetailsViewModel.post.collect {
                    postImageView.load(it.url) {
                        diskCacheKey(it.url)
                        diskCachePolicy(CachePolicy.ENABLED)
                    }

                } }
                launch { postDetailsViewModel.comments.collect {
                    adapter.submitDataSet(it)
                    (commentRv.layoutManager as LinearLayoutManager).scrollToPosition(0)
                } }
            }
        }

    }
}