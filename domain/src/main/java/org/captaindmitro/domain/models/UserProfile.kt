package org.captaindmitro.domain.models

data class UserProfile(
    val id: String,
    val avatar: String,
    val userName: String,
    val email: String,
    val followers: Int,
    val follows: Int,
    val posts: List<Post>
)

data class Post(
    val id: String,
    val url: String,
    val likes: Int,
    val comments: List<String>
)

data class Comment(
    val id: String,
    val username: String,
    val comment: String
)