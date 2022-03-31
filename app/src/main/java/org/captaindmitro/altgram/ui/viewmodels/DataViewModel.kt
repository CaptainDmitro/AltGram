package org.captaindmitro.altgram.ui.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.captaindmitro.domain.repositories.DataRepository
import org.captaindmitro.domain.repositories.ProfileRepository
import javax.inject.Inject

@HiltViewModel
class DataViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    fun uploadImage(uri: Uri) {
        viewModelScope.launch {
            val downloadUrl = withContext(Dispatchers.IO) { dataRepository.uploadImage(uri.toString()) }
            val userProfile = withContext(Dispatchers.IO) { profileRepository.getProfile() }
            val newImages = userProfile.images + downloadUrl
            val newUserProfile = userProfile.copy(images = newImages)
            Log.i("Main", "New images: ${newImages}")
            Log.i("Main", "New porofile: ${newUserProfile}")
            profileRepository.updateProfile(newUserProfile)
        }
    }

}