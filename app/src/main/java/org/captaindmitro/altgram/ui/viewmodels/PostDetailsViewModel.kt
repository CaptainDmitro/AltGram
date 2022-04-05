package org.captaindmitro.altgram.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import org.captaindmitro.domain.models.Post
import org.captaindmitro.domain.models.UserProfile
import org.captaindmitro.domain.repositories.DataRepository
import org.captaindmitro.domain.repositories.ProfileRepository
import javax.inject.Inject

@HiltViewModel
class PostDetailsViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val dataRepository: DataRepository
) : ViewModel() {

    private val _post: MutableStateFlow<Post> = MutableStateFlow(Post("", "", 0, emptyList()))
    val post = _post.asStateFlow()

    private val _userName: MutableStateFlow<String> = MutableStateFlow("")
    val userName = _userName.asStateFlow()

    private val _urlToAvatar: MutableStateFlow<String> = MutableStateFlow("")
    val urlToAvatar = _urlToAvatar.asStateFlow()

    private val _description: MutableStateFlow<String> = MutableStateFlow("")
    val description = _description.asStateFlow()


    suspend fun fetchUserProfile(uid: String) {
        val userProfile = withContext(Dispatchers.IO) { profileRepository.getProfile(uid) }

        _userName.value = userProfile.userName
        _urlToAvatar.value = userProfile.avatar
    }

    suspend fun fetchPostData(id: String) {
        _post.value = withContext(Dispatchers.IO) { dataRepository.getPost(id) }
        Log.i("Main", "vm received post: ${_post.value}")
    }

}