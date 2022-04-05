package org.captaindmitro.data.models

internal data class UserProfile(
    val id: String = "",
    val avatar: String = "",
    val userName: String = "",
    val email: String = "",
    val followers: Int = 0,
    val follows: Int = 0,
    val posts: List<Post> = emptyList(),
)

internal data class Post(
    val id: String = "",
    val url: String = "",
    val likes: Int = 0,
    val comments: List<String> = emptyList()
)

internal fun UserProfile.toDomain() = org.captaindmitro.domain.models.UserProfile(
    this.id,
    this.avatar,
    this.userName,
    this.email,
    this.followers,
    this.follows,
    this.posts.map { it.toDomain() }
)

internal fun Post.toDomain() = org.captaindmitro.domain.models.Post(
    this.id,
    this.url,
    this.likes,
    this.comments
)