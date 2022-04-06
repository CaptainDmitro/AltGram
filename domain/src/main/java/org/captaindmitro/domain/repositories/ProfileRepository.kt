package org.captaindmitro.domain.repositories

import org.captaindmitro.domain.models.Post
import org.captaindmitro.domain.models.UserProfile

interface ProfileRepository {

    suspend fun getProfile(uid: String?): UserProfile

    suspend fun getProfile(): UserProfile

    suspend fun getPosts(): List<Post>

    suspend fun getPosts(userId: String): List<Post>

    suspend fun addNewPost(post: Post)

    suspend fun updateProfile(userProfile: UserProfile)

    suspend fun createNewProfile(userProfile: UserProfile)

    suspend fun userPostsCount(): Int

}