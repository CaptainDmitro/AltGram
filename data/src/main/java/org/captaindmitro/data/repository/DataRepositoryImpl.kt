package org.captaindmitro.data.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import org.captaindmitro.domain.repositories.DataRepository
import java.util.*
import javax.inject.Inject

class DataRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseStorage: FirebaseStorage
) : DataRepository {

    private val currentUser = firebaseAuth.currentUser!!.displayName!!

    override suspend fun uploadImage(uri: String): String {
        val storageRef = firebaseStorage.reference

        val testFileRef = storageRef.child("$currentUser/${UUID.randomUUID()}")
        val uploadTask = testFileRef.putFile(Uri.parse(uri))
        uploadTask.await()
        val downloadUrl = testFileRef.downloadUrl.await()
        Log.i("Main", "Image url: $downloadUrl")
        return downloadUrl.toString()
    }

}