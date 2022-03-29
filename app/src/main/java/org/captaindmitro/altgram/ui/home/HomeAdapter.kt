package org.captaindmitro.altgram.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import org.captaindmitro.altgram.R
import org.captaindmitro.altgram.databinding.FragmentHomeItemBinding

class HomeAdapter(private val items: List<String>) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    inner class HomeViewHolder(val binding: FragmentHomeItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding = FragmentHomeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val url = items[position]
        with (holder) {
            binding.imageView.load(url) {
                diskCacheKey(url)
                diskCachePolicy(CachePolicy.ENABLED)
                placeholder(R.drawable.ic_launcher_background)
            }
            binding.imageView.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToPostDetailsFragment(url)
                it.findNavController().navigate(action)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

}