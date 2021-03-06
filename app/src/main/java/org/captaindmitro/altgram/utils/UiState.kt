package org.captaindmitro.altgram.utils

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    class Success<T>(val data: T) : UiState<T>()
    class Error(val error: Exception) : UiState<Nothing>()
    object Empty : UiState<Nothing>()
}