package org.captaindmitro.data.repository

import javax.inject.Inject

class Repository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) {

    suspend fun getCuratedPhotos() = remoteDataSource.getCuratedPhotos()

}