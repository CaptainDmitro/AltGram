package org.captaindmitro.altgram.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import org.captaindmitro.altgram.R
import org.captaindmitro.altgram.databinding.FragmentSearchItemBinding
import org.captaindmitro.domain.models.Post
import org.captaindmitro.domain.models.UserProfile

class SearchAdapter(val onSubscribe: (String) -> Unit) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {
    private val items = mutableListOf<Pair<UserProfile, Post>>()

    inner class SearchViewHolder(val binding: FragmentSearchItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = FragmentSearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val post = items[position]
        with (holder) {
            binding.searchUserAvatar.load(post.first.avatar.ifEmpty {R.drawable.ic_avatar_placeholder }) {
                transformations(CircleCropTransformation())
            }
            binding.searchUsername.text = post.first.userName
            binding.searchPost.load(post.second.url)
            binding.searchSubscribeButton.setOnClickListener {
                onSubscribe(post.first.id)
                items.removeAt(position)
                notifyItemRemoved(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun submitDataSet(newItems: List<Pair<UserProfile, Post>>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}