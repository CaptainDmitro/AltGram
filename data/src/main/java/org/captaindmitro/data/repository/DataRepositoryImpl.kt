package org.captaindmitro.data.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import org.captaindmitro.data.models.Comment
import org.captaindmitro.data.models.toDomain
import org.captaindmitro.domain.models.Post
import org.captaindmitro.domain.models.UserProfile
import org.captaindmitro.domain.repositories.DataRepository
import java.lang.Exception
import javax.inject.Inject

class DataRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseStorage: FirebaseStorage,
    private val firebaseDatabase: FirebaseDatabase
) : DataRepository {

    private val currentUser = firebaseAuth.currentUser?.displayName

    override suspend fun uploadImage(uri: String): String {
        val storageRef = firebaseStorage.reference
        val endUri = Uri.parse(uri)

        val imageRef = storageRef.child("$currentUser/${endUri.lastPathSegment}")
        val uploadTask = imageRef.putFile(endUri)
        uploadTask.await()
        val downloadUrl = imageRef.downloadUrl.await()
        Log.i("Main", "Image url: $downloadUrl")
        return downloadUrl.toString()
    }

    override suspend fun uploadAvatar(uri: String): String {
        val storageRef = firebaseStorage.reference
        val endUri = Uri.parse(uri)

        val imageRef = storageRef.child("$currentUser/avatar")
        val uploadTask = imageRef.putFile(endUri)
        uploadTask.await()
        val downloadUrl = imageRef.downloadUrl.await()
        Log.i("Main", "Avatar url: $downloadUrl")
        return downloadUrl.toString()
    }

    override suspend fun getAllPosts(): List<Post> {
        val allPosts = mutableListOf<Post>()
        val dbRef = firebaseDatabase.reference
        val postRef = dbRef.child("Post")

        postRef.get().await().children.forEach { userId ->
            if (userId.key == firebaseAuth.currentUser?.uid) {
                return@forEach
            }
            userId.children.forEach { posts ->
                val post = posts.getValue(org.captaindmitro.data.models.Post::class.java)
                post?.let {
                    allPosts += it.toDomain().copy(id = "${userId.key}/${it.id}")
                }
            }
        }
        Log.i("Main", "All posts: $allPosts")
        return allPosts
    }

    override suspend fun getFeed(): List<Post> {
        val allPosts = mutableListOf<Post>()
        val dbRef = firebaseDatabase.reference
        val postRef = dbRef.child("Post")

        postRef.get().await().children.forEach { userId ->
            if (userId.key == firebaseAuth.currentUser?.uid) {
                return@forEach
            }
            val post = userId.children.first().getValue(org.captaindmitro.data.models.Post::class.java)
            post?.let {
                allPosts += it.toDomain().copy(id = "${userId.key}/${it.id}")
            }
        }
        Log.i("Main", "All posts: $allPosts")
        return allPosts
    }

    override suspend fun getPost(postId: String): Post {
        Log.i("<Main", "Received postid $postId")
        val dbRef = firebaseDatabase.getReference("Post").child(postId.substringBefore('/'))
        dbRef.get().await().children.forEach {
            val res = it.getValue(org.captaindmitro.data.models.Post::class.java)
            if (res!!.id == postId.substringAfter('/')){
                return res.toDomain()
            }
        }

        throw Exception("Cound not find a post")
    }

    override suspend fun sendComment(postId: String, comment: String) {
        val (uId, pId) = postId.split('/')
        val dbRef = firebaseDatabase.getReference("Post").child(uId)
        dbRef.get().await().children.forEach {
            if (it.child("id").value == pId) {
                val commentRef = it.ref
                val newComment = Comment(firebaseAuth.currentUser!!.uid, comment)
                commentRef.child("comments").push().setValue(newComment)
                return
            }
        }
    }

    override suspend fun getComments(postId: String): List<org.captaindmitro.domain.models.Comment> {
        val comments = mutableListOf<org.captaindmitro.domain.models.Comment>()
        val (uId, pId) = postId.split('/')
        val dbRef = firebaseDatabase.getReference("Post").child(uId)
        dbRef.get().await().children.forEach {
            if (it.child("id").value == pId) {
                it.child("comments").children.forEach {
                    Log.i("Main", "comment $it")
                    comments += it.getValue(Comment::class.java)!!.toDomain()
                }
                return comments
            }
        }

        return emptyList()
    }

}