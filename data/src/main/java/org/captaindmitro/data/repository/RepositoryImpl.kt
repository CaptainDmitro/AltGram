package org.captaindmitro.data.repository

import org.captaindmitro.data.network.toDomain
import org.captaindmitro.domain.repositories.Repository
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : Repository {

    override suspend fun getCuratedPhotos() = remoteDataSource.getCuratedPhotos().toDomain()

}