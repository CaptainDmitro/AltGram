package org.captaindmitro.altgram.ui.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    private var _subscriptions: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    val subscriptions = _subscriptions.asStateFlow()

    private var _followedOn: MutableStateFlow<Int> = MutableStateFlow(0)
    val followedOn: StateFlow<Int> = _followedOn.asStateFlow()

    private var _contentCount: MutableStateFlow<Int> = MutableStateFlow(0)
    val contentCount: StateFlow<Int> = _contentCount.asStateFlow()

    fun updateAvatar(uri: Uri) {
        viewModelScope.launch {
            val avatarLink = withContext(Dispatchers.IO) { dataRepository.uploadAvatar(uri.toString()) }
            val currentProfile = profileRepository.getProfile(id.value).copy(avatar = avatarLink)
            profileRepository.updateProfile(currentProfile)
            _avatar.value = avatarLink
        }
    }

    suspend fun fetchProfile(userId: String) {
        val userProfile = withContext(Dispatchers.IO) { profileRepository.getProfile(userId) }
        Log.i("Main", "Fetched profile: $userProfile")

        _id.value = userProfile.id
        _avatar.value = userProfile.avatar
        _userName.value = userProfile.userName
        _followedOn.value = userProfile.followers.size
        _contentCount.value = profileRepository.getContentCounter(userId)
        _subscriptions.value = withContext(Dispatchers.IO) { profileRepository.getSubscriptions(userId) }
        Log.i("Main", "Subscriptions: ${subscriptions.value}")

        Log.i("Main", "User id: ${id.value}")

        _posts.value = UiState.Loading
        try {
            val posts = profileRepository.getPosts(userId)//.map { it.copy(id = userName.value) }
            _posts.value = if (posts.isEmpty()) UiState.Empty else UiState.Success(posts)
        } catch (e: Exception) {
            _posts.value = UiState.Error(e)
        }

    }

    fun updateFollowers() {
        _followedOn.value += 1
    }

    fun subscribeOn(userId: String) {
        viewModelScope.launch {
            profileRepository.subscribeOn(userId)
        }
    }

    fun unsubscribe(userId: String) {
        viewModelScope.launch {
            profileRepository.unsubscribeFrom(userId)
        }
    }
}