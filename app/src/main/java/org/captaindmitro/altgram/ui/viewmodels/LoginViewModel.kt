package org.captaindmitro.altgram.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.captaindmitro.domain.models.UserProfile
import org.captaindmitro.domain.repositories.ProfileRepository
import java.util.*
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val profileRepository: ProfileRepository,
) : ViewModel() {

    private var _currentUser: MutableStateFlow<FirebaseUser?> = MutableStateFlow(auth.currentUser)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser.asStateFlow()

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
                Log.i("Main", "After signup uid = ${currentUser.value?.uid}")
                changeDisplayName(userName, {}, {})

                val newUserProfile = createEmptyUserProfile(userName, email)
                profileRepository.createNewProfile(newUserProfile)

                onSuccess()
            } catch (e: Exception) {
                Log.i("Main", "Error in signing up $e")
                onFailure()
            }
        }
    }

    private fun createEmptyUserProfile(userName: String, email: String): UserProfile = UserProfile(
        currentUser.value?.uid ?: throw Exception("Cannot create new user profile"),
        "",
        userName,
        email,
        emptyList(),
        emptyList(),
        emptyList()
    )

    fun changeDisplayName(newName: String, onSuccess: () -> Unit, onFailure: () -> Unit) {
        viewModelScope.launch {
            try {
                currentUser.value?.updateProfile(userProfileChangeRequest {
                    displayName = newName
                })?.await()
                profileRepository.changeUserName(newName)
                onSuccess()
            } catch (e: Exception) {
                Log.i("Main", "Error in changing name $e")
                onFailure()
            }
        }
    }

    fun signOut() {
        auth.signOut()
        _currentUser.value = auth.currentUser
    }

}