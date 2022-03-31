package org.captaindmitro.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await
import org.captaindmitro.data.models.toDomain
import org.captaindmitro.domain.models.UserProfile
import org.captaindmitro.domain.repositories.ProfileRepository
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase,
    private val firebaseAuth: FirebaseAuth
) : ProfileRepository {
    private val currentUser = firebaseAuth.currentUser?.displayName
    private val ref = firebaseDatabase.getReference(UserProfile::class.simpleName!!)

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getProfile(): UserProfile {
        currentUser?.let {
            val task = ref.get()
            val result = task.await().child(currentUser).getValue(org.captaindmitro.data.models.UserProfile::class.java)

            return result?.toDomain() ?: throw Exception("Error in receiving user profile")
        }
        throw Exception("Error")
    }

    override suspend fun updateProfile(userProfile: UserProfile) {
        ref.updateChildren(mapOf(userProfile.userName to userProfile)).await()
    }

    override suspend fun createNewProfile(userProfile: UserProfile) {
        val task = ref.get()
        val result = task.await().child(userProfile.userName).value
        when (result) {
            null -> { updateProfile(userProfile) }
            else -> { throw Exception("Cannot create profile") }
        }
    }
}