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