package com.example.lostandfound.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lost_found_items")
data class LostFoundItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String,
    val description: String,
    val category: String,
    val type: String, // "LOST" or "FOUND"
    val location: String,
    val dateReported: Long, // Epoch timestamp in ms
    val contactInfo: String,
    val imageUrl: String? = null,
    val status: String = STATUS_OPEN, // "OPEN", "CLAIMED", "RETURNED"
    val isSyncedWithPython: Boolean = false,
    val reporterName: String = "Campus Member",
    val latitude: Double? = null,
    val longitude: Double? = null
) {
    companion object {
        const val TYPE_LOST = "LOST"
        const val TYPE_FOUND = "FOUND"

        const val STATUS_OPEN = "OPEN"
        const val STATUS_CLAIMED = "CLAIMED"
        const val STATUS_RETURNED = "RETURNED"

        val CATEGORIES = listOf(
            "Electronics",
            "Cards & IDs",
            "Keys",
            "Bags & Wallets",
            "Clothing & Accessories",
            "Books & Stationery",
            "Other"
        )

        val LOCATIONS = listOf(
            "Central Library",
            "Student Union",
            "Science Hall",
            "University Gym",
            "Cafeteria",
            "Engineering Building",
            "Math & Physics Center",
            "Campus Quad",
            "Dormitory North",
            "Dormitory South",
            "Transit Station"
        )
    }
}
