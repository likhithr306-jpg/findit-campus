package com.example.lostandfound.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.lostandfound.data.local.entity.MatchCandidate
import kotlinx.coroutines.flow.Flow

@Dao
interface MatchDao {
    @Query("SELECT * FROM match_candidates ORDER BY confidenceScore DESC")
    fun getAllMatches(): Flow<List<MatchCandidate>>

    @Query("SELECT * FROM match_candidates WHERE lostItemId = :lostItemId ORDER BY confidenceScore DESC")
    fun getMatchesForLostItem(lostItemId: Long): Flow<List<MatchCandidate>>

    @Query("SELECT * FROM match_candidates WHERE foundItemId = :foundItemId ORDER BY confidenceScore DESC")
    fun getMatchesForFoundItem(foundItemId: Long): Flow<List<MatchCandidate>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatch(match: MatchCandidate): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatches(matches: List<MatchCandidate>): List<Long>

    @Query("UPDATE match_candidates SET status = :status WHERE id = :id")
    suspend fun updateMatchStatus(id: Long, status: String): Int

    @Query("DELETE FROM match_candidates WHERE lostItemId = :itemId OR foundItemId = :itemId")
    suspend fun deleteMatchesForItem(itemId: Long): Int

    @Query("DELETE FROM match_candidates")
    suspend fun deleteAllMatches(): Int
}
