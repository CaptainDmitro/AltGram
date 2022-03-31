package org.captaindmitro.altgram.utils

sealed class NewUiState {
    object Loading : NewUiState()
    class Success(val data: List<String>) : NewUiState()
    class Error(val error: Exception) : NewUiState()
    object Empty : NewUiState()
}