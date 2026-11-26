package com.example.lostandfound

import com.example.lostandfound.data.local.SampleData
import com.example.lostandfound.data.local.entity.LostFoundItem
import org.junit.Assert.assertTrue
import org.junit.Test

class ItemRepositoryTest {

    @Test
    fun sampleData_containsLostAndFoundItems() {
        val items = SampleData.initialItems
        assertTrue("Sample data should not be empty", items.isNotEmpty())
        
        val lostItems = items.filter { it.type == LostFoundItem.TYPE_LOST }
        val foundItems = items.filter { it.type == LostFoundItem.TYPE_FOUND }

        assertTrue("Should have lost items", lostItems.isNotEmpty())
        assertTrue("Should have found items", foundItems.isNotEmpty())
    }

    @Test
    fun sampleData_initialMatchesAreValid() {
        val matches = SampleData.initialMatches
        assertTrue("Initial matches should exist", matches.isNotEmpty())
        
        val firstMatch = matches.first()
        assertTrue("Confidence score should be between 0 and 1", firstMatch.confidenceScore in 0.0f..1.0f)
        assertTrue("Match reason should not be empty", firstMatch.matchReason.isNotBlank())
    }
}
