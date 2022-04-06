package org.captaindmitro.altgram.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.captaindmitro.altgram.utils.UiState
import org.captaindmitro.domain.models.Post
import org.captaindmitro.domain.models.UserProfile
import org.captaindmitro.domain.repositories.DataRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dataRepository: DataRepository
) : ViewModel() {

    private val _posts: MutableStateFlow<UiState<List<Post>>> = MutableStateFlow(UiState.Empty)
    val posts: StateFlow<UiState<List<Post>>> = _posts.asStateFlow()

    fun getAllPosts() {
        viewModelScope.launch {
            _posts.value = try {
                UiState.Loading
                val res = dataRepository.getAllPosts()
                if (res.isEmpty()) UiState.Empty else UiState.Success(res)
            } catch (e: Exception) {
                Log.i("Main", "error loading $e")
                UiState.Error(e)
            }
        }
    }

}