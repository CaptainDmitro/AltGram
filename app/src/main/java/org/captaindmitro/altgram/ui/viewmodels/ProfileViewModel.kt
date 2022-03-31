package org.captaindmitro.altgram.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.captaindmitro.altgram.utils.NewUiState
import org.captaindmitro.altgram.utils.UiState
import org.captaindmitro.domain.repositories.ProfileRepository
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(

    private val profileRepository: ProfileRepository
) : ViewModel() {

    private var _userName: MutableStateFlow<String> = MutableStateFlow("")
    val userName: StateFlow<String> = _userName.asStateFlow()

//    private val _posts: MutableStateFlow<UiState> = MutableStateFlow(UiState.Empty)
//    val posts: StateFlow<UiState> = _posts.asStateFlow()

    private val _posts: MutableStateFlow<NewUiState> = MutableStateFlow(NewUiState.Empty)
    val posts: StateFlow<NewUiState> = _posts.asStateFlow()

    private var _subscriptions: MutableStateFlow<Int> = MutableStateFlow(0)
    val subscriptions: StateFlow<Int> = _subscriptions.asStateFlow()

    private var _followedOn: MutableStateFlow<Int> = MutableStateFlow(0)
    val followedOn: StateFlow<Int> = _followedOn.asStateFlow()

    private var _contentCount: MutableStateFlow<Int> = MutableStateFlow(0)
    val contentCount: StateFlow<Int> = _contentCount.asStateFlow()

    init {
        viewModelScope.launch {
            launch { fetchProfile() }
            //launch { updatePosts() }
        }
    }

    fun fetchProfile() {
        viewModelScope.launch {
            val userProfile = profileRepository.getProfile()

            _userName.value = userProfile.userName
            _contentCount.value = userProfile.posts
            _subscriptions.value = userProfile.followers
            _followedOn.value = userProfile.follows

            _posts.value = NewUiState.Loading
            try {
                _posts.value = NewUiState.Success(userProfile.images)
            } catch (e: Exception) {
                Log.i("Main", "$e")
                _posts.value = NewUiState.Error(e)
            }

        }
    }

//    fun updatePosts() {
//        viewModelScope.launch {
//            _posts.value = UiState.Loading
//            delay(3000L)
//            _posts.value = UiState.Empty
//        }
//    }
}