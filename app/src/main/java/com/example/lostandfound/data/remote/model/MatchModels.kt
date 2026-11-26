package com.example.lostandfound.data.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MatchItemDto(
    @Json(name = "id") val id: Long,
    @Json(name = "title") val title: String,
    @Json(name = "description") val description: String,
    @Json(name = "category") val category: String,
    @Json(name = "location") val location: String,
    @Json(name = "dateReported") val dateReported: Long,
    @Json(name = "type") val type: String
)

@JsonClass(generateAdapter = true)
data class SingleMatchRequest(
    @Json(name = "lostItem") val lostItem: MatchItemDto,
    @Json(name = "foundItem") val foundItem: MatchItemDto
)

@JsonClass(generateAdapter = true)
data class BatchMatchRequest(
    @Json(name = "lostItems") val lostItems: List<MatchItemDto>,
    @Json(name = "foundItems") val foundItems: List<MatchItemDto>
)

@JsonClass(generateAdapter = true)
data class MatchResponseDto(
    @Json(name = "lostItemId") val lostItemId: Long,
    @Json(name = "foundItemId") val foundItemId: Long,
    @Json(name = "confidenceScore") val confidenceScore: Float,
    @Json(name = "matchReason") val matchReason: String,
    @Json(name = "isMatch") val isMatch: Boolean = true
)

@JsonClass(generateAdapter = true)
data class HealthCheckResponse(
    @Json(name = "status") val status: String,
    @Json(name = "service") val service: String = "FindIt Campus Python Analytics",
    @Json(name = "version") val version: String = "1.0.0"
)
