package org.captaindmitro.altgram.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.asDeferred
import kotlinx.coroutines.tasks.await
import org.captaindmitro.altgram.utils.UiState
import org.captaindmitro.domain.models.UserProfile
import org.captaindmitro.domain.repositories.ProfileRepository
import org.captaindmitro.domain.repositories.Repository
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val repository: Repository,
    private val profileRepository: ProfileRepository,
    private val firebaseStorage: FirebaseStorage
) : ViewModel() {

    private var _currentUser: MutableStateFlow<FirebaseUser?> = MutableStateFlow(auth.currentUser)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser.asStateFlow()

    private var _images: MutableStateFlow<UiState> = MutableStateFlow(UiState.Empty)
    val images: StateFlow<UiState> = _images.asStateFlow()

    init {
        getImages()
    }

    fun signIn(email: String, password: String, onSuccess: () -> Unit, onFailure: () -> Unit) {
        viewModelScope.launch {
            try {
                _currentUser.value = auth.signInWithEmailAndPassword(email, password).await().user
                onSuccess()
            } catch (e: Exception) {
                Log.i("Main", e.toString())
                onFailure()
            }

        }
    }

    fun signUp(userName: String, email: String, password: String, onSuccess: () -> Unit, onFailure: () -> Unit) {
        viewModelScope.launch {
            try {
                _currentUser.value = auth.createUserWithEmailAndPassword(email, password).await().user
                changeDisplayName(userName, {}, {})
                profileRepository.createNewProfile(UserProfile(userName, email, 0, 0, 0, emptyList()))
                onSuccess()
            } catch (e: Exception) {
                Log.i("Main", e.toString())
                onFailure()
            }
        }
    }

    fun changeDisplayName(newName: String, onSuccess: () -> Unit, onFailure: () -> Unit) {
        viewModelScope.launch {
            try {
                currentUser.value?.updateProfile(userProfileChangeRequest {
                    displayName = newName
                })?.await()
                onSuccess()
            } catch (e: Exception) {
                Log.i("Main", e.toString())
                onFailure()
            }
        }
    }

    fun signOut() {
        auth.signOut()
        _currentUser.value = auth.currentUser
    }

    fun getImages() {
        viewModelScope.launch {
            _images.value = UiState.Loading
            try {
                val result = repository.getCuratedPhotos()
                Log.i("Main", "${result.photos.size}")
                _images.value = UiState.Success(result)
            } catch (e:Exception) {
                Log.i("Main", "$e")
                _images.value = UiState.Error(e)
            }
        }

    }

}