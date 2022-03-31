package org.captaindmitro.domain.repositories

import org.captaindmitro.domain.models.UserProfile

interface ProfileRepository {

    suspend fun getProfile(): UserProfile

    suspend fun updateProfile(userProfile: UserProfile)

    suspend fun createNewProfile(userProfile: UserProfile)

}