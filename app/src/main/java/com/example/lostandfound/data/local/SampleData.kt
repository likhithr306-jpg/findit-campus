package com.example.lostandfound.data.local

import com.example.lostandfound.data.local.entity.LostFoundItem
import com.example.lostandfound.data.local.entity.MatchCandidate

object SampleData {
    private val now = System.currentTimeMillis()
    private const val HOUR = 3600_000L
    private const val DAY = 86400_000L

    val initialItems = listOf(
        LostFoundItem(
            id = 1L,
            title = "Student ID Card - Alex Rivera",
            description = "Campus Student ID card with red lanyard. Lost near 2nd floor study area.",
            category = "Cards & IDs",
            type = LostFoundItem.TYPE_LOST,
            location = "Central Library",
            dateReported = now - (2 * HOUR),
            contactInfo = "alex.rivera@campus.edu | (555) 019-2834",
            imageUrl = null,
            status = LostFoundItem.STATUS_OPEN,
            reporterName = "Alex Rivera"
        ),
        LostFoundItem(
            id = 2L,
            title = "Student ID Card with Red Lanyard",
            description = "Found a student ID card belonging to Alex on the 2nd floor desk.",
            category = "Cards & IDs",
            type = LostFoundItem.TYPE_FOUND,
            location = "Central Library",
            dateReported = now - (1 * HOUR),
            contactInfo = "Turned in to Library Desk",
            imageUrl = null,
            status = LostFoundItem.STATUS_OPEN,
            reporterName = "Library Staff"
        ),
        LostFoundItem(
            id = 3L,
            title = "Apple AirPods Pro 2",
            description = "White AirPods Pro case with a blue silicone sleeve. Has a small scratch on back.",
            category = "Electronics",
            type = LostFoundItem.TYPE_LOST,
            location = "Student Union",
            dateReported = now - (5 * HOUR),
            contactInfo = "sarah.m@campus.edu",
            imageUrl = null,
            status = LostFoundItem.STATUS_OPEN,
            reporterName = "Sarah Miller"
        ),
        LostFoundItem(
            id = 4L,
            title = "AirPods Case (White in Blue Cover)",
            description = "Found white wireless earbuds case with blue cover near food court tables.",
            category = "Electronics",
            type = LostFoundItem.TYPE_FOUND,
            location = "Student Union",
            dateReported = now - (3 * HOUR),
            contactInfo = "union.info@campus.edu",
            imageUrl = null,
            status = LostFoundItem.STATUS_OPEN,
            reporterName = "Jordan Tech"
        ),
        LostFoundItem(
            id = 5L,
            title = "Toyota Car Key & Gym Locker Key",
            description = "Found silver keychain with 1 Toyota smart key and brass locker key #142.",
            category = "Keys",
            type = LostFoundItem.TYPE_FOUND,
            location = "University Gym",
            dateReported = now - (1 * DAY),
            contactInfo = "gym.frontdesk@campus.edu",
            imageUrl = null,
            status = LostFoundItem.STATUS_OPEN,
            reporterName = "Gym Reception"
        ),
        LostFoundItem(
            id = 6L,
            title = "Toyota Car Keys on Silver Ring",
            description = "Lost my Toyota key fob along with gym locker key while exercising.",
            category = "Keys",
            type = LostFoundItem.TYPE_LOST,
            location = "University Gym",
            dateReported = now - (1 * DAY + 2 * HOUR),
            contactInfo = "dave.k@campus.edu",
            imageUrl = null,
            status = LostFoundItem.STATUS_OPEN,
            reporterName = "David Kim"
        ),
        LostFoundItem(
            id = 7L,
            title = "Blue Hydro Flask 32oz Water Bottle",
            description = "Navy blue metal water bottle with National Park stickers.",
            category = "Other",
            type = LostFoundItem.TYPE_LOST,
            location = "Science Hall",
            dateReported = now - (2 * DAY),
            contactInfo = "emily.s@campus.edu",
            imageUrl = null,
            status = LostFoundItem.STATUS_OPEN,
            reporterName = "Emily Stone"
        ),
        LostFoundItem(
            id = 8L,
            title = "Blue Metal Flask with Stickers",
            description = "Found blue insulated water bottle left in Science Hall lecture room 102.",
            category = "Other",
            type = LostFoundItem.TYPE_FOUND,
            location = "Science Hall",
            dateReported = now - (2 * DAY - 1 * HOUR),
            contactInfo = "science.dept@campus.edu",
            imageUrl = null,
            status = LostFoundItem.STATUS_OPEN,
            reporterName = "Dr. Evans"
        ),
        LostFoundItem(
            id = 9L,
            title = "Brown Leather Fossil Wallet",
            description = "Contains driver's license, cash, and campus meal card. Lost during lunch.",
            category = "Bags & Wallets",
            type = LostFoundItem.TYPE_LOST,
            location = "Cafeteria",
            dateReported = now - (3 * DAY),
            contactInfo = "marcus.v@campus.edu",
            imageUrl = null,
            status = LostFoundItem.STATUS_OPEN,
            reporterName = "Marcus Vance"
        ),
        LostFoundItem(
            id = 10L,
            title = "MacBook Pro 67W USB-C Charger",
            description = "White Apple power adapter with white braided cable.",
            category = "Electronics",
            type = LostFoundItem.TYPE_FOUND,
            location = "Engineering Building",
            dateReported = now - (4 * DAY),
            contactInfo = "eng.lab@campus.edu",
            imageUrl = null,
            status = LostFoundItem.STATUS_OPEN,
            reporterName = "Lab Assistant"
        ),
        LostFoundItem(
            id = 11L,
            title = "TI-84 Plus CE Graphing Calculator",
            description = "Black Texas Instruments calculator with name label 'Chris' on back.",
            category = "Electronics",
            type = LostFoundItem.TYPE_LOST,
            location = "Math & Physics Center",
            dateReported = now - (5 * DAY),
            contactInfo = "chris.b@campus.edu",
            imageUrl = null,
            status = LostFoundItem.STATUS_OPEN,
            reporterName = "Chris Bennett"
        ),
        LostFoundItem(
            id = 12L,
            title = "Black North Face Backpack",
            description = "Found dark backpack near bus stop bench containing notebooks and umbrella.",
            category = "Bags & Wallets",
            type = LostFoundItem.TYPE_FOUND,
            location = "Transit Station",
            dateReported = now - (6 * DAY),
            contactInfo = "campus.security@campus.edu",
            imageUrl = null,
            status = LostFoundItem.STATUS_OPEN,
            reporterName = "Campus Security"
        )
    )

