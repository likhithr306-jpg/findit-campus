package com.example.lostandfound.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.lostandfound.data.local.entity.LostFoundItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Query("SELECT * FROM lost_found_items ORDER BY dateReported DESC")
    fun getAllItems(): Flow<List<LostFoundItem>>

    @Query("SELECT * FROM lost_found_items WHERE type = :type ORDER BY dateReported DESC")
    fun getItemsByType(type: String): Flow<List<LostFoundItem>>

    @Query("SELECT * FROM lost_found_items WHERE id = :id")
    fun getItemById(id: Long): Flow<LostFoundItem?>

    @Query("SELECT * FROM lost_found_items WHERE id = :id")
    suspend fun getItemByIdDirect(id: Long): LostFoundItem?

    @Query("""
        SELECT * FROM lost_found_items 
        WHERE (:type IS NULL OR type = :type)
          AND (:category IS NULL OR category = :category)
          AND (:location IS NULL OR location = :location)
          AND (:status IS NULL OR status = :status)
          AND (:query IS NULL OR title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' OR location LIKE '%' || :query || '%')
        ORDER BY dateReported DESC
    """)
    fun filterItems(
        query: String? = null,
        type: String? = null,
        category: String? = null,
        location: String? = null,
        status: String? = null
    ): Flow<List<LostFoundItem>>

    @Query("SELECT * FROM lost_found_items WHERE type = 'LOST' AND status = 'OPEN'")
    suspend fun getAllOpenLostItems(): List<LostFoundItem>

    @Query("SELECT * FROM lost_found_items WHERE type = 'FOUND' AND status = 'OPEN'")
    suspend fun getAllOpenFoundItems(): List<LostFoundItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: LostFoundItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<LostFoundItem>): List<Long>

    @Update
    suspend fun updateItem(item: LostFoundItem): Int

    @Query("UPDATE lost_found_items SET status = :status WHERE id = :id")
    suspend fun updateItemStatus(id: Long, status: String): Int

    @Query("UPDATE lost_found_items SET isSyncedWithPython = :isSynced WHERE id = :id")
    suspend fun updatePythonSyncStatus(id: Long, isSynced: Boolean): Int

    @Delete
    suspend fun deleteItem(item: LostFoundItem): Int

    @Query("DELETE FROM lost_found_items")
    suspend fun deleteAllItems(): Int
}
