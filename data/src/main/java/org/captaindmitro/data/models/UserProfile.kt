package org.captaindmitro.data.models

internal data class UserProfile(
    val userName: String = "",
    val email: String = "",
    val posts: Int = 0,
    val followers: Int = 0,
    val follows: Int = 0,
    val images: List<String> = emptyList()
)

internal fun UserProfile.toDomain() = org.captaindmitro.domain.models.UserProfile(
    this.userName,
    this.email,
    this.posts,
    this.followers,
    this.follows,
    this.images
)