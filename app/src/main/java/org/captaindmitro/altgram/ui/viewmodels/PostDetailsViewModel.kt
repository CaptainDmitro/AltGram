package org.captaindmitro.altgram.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.captaindmitro.domain.models.Comment
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

    private val _comments: MutableStateFlow<List<Comment>> = MutableStateFlow(emptyList())
    val comments = _comments.asStateFlow()


    fun sendComment(comment: String, postId: String) {
        viewModelScope.launch {
            Log.i("Main", "vm sendCOmment $comment, $postId")
            dataRepository.sendComment(postId, comment)
            _comments.value = withContext(Dispatchers.IO) { dataRepository.getComments(postId).map { comment ->
                comment.copy(id = profileRepository.getUserAvatar(comment.id))
            } }
        }
    }

    suspend fun fetchUserProfile(uid: String, postId: String) {
        val userProfile = withContext(Dispatchers.IO) { profileRepository.getProfile(uid) }

        _userName.value = userProfile.userName
        _urlToAvatar.value = userProfile.avatar
        _comments.value = withContext(Dispatchers.IO) { dataRepository.getComments(postId).map { comment ->
            comment.copy(id = profileRepository.getUserAvatar(comment.id))
        } }
    }

    suspend fun fetchPostData(id: String) {
        _post.value = withContext(Dispatchers.IO) { dataRepository.getPost(id) }
        Log.i("Main", "vm received post: ${_post.value}")
    }

}