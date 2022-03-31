package org.captaindmitro.data.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object Firebase {

    @Provides
    fun provideAuth(): FirebaseAuth = Firebase.auth.apply {
        useEmulator("10.0.2.2", 9099)
    }

    @Provides
    fun provideFirebaseDatabase(): FirebaseDatabase = Firebase.database("https://altgram-bcbea-default-rtdb.europe-west1.firebasedatabase.app").apply {
        useEmulator("10.0.2.2", 9000)
    }

    @Provides
    fun provideFirebaseStorage(): FirebaseStorage = Firebase.storage.apply {
        useEmulator("10.0.2.2", 9199)
    }

}