package org.captaindmitro.altgram.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import org.captaindmitro.data.repository.DataRepositoryImpl
import org.captaindmitro.data.repository.ProfileRepositoryImpl
import org.captaindmitro.domain.repositories.DataRepository
import org.captaindmitro.domain.repositories.ProfileRepository

@Module
@InstallIn(ViewModelComponent::class)
abstract class Repos {

    @Binds
    abstract fun bindProfileRepository(profileRepositoryImpl: ProfileRepositoryImpl): ProfileRepository

    @Binds
    abstract fun bindDataRepository(dataRepositoryImpl: DataRepositoryImpl): DataRepository

}