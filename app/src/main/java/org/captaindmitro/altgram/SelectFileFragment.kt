package org.captaindmitro.altgram

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import org.captaindmitro.altgram.ui.viewmodels.DataViewModel

class SelectFileFragment : Fragment() {
    private val dataViewModel: DataViewModel by activityViewModels()

    val getImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        Log.i("Main", "Selected: $uri")
        uri?.let {
            dataViewModel.uploadImage(uri)
        }
        findNavController().navigateUp()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getImage.launch("image/*")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}