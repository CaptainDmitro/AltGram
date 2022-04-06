package org.captaindmitro.altgram.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.captaindmitro.altgram.utils.UiState
import org.captaindmitro.domain.models.UserProfile
import org.captaindmitro.domain.repositories.ProfileRepository
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _subscriptions: MutableStateFlow<UiState<List<UserProfile>>> = MutableStateFlow(UiState.Empty)
    val subscriptions = _subscriptions.asStateFlow()

    init {
        updateSubscriptions()
    }

    fun updateSubscriptions() {
        viewModelScope.launch {
            try {
                _subscriptions.value = UiState.Loading

                val userIds = profileRepository.getSubscriptions()
                val users = userIds.map {
                    profileRepository.getProfile(it)
                }
                _subscriptions.value = if (users.isEmpty()) UiState.Empty else UiState.Success(users)
            } catch (e: Exception) {
                _subscriptions.value = UiState.Error(e)
            }
        }
    }

    fun unsubscribeFrom(userId: String) {
        viewModelScope.launch {
            profileRepository.unsubscribeFrom(userId)
            updateSubscriptions()
        }
    }

}