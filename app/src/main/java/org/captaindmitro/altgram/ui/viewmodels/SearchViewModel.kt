package org.captaindmitro.altgram.ui.viewmodels

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
import org.captaindmitro.domain.models.UserProfile
import org.captaindmitro.domain.repositories.DataRepository
import org.captaindmitro.domain.repositories.ProfileRepository
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _feed: MutableStateFlow<UiState<List<Pair<UserProfile, Post>>>> = MutableStateFlow(UiState.Empty)
    val feed: StateFlow<UiState<List<Pair<UserProfile, Post>>>> = _feed.asStateFlow()

    init {
        viewModelScope.launch {
            fetchPosts()
        }
    }

    suspend fun fetchPosts() {
        withContext(Dispatchers.IO) {
            _feed.value = UiState.Loading
            try {
                val fetchedPosts = dataRepository.getFeed()
                val userNames = userProfiles(fetchedPosts)

                _feed.value = UiState.Success(userNames.zip(fetchedPosts) { a, b -> a to b })
            } catch (e: Exception) {
                _feed.value = UiState.Error(e)
            }
        }
    }

    private suspend fun userProfiles(posts: List<Post>): List<UserProfile> = withContext(Dispatchers.IO) {
        val userNames = mutableListOf<UserProfile>()
        userNames += posts.map {
            val userId = it.id.substringBefore('/')
            profileRepository.getProfile(userId)
        }

        userNames
    }

}