package org.captaindmitro.altgram.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.captaindmitro.altgram.databinding.FragmentCommentItemBinding
import org.captaindmitro.domain.models.Comment

class CommentsAdapter : RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>() {
    private val items = mutableListOf<Comment>()

    class CommentsViewHolder(val binding: FragmentCommentItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        val binding = FragmentCommentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        val post = items[position]

        with (holder) {
            binding.commentItemComment.text = post.comment
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun submitItems(newItems: List<Comment>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

}