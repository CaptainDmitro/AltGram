package org.captaindmitro.data.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImagesApiResponse(
    val page: Int,
    val per_page: Int,
    val photos: List<PhotoResponse>,
    val next_page: String?,
    val prev_page: String?
)

@JsonClass(generateAdapter = true)
data class PhotoResponse(
    val id: Int,
    val width: Int,
    val height: Int,
    val url: String,
    val photographer: String,
    val photographer_url: String,
    val photographer_id: Int,
    val avg_color: String,
    val src: Sources,
    val liked: Boolean,
    val alt: String
)

@JsonClass(generateAdapter = true)
data class Sources(
    val original: String,
    val large2x: String,
    val large: String,
    val medium: String,
    val small: String,
    val portrait: String,
    val landscape: String,
    val tiny: String,
)

fun ImagesApiResponse.toDomain() = org.captaindmitro.domain.models.ImagesApiResponse(
    this.per_page,
    this.per_page,
    this.photos.map { it.toDomain() },
    this.next_page,
    this.prev_page
)

fun PhotoResponse.toDomain() = org.captaindmitro.domain.models.PhotoResponse(
    this.id,
    this.width,
    this.height,
    this.photographer_url,
    this.photographer,
    this.photographer_url,
    this.photographer_id,
    this.avg_color,
    this.src.toDomain(),
    this.liked,
    this.alt
)

fun Sources.toDomain() = org.captaindmitro.domain.models.Sources(
    this.original,
    this.large2x,
    this.large,
    this.medium,
    this.small,
    this.portrait,
    this.landscape,
    this.tiny
)