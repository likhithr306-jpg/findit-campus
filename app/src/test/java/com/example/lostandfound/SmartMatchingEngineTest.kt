package com.example.lostandfound

import com.example.lostandfound.data.engine.SmartMatchingEngine
import com.example.lostandfound.data.local.SampleData
import com.example.lostandfound.data.local.entity.LostFoundItem
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SmartMatchingEngineTest {

    private val engine = SmartMatchingEngine()

    @Test
    fun localMatching_identifiesMatchesFromSampleData() = runTest {
        val lostItems = SampleData.initialItems.filter { it.type == LostFoundItem.TYPE_LOST }
        val foundItems = SampleData.initialItems.filter { it.type == LostFoundItem.TYPE_FOUND }

        val matches = engine.computeLocalRuleBasedMatches(lostItems, foundItems)

        assertTrue("Should calculate at least one match", matches.isNotEmpty())

        val topMatch = matches.maxByOrNull { it.confidenceScore }
        assertTrue("Top match score should be >= 0.70", topMatch != null && topMatch.confidenceScore >= 0.70f)
    }

    @Test
    fun localMatching_highConfidenceForIdenticalCategoryAndLocation() {
        val lostCard = LostFoundItem(
            id = 100L,
            title = "Student ID",
            description = "Lost my student ID card",
            category = "Cards & IDs",
            type = LostFoundItem.TYPE_LOST,
            location = "Central Library",
            dateReported = System.currentTimeMillis(),
            contactInfo = "test@campus.edu"
        )

        val foundCard = LostFoundItem(
            id = 101L,
            title = "Student ID Card",
            description = "Found a student ID card",
            category = "Cards & IDs",
            type = LostFoundItem.TYPE_FOUND,
            location = "Central Library",
            dateReported = System.currentTimeMillis(),
            contactInfo = "desk@campus.edu"
        )

        val matches = engine.computeLocalRuleBasedMatches(listOf(lostCard), listOf(foundCard))

        assertEquals(1, matches.size)
        assertTrue("Confidence score should be above 0.80", matches.first().confidenceScore >= 0.80f)
    }
}
