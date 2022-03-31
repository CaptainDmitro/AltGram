package org.captaindmitro.domain.repositories

import org.captaindmitro.domain.models.ImagesApiResponse

interface Repository {

    suspend fun getCuratedPhotos(): ImagesApiResponse

}