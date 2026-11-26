package com.example.lostandfound.data.repository

import com.example.lostandfound.data.engine.SmartMatchingEngine
import com.example.lostandfound.data.engine.SmartMatchingResult
import com.example.lostandfound.data.local.dao.ItemDao
import com.example.lostandfound.data.local.dao.MatchDao
import com.example.lostandfound.data.local.entity.LostFoundItem
import com.example.lostandfound.data.local.entity.MatchCandidate
import com.example.lostandfound.data.local.SampleData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class ItemRepository(
    private val itemDao: ItemDao,
    private val matchDao: MatchDao,
    private val matchingEngine: SmartMatchingEngine = SmartMatchingEngine()
) {
    val allItems: Flow<List<LostFoundItem>> = itemDao.getAllItems()
    val allMatches: Flow<List<MatchCandidate>> = matchDao.getAllMatches()

    fun updateBackendUrl(baseUrl: String) {
        matchingEngine.updateBackendUrl(baseUrl)
    }

    fun getItemsByType(type: String): Flow<List<LostFoundItem>> {
        return itemDao.getItemsByType(type)
    }

    fun getItemById(id: Long): Flow<LostFoundItem?> {
        return itemDao.getItemById(id)
    }

    suspend fun getItemByIdDirect(id: Long): LostFoundItem? {
        return itemDao.getItemByIdDirect(id)
    }

    fun filterItems(
        query: String? = null,
        type: String? = null,
        category: String? = null,
        location: String? = null,
        status: String? = null
    ): Flow<List<LostFoundItem>> {
        val formattedQuery = query?.takeIf { it.isNotBlank() }?.trim()
        val formattedType = type?.takeIf { it.isNotBlank() && it != "ALL" }
        val formattedCategory = category?.takeIf { it.isNotBlank() && it != "ALL" }
        val formattedLocation = location?.takeIf { it.isNotBlank() && it != "ALL" }
        val formattedStatus = status?.takeIf { it.isNotBlank() && it != "ALL" }

        return itemDao.filterItems(
            query = formattedQuery,
            type = formattedType,
            category = formattedCategory,
            location = formattedLocation,
            status = formattedStatus
        )
    }

    suspend fun insertItem(item: LostFoundItem): Long {
        val newId = itemDao.insertItem(item)
        recalculateMatches()
        return newId
    }

    suspend fun updateItem(item: LostFoundItem): Int {
        val updated = itemDao.updateItem(item)
        recalculateMatches()
        return updated
    }

    suspend fun updateItemStatus(id: Long, status: String): Int {
        val result = itemDao.updateItemStatus(id, status)
        if (status == LostFoundItem.STATUS_CLAIMED || status == LostFoundItem.STATUS_RETURNED) {
            matchDao.deleteMatchesForItem(id)
        }
        return result
    }

    suspend fun deleteItem(item: LostFoundItem): Int {
        matchDao.deleteMatchesForItem(item.id)
        return itemDao.deleteItem(item)
    }

    fun getMatchesForLostItem(itemId: Long): Flow<List<MatchCandidate>> {
        return matchDao.getMatchesForLostItem(itemId)
    }

    fun getMatchesForFoundItem(itemId: Long): Flow<List<MatchCandidate>> {
        return matchDao.getMatchesForFoundItem(itemId)
    }

    suspend fun updateMatchStatus(matchId: Long, status: String): Int {
        return matchDao.updateMatchStatus(matchId, status)
    }

    suspend fun ensureInitialData() {
        val currentItems = itemDao.getAllItems().first()
        if (currentItems.isEmpty()) {
            resetDatabaseToSampleData()
        }
    }

    suspend fun resetDatabaseToSampleData() {
        itemDao.deleteAllItems()
        matchDao.deleteAllMatches()

        itemDao.insertItems(SampleData.initialItems)
        matchDao.insertMatches(SampleData.initialMatches)

        recalculateMatches()
    }

    suspend fun recalculateMatches(): SmartMatchingResult {
        val lostItems = itemDao.getAllOpenLostItems()
        val foundItems = itemDao.getAllOpenFoundItems()

        val result = matchingEngine.executeMatching(lostItems, foundItems)

        if (result.candidates.isNotEmpty()) {
            matchDao.insertMatches(result.candidates)
        }

        return result
    }
}
