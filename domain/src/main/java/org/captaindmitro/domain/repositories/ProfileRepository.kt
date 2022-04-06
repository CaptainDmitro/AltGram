package org.captaindmitro.domain.repositories

import org.captaindmitro.domain.models.Post
import org.captaindmitro.domain.models.UserProfile

interface ProfileRepository {

    suspend fun getProfile(uid: String?): UserProfile

    suspend fun getPosts(userId: String): List<Post>

    suspend fun getSubscriptions(userId: String): List<String>

    suspend fun getSubscriptions(): List<String>

    suspend fun getUserAvatar(userId: String): String

    suspend fun getContentCounter(userId: String): Int

    suspend fun subscribeOn(userId: String)

    suspend fun unsubscribeFrom(userId: String)

    suspend fun publishPost(post: Post)

    suspend fun updateProfile(userProfile: UserProfile)

    suspend fun createNewProfile(userProfile: UserProfile)

    suspend fun changeUserName(userName: String)

}