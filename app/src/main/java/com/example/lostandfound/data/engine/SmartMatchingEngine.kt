package com.example.lostandfound.data.engine

import com.example.lostandfound.data.local.entity.LostFoundItem
import com.example.lostandfound.data.local.entity.MatchCandidate
import com.example.lostandfound.data.remote.api.PythonMatchingApiService
import com.example.lostandfound.data.remote.api.RetrofitClient
import com.example.lostandfound.data.remote.model.BatchMatchRequest
import com.example.lostandfound.data.remote.model.MatchItemDto
import kotlin.math.abs
import kotlin.math.min

data class SmartMatchingResult(
    val candidates: List<MatchCandidate>,
    val isPythonConnected: Boolean,
    val sourceDescription: String
)

class SmartMatchingEngine(
    private var apiService: PythonMatchingApiService = RetrofitClient.createService()
) {

    fun updateBackendUrl(baseUrl: String) {
        try {
            apiService = RetrofitClient.createService(baseUrl)
        } catch (e: Exception) {
            // Keep default service if invalid URL
        }
    }

    suspend fun executeMatching(
        lostItems: List<LostFoundItem>,
        foundItems: List<LostFoundItem>
    ): SmartMatchingResult {
        if (lostItems.isEmpty() || foundItems.isEmpty()) {
            return SmartMatchingResult(
                candidates = emptyList(),
                isPythonConnected = false,
                sourceDescription = "No open items available for matching."
            )
        }

        // Try Python API Backend first
        try {
            val lostDtos = lostItems.map { it.toDto() }
            val foundDtos = foundItems.map { it.toDto() }

            val responseDtos = apiService.matchBatch(
                BatchMatchRequest(lostItems = lostDtos, foundItems = foundDtos)
            )

            val candidates = responseDtos.map { dto ->
                MatchCandidate(
                    lostItemId = dto.lostItemId,
                    foundItemId = dto.foundItemId,
                    confidenceScore = dto.confidenceScore,
                    matchReason = "[Python Analytics Engine] ${dto.matchReason}",
                    status = MatchCandidate.STATUS_PENDING,
                    calculatedAt = System.currentTimeMillis()
                )
            }

            return SmartMatchingResult(
                candidates = candidates,
                isPythonConnected = true,
                sourceDescription = "Matched using Python FastAPI ML/Analytics Backend."
            )

        } catch (e: Exception) {
            // Offline / Server Unreachable Fallback: Rule-Based Local Matching Engine
            val localCandidates = computeLocalRuleBasedMatches(lostItems, foundItems)
            return SmartMatchingResult(
                candidates = localCandidates,
                isPythonConnected = false,
                sourceDescription = "Matched using Local Rule-Based Engine (Python backend offline)."
            )
        }
    }

    fun computeLocalRuleBasedMatches(
        lostItems: List<LostFoundItem>,
        foundItems: List<LostFoundItem>
    ): List<MatchCandidate> {
        val matches = mutableListOf<MatchCandidate>()

        for (lost in lostItems) {
            for (found in foundItems) {
                val (score, reason) = calculateConfidence(lost, found)
                if (score >= 0.35f) {
                    matches.add(
                        MatchCandidate(
                            lostItemId = lost.id,
                            foundItemId = found.id,
                            confidenceScore = score,
                            matchReason = reason,
                            status = MatchCandidate.STATUS_PENDING,
                            calculatedAt = System.currentTimeMillis()
                        )
                    )
                }
            }
        }
        return matches
    }

    private fun calculateConfidence(
        lost: LostFoundItem,
        found: LostFoundItem
    ): Pair<Float, String> {
        var score = 0.0f
        val reasons = mutableListOf<String>()

        // 1. Exact Category Match (Weight: 0.35)
        if (lost.category.equals(found.category, ignoreCase = true)) {
            score += 0.35f
            reasons.add("Category: ${lost.category}")
        }

        // 2. Location Proximity / Match (Weight: 0.35)
        if (lost.location.equals(found.location, ignoreCase = true)) {
            score += 0.35f
            reasons.add("Location: ${lost.location}")
        } else if (lost.location.contains(found.location, ignoreCase = true) ||
            found.location.contains(lost.location, ignoreCase = true)
        ) {
            score += 0.20f
            reasons.add("Location area match")
        }

        // 3. Keyword Similarity (Weight: 0.20)
        val lostKeywords = extractKeywords("${lost.title} ${lost.description}")
        val foundKeywords = extractKeywords("${found.title} ${found.description}")
        if (lostKeywords.isNotEmpty() && foundKeywords.isNotEmpty()) {
            val common = lostKeywords.intersect(foundKeywords)
            val union = lostKeywords.union(foundKeywords)
            if (union.isNotEmpty()) {
                val jaccard = common.size.toFloat() / union.size.toFloat()
                score += min(0.20f, jaccard * 0.5f)
                if (common.isNotEmpty()) {
                    reasons.add("Keywords: ${common.take(3).joinToString(", ")}")
                }
            }
        }

        // 4. Date Proximity (Weight: 0.10)
        val dayDifference = abs(lost.dateReported - found.dateReported) / (1000 * 60 * 60 * 24).toFloat()
        if (dayDifference <= 1.0f) {
            score += 0.10f
            reasons.add("Reported within 24 hours")
        } else if (dayDifference <= 3.0f) {
            score += 0.05f
            reasons.add("Reported within 3 days")
        }

        val finalScore = min(1.0f, score)
        val reasonSummary = if (reasons.isNotEmpty()) reasons.joinToString(" • ") else "Low structural similarity"

        return Pair(finalScore, reasonSummary)
    }

    private fun extractKeywords(text: String): Set<String> {
        val stopWords = setOf(
            "a", "an", "the", "and", "or", "in", "on", "at", "to", "for", "with",
            "is", "of", "my", "was", "near", "found", "lost", "has", "it", "this", "room"
        )
        return text.lowercase()
            .replace(Regex("[^a-z0-9\\s]"), "")
            .split(Regex("\\s+"))
            .filter { it.length > 2 && it !in stopWords }
            .toSet()
    }

    private fun LostFoundItem.toDto() = MatchItemDto(
        id = id,
        title = title,
        description = description,
        category = category,
        location = location,
        dateReported = dateReported,
        type = type
    )
}