    val initialMatches = listOf(
        MatchCandidate(
            id = 1L,
            lostItemId = 1L, // Student ID Lost
            foundItemId = 2L, // Student ID Found
            confidenceScore = 0.95f,
            matchReason = "Exact name match 'Alex', matching location 'Central Library', category 'Cards & IDs'",
            status = MatchCandidate.STATUS_PENDING,
            calculatedAt = now - (1 * HOUR)
        ),
        MatchCandidate(
            id = 2L,
            lostItemId = 3L, // AirPods Lost
            foundItemId = 4L, // AirPods Found
            confidenceScore = 0.88f,
            matchReason = "Keyword match 'AirPods' & 'Blue cover', location match 'Student Union'",
            status = MatchCandidate.STATUS_PENDING,
            calculatedAt = now - (3 * HOUR)
        ),
        MatchCandidate(
            id = 3L,
            lostItemId = 6L, // Toyota Keys Lost
            foundItemId = 5L, // Toyota Keys Found
            confidenceScore = 0.91f,
            matchReason = "Keyword match 'Toyota key' & 'locker key', location match 'University Gym'",
            status = MatchCandidate.STATUS_PENDING,
            calculatedAt = now - (1 * DAY)
        ),
        MatchCandidate(
            id = 4L,
            lostItemId = 7L, // Hydro Flask Lost
            foundItemId = 8L, // Water Bottle Found
            confidenceScore = 0.82f,
            matchReason = "Keyword match 'Blue flask / stickers', location match 'Science Hall'",
            status = MatchCandidate.STATUS_PENDING,
            calculatedAt = now - (2 * DAY)
        )
    )
}
