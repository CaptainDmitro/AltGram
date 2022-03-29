package org.captaindmitro.altgram.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.captaindmitro.altgram.utils.UiState
import org.captaindmitro.data.repository.Repository
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val repository: Repository
) : ViewModel() {

    private var _currentUser: MutableStateFlow<FirebaseUser?> = MutableStateFlow(auth.currentUser)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser.asStateFlow()

    private var _images: MutableStateFlow<UiState> = MutableStateFlow(UiState.Empty)
    val images: StateFlow<UiState> = _images.asStateFlow()

    init {
        getImages()
    }

    fun signIn(email: String, password: String, onSuccess: () -> Unit, onFailure: () -> Unit) = auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
        when (task.isSuccessful) {
            true -> { _currentUser.value = auth.currentUser; onSuccess() }
            false -> { onFailure() }
        }
    }

    fun signUp(email: String, password: String, onSuccess: () -> Unit, onFailure: () -> Unit) = auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
        when (task.isSuccessful) {
            true -> { _currentUser.value = auth.currentUser; onSuccess() }
            false -> { onFailure() }
        }
    }

    fun changeDisplayName(newName: String) {
        _currentUser.value?.updateProfile(userProfileChangeRequest {
            displayName = newName
        })
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