package org.captaindmitro.altgram.ui.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.captaindmitro.altgram.utils.UiState
import org.captaindmitro.domain.models.Post
import org.captaindmitro.domain.repositories.DataRepository
import org.captaindmitro.domain.repositories.ProfileRepository
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val dataRepository: DataRepository
) : ViewModel() {

    private var _id: MutableStateFlow<String> = MutableStateFlow("")
    val id: StateFlow<String> = _id.asStateFlow()

    private var _avatar: MutableStateFlow<String> = MutableStateFlow("")
    val avatar: StateFlow<String> = _avatar.asStateFlow()

    private var _userName: MutableStateFlow<String> = MutableStateFlow("")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _posts: MutableStateFlow<UiState<List<Post>>> = MutableStateFlow(UiState.Empty)
    val posts: StateFlow<UiState<List<Post>>> = _posts.asStateFlow()

    private var _subscriptions: MutableStateFlow<Int> = MutableStateFlow(0)
    val subscriptions: StateFlow<Int> = _subscriptions.asStateFlow()

    private var _followedOn: MutableStateFlow<Int> = MutableStateFlow(0)
    val followedOn: StateFlow<Int> = _followedOn.asStateFlow()

    private var _contentCount: MutableStateFlow<Int> = MutableStateFlow(0)
    val contentCount: StateFlow<Int> = _contentCount.asStateFlow()


    fun updateAvatar(uri: Uri) {
        viewModelScope.launch {
            val avatarLink = dataRepository.uploadAvatar(uri.toString())
            val currentProfile = profileRepository.getProfile().copy(avatar = avatarLink)
            profileRepository.updateProfile(currentProfile)
            _avatar.value = avatarLink
        }
    }

    suspend fun fetchProfile() {
        val userProfile = profileRepository.getProfile()

        _id.value = userProfile.id
        _avatar.value = userProfile.avatar
        _userName.value = userProfile.userName
        _subscriptions.value = userProfile.followers
        _followedOn.value = userProfile.follows
        _contentCount.value = profileRepository.userPostsCount()

        _posts.value = UiState.Loading
        try {
            val posts = profileRepository.getPosts()
            _posts.value = if (posts.isEmpty()) UiState.Empty else UiState.Success(posts)
        } catch (e: Exception) {
            _posts.value = UiState.Error(e)
        }
    }

    suspend fun fetchProfile(userId: String) {
        val userProfile = profileRepository.getProfile(userId)

        _id.value = userProfile.id
        _avatar.value = userProfile.avatar
        _userName.value = userProfile.userName
        _subscriptions.value = userProfile.followers
        _followedOn.value = userProfile.follows

        _posts.value = UiState.Loading
        try {
            val posts = profileRepository.getPosts(userId).map { it.copy(id = userName.value) }
            _posts.value = if (posts.isEmpty()) UiState.Empty else UiState.Success(posts)
        } catch (e: Exception) {
            _posts.value = UiState.Error(e)
        }
    }
}