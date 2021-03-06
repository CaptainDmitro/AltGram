package org.captaindmitro.domain.repositories

import org.captaindmitro.domain.models.Comment
import org.captaindmitro.domain.models.Post

interface DataRepository {

    suspend fun uploadImage(uri: String): String

    suspend fun uploadAvatar(uri: String): String

    suspend fun getAllPosts(): List<Post>

    suspend fun getPost(postId: String): Post

    suspend fun getFeed(): List<Post>

    suspend fun sendComment(postId: String, comment: String)

    suspend fun getComments(postId: String): List<Comment>

}