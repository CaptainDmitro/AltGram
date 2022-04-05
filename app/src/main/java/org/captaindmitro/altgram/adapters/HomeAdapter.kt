package org.captaindmitro.altgram.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import org.captaindmitro.altgram.R
import org.captaindmitro.altgram.databinding.FragmentHomeItemBinding
import org.captaindmitro.altgram.ui.details.PostDetailsFragmentDirections
import org.captaindmitro.domain.models.Post
import org.captaindmitro.domain.models.UserProfile

class HomeAdapter(private val posts: List<Post>) : RecyclerView.Adapter<HomeAdapter.PostViewHolder>() {

    inner class PostViewHolder(val binding: FragmentHomeItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = FragmentHomeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        with (holder) {
            binding.imageView.load(post.url) {
                diskCacheKey(post.url)
                diskCachePolicy(CachePolicy.ENABLED)
                placeholder(R.drawable.ic_launcher_background)
            }
            binding.imageView.setOnClickListener {
                val action = PostDetailsFragmentDirections.actionGlobalPostDetailsFragment(post.id)
                it.findNavController().navigate(action)
            }
        }
    }

    override fun getItemCount(): Int {
        return posts.size
    }

}