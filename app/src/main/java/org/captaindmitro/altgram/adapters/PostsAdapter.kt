package org.captaindmitro.altgram.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import org.captaindmitro.altgram.R
import org.captaindmitro.altgram.databinding.FragmentHomeItemBinding
import org.captaindmitro.altgram.databinding.FragmentPostDetailsBinding
import org.captaindmitro.altgram.ui.details.PostDetailsFragmentDirections
import org.captaindmitro.domain.models.Post

class PostsAdapter(private val items: List<Post>) : RecyclerView.Adapter<PostsAdapter.PostViewHolder>() {

    inner class PostViewHolder(val binding: FragmentPostDetailsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = FragmentPostDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = items[position]
        with (holder) {
            binding.postImage.load(post.url) {
                diskCacheKey(post.url)
                diskCachePolicy(CachePolicy.ENABLED)
            }
            binding.postImage.setOnClickListener {
                val action = PostDetailsFragmentDirections.actionGlobalPostDetailsFragment(post.id)
                it.findNavController().navigate(action)
            }


        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

}