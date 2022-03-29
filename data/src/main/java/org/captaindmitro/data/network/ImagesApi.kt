package org.captaindmitro.data.network

import retrofit2.http.GET

interface ImagesApi {

    @GET("/v1/curated?page=1&per_page=45")
    suspend fun getCuratedPhotos(): ImagesApiResponse

}