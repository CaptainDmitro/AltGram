package org.captaindmitro.altgram.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object Auth {

    @Provides
    fun provideAuth(): FirebaseAuth = FirebaseAuth.getInstance()

}