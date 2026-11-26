package com.example.lostandfound.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "match_candidates")
data class MatchCandidate(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val lostItemId: Long,
    val foundItemId: Long,
    val confidenceScore: Float, // 0.0 to 1.0 (or 0 to 100%)
    val matchReason: String,
    val status: String = STATUS_PENDING, // "PENDING", "CONFIRMED", "REJECTED"
    val calculatedAt: Long = System.currentTimeMillis()
) {
    companion object {
        const val STATUS_PENDING = "PENDING"
        const val STATUS_CONFIRMED = "CONFIRMED"
        const val STATUS_REJECTED = "REJECTED"
    }
}
