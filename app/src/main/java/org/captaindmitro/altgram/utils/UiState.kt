package org.captaindmitro.altgram.utils

import org.captaindmitro.data.network.ImagesApiResponse
import java.lang.Exception

sealed class UiState {
    object Loading : UiState()
    class Success(val data: ImagesApiResponse) : UiState()
    class Error(val error: Exception) : UiState()
    object Empty : UiState()
}