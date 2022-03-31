package org.captaindmitro.altgram.utils

import org.captaindmitro.domain.models.ImagesApiResponse

sealed class UiState {
    object Loading : UiState()
    class Success(val data: ImagesApiResponse) : UiState()
    class Error(val error: Exception) : UiState()
    object Empty : UiState()
}