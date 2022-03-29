package org.captaindmitro.data.repository

import org.captaindmitro.data.network.ImagesApi
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val imagesApi: ImagesApi
) {

    suspend fun getCuratedPhotos() = imagesApi.getCuratedPhotos()

}