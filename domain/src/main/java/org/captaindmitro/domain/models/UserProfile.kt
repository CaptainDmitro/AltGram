package org.captaindmitro.domain.models

data class UserProfile(
    val userName: String,
    val email: String,
    val posts: Int,
    val followers: Int,
    val follows: Int,
    val images: List<String>
)