package org.captaindmitro.domain.repositories

interface DataRepository {

    suspend fun uploadImage(uri: String): String

}