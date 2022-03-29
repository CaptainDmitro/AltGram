package org.captaindmitro.altgram.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.captaindmitro.altgram.utils.UiState
import javax.inject.Inject

class ProfileViewModel @Inject constructor() : ViewModel() {

    private val _posts: MutableStateFlow<UiState> = MutableStateFlow(UiState.Empty)
    val posts: StateFlow<UiState> = _posts.asStateFlow()

    private var _subscriptions: MutableStateFlow<Int> = MutableStateFlow(0)
    val subscriptions: StateFlow<Int> = _subscriptions.asStateFlow()

    private var _followedOn: MutableStateFlow<Int> = MutableStateFlow(0)
    val followedOn: StateFlow<Int> = _followedOn.asStateFlow()

    private var _contentCount: MutableStateFlow<Int> = MutableStateFlow(0)
    val contentCount: StateFlow<Int> = _contentCount.asStateFlow()

    init {
        updatePosts()
    }

    fun updatePosts() {
        viewModelScope.launch {
            _posts.value = UiState.Loading
            delay(3000L)
            _posts.value = UiState.Empty
        }
    }
}