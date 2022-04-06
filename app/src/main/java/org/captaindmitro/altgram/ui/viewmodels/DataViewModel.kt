package org.captaindmitro.altgram.ui.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import org.captaindmitro.domain.models.Post
import org.captaindmitro.domain.repositories.DataRepository
import org.captaindmitro.domain.repositories.ProfileRepository
import javax.inject.Inject

@HiltViewModel
class DataViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val dataRepository: DataRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    fun uploadPost(uri: Uri) {
        viewModelScope.launch {
            val downloadUrl = withContext(Dispatchers.IO) { dataRepository.uploadImage(uri.toString()) }
            val userPosts = withContext(Dispatchers.IO) { profileRepository.getPosts() }
            Log.i("Main", "User posts: $userPosts")
            firebaseAuth.currentUser?.let {
                val newPost = Post(uri.lastPathSegment.toString(), downloadUrl, 0, emptyMap())
                Log.i("Main", "New post: $newPost")
                profileRepository.addNewPost(newPost)
            }
//            val userProfile = withContext(Dispatchers.IO) { profileRepository.getProfile() }
//            val newPosts = userProfile.posts + Post(uri.path!!, downloadUrl, 0, emptyList())
//            val newUserProfile = userProfile.copy(posts = newPosts)
//            Log.i("Main", "New images: ${newPosts}")
//            Log.i("Main", "New profile: ${newUserProfile}")
//            profileRepository.updateProfile(newUserProfile)
        }
    }

    init {
        getAllPosts()
    }

    fun getAllPosts() {
        viewModelScope.launch {
            dataRepository.getAllPosts()
        }
    }

}