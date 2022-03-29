package org.captaindmitro.altgram.ui.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import coil.load
import coil.request.CachePolicy
import org.captaindmitro.altgram.databinding.FragmentPostDetailsBinding

class PostDetailsFragment : Fragment() {
    private lateinit var binding: FragmentPostDetailsBinding
    private val args: PostDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val urlToImage = args.urlToImage
        binding.imageView3.load(urlToImage) {
            diskCacheKey(urlToImage)
            diskCachePolicy(CachePolicy.ENABLED)
        }
    }
}