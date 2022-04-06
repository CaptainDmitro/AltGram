package org.captaindmitro.altgram.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.clear
import coil.dispose
import coil.load
import coil.transform.CircleCropTransformation
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
        Log.i("Main", "New Avatar profile url: ${post.id}")

        with (holder) {
            binding.commentItemComment.text = post.comment
            binding.commentItemAvatar.dispose()
            binding.commentItemAvatar.load(post.id) {
                transformations(CircleCropTransformation())
            }
        }
    }

    override fun onViewRecycled(holder: CommentsViewHolder) {
        holder.binding.commentItemAvatar.dispose()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun submitDataSet(newItems: List<Comment>) {
        Log.i("Main", "RERERE $newItems")
        if (items.size == 0) {
            items.addAll(newItems)
            notifyDataSetChanged()
        } else {
            items.add(0, newItems[newItems.size - 1])
            notifyItemInserted(0)
        }
    }

}