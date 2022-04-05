package org.captaindmitro.altgram.ui.newpost

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import org.captaindmitro.altgram.databinding.FragmentNewPostBinding
import org.captaindmitro.altgram.ui.viewmodels.DataViewModel

class NewPostFragment : Fragment() {
    private lateinit var binding: FragmentNewPostBinding
    private val dataViewModel: DataViewModel by activityViewModels()

    private val getImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        Log.i("Main", "Selected: $uri")
        uri?.let {
            dataViewModel.uploadPost(uri)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val selectFromDeviceButton = binding.selectFromDeviceButton

        selectFromDeviceButton.setOnClickListener {
            getImage.launch("image/*")
        }
    }
}