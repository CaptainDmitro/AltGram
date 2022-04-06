package org.captaindmitro.altgram.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.dispose
import coil.load
import coil.transform.CircleCropTransformation
import org.captaindmitro.altgram.R
import org.captaindmitro.altgram.databinding.FragmentFavoritesItemBinding
import org.captaindmitro.altgram.ui.favorites.FavoritesFragmentDirections
import org.captaindmitro.domain.models.UserProfile

class FavoritesAdapter(val onUnsubscribe: (String) -> Unit) : RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>() {
    private val items = mutableListOf<UserProfile>()

    class FavoritesViewHolder(val binding: FragmentFavoritesItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val binding = FragmentFavoritesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoritesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        val item = items[position]
        with (holder) {
            binding.favoritesUserName.text = item.userName

            binding.favoritesUserAvatar.load(item.avatar.ifEmpty { R.drawable.ic_avatar_placeholder }) {
                transformations(CircleCropTransformation())
            }
            binding.favoritesUserAvatar.setOnClickListener {
                val action = FavoritesFragmentDirections.actionFavoritesFragmentToProfileFragment2(isSelf = false, userId = item.id)
                it.findNavController().navigate(action)
            }

            binding.favoritesUnsubscribeButton.setOnClickListener {
                onUnsubscribe(item.id)
            }
        }
    }

    override fun onViewRecycled(holder: FavoritesViewHolder) {
        holder.binding.favoritesUserAvatar.dispose()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun submitDataSet(newItems: List<UserProfile>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

}