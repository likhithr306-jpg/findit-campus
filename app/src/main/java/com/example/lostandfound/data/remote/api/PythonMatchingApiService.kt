package com.example.lostandfound.data.remote.api

import com.example.lostandfound.data.remote.model.BatchMatchRequest
import com.example.lostandfound.data.remote.model.HealthCheckResponse
import com.example.lostandfound.data.remote.model.MatchResponseDto
import com.example.lostandfound.data.remote.model.SingleMatchRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PythonMatchingApiService {

    @GET("api/health")
    suspend fun checkHealth(): HealthCheckResponse

    @POST("api/match/single")
    suspend fun matchSingle(@Body request: SingleMatchRequest): MatchResponseDto

    @POST("api/match/batch")
    suspend fun matchBatch(@Body request: BatchMatchRequest): List<MatchResponseDto>
}
