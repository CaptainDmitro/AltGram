package org.captaindmitro.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import org.captaindmitro.data.models.toDomain
import org.captaindmitro.domain.models.Post
import org.captaindmitro.domain.models.UserProfile
import org.captaindmitro.domain.repositories.ProfileRepository
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase,
    private val firebaseAuth: FirebaseAuth
) : ProfileRepository {
    private val currentUser = firebaseAuth.currentUser
    private val ref = firebaseDatabase.getReference(UserProfile::class.simpleName!!)

    override suspend fun getProfile(uid: String?): UserProfile {
        uid?.let {
            Log.i("Main", "Received id: $uid")
            val task = ref.get()
            val result = task.await().child(it).getValue(org.captaindmitro.data.models.UserProfile::class.java)
            Log.i("Main", "Received profile: ${result?.toDomain()}")

            return result?.toDomain() ?: throw Exception("Error in receiving user profile")
        }
        throw Exception("Error")
    }

    override suspend fun getProfile(): UserProfile = getProfile(currentUser?.uid)

    override suspend fun getPosts(): List<Post> {
        val userPosts = mutableListOf<Post>()
        currentUser?.let { user ->
            val postsRef = firebaseDatabase.getReference("Post")
            val task = postsRef.get()
            task.await().child(user.uid).children.forEach {
                val post = it.getValue(org.captaindmitro.data.models.Post::class.java)
                val newId = user.uid + '/' + post!!.id
                userPosts += post.toDomain().copy(id = newId)
            }
        }

        return userPosts
    }

    override suspend fun addNewPost(post: Post) {
        currentUser?.let {
            val postRef = firebaseDatabase.getReference("Post")
            postRef.child(it.uid).push().setValue(post).await()
        }
    }

    override suspend fun updateProfile(userProfile: UserProfile) {
        currentUser?.let {
            ref.updateChildren(mapOf(it.uid to userProfile)).await()
        }
    }

    override suspend fun createNewProfile(userProfile: UserProfile) {
        Log.i("Main", "init user uid: ${userProfile.id}")
        val task = ref.get()
        val result = task.await().child(userProfile.id).value
        if (result == null) {
            ref.updateChildren(mapOf(userProfile.id to userProfile)).await()
        }
//        val result = task.await().child(currentUser?.uid).value
//        Log.i("Main", "user uid: ${currentUser.uid}")
//        when (result) {
//            null -> { updateProfile(userProfile) }
//            else -> { throw Exception("Cannot create profile") }
//        }
    }

    override suspend fun userPostsCount(): Int {
        val postsCount =  firebaseDatabase.getReference("Post").child(currentUser?.uid!!).get().await().childrenCount
        return postsCount.toInt()
    }
}