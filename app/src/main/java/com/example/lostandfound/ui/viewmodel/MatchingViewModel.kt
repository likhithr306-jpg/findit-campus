package com.example.lostandfound.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.lostandfound.data.local.entity.LostFoundItem
import com.example.lostandfound.data.local.entity.MatchCandidate
import com.example.lostandfound.data.repository.ItemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class MatchPair(
    val match: MatchCandidate,
    val lostItem: LostFoundItem,
    val foundItem: LostFoundItem
)

data class MatchingUiState(
    val matches: List<MatchPair> = emptyList(),
    val minConfidenceFilter: Float = 0.40f,
    val selectedStatusFilter: String? = null, // null for All
    val isLoading: Boolean = false,
    val isPythonConnected: Boolean = false,
    val engineDescription: String = "Local & Python Analytics Smart Matching Engine",
    val userMessage: String? = null
)

class MatchingViewModel(
    private val repository: ItemRepository
) : ViewModel() {

    private val _minConfidenceFilter = MutableStateFlow(0.40f)
    private val _selectedStatusFilter = MutableStateFlow<String?>(null)
    private val _isLoading = MutableStateFlow(false)
    private val _isPythonConnected = MutableStateFlow(false)
    private val _engineDescription = MutableStateFlow("Local & Remote Python Matching Active")
    private val _userMessage = MutableStateFlow<String?>(null)

    private data class FilterState(
        val minConfidence: Float,
        val statusFilter: String?
    )

    private data class MetaState(
        val loading: Boolean,
        val pythonConnected: Boolean,
        val engineDesc: String,
        val message: String?
    )

    private val matchPairsFlow = combine(
        repository.allMatches,
        repository.allItems
    ) { rawMatches, allItems ->
        val itemMap = allItems.associateBy { it.id }
        rawMatches.mapNotNull { match ->
            val lost = itemMap[match.lostItemId]
            val found = itemMap[match.foundItemId]
            if (lost != null && found != null) {
                MatchPair(match = match, lostItem = lost, foundItem = found)
            } else null
        }
    }

    private val filterStateFlow = combine(
        _minConfidenceFilter,
        _selectedStatusFilter
    ) { minConf, status ->
        FilterState(minConf, status)
    }

    private val metaStateFlow = combine(
        _isLoading,
        _isPythonConnected,
        _engineDescription,
        _userMessage
    ) { loading, pythonConnected, engineDesc, message ->
        MetaState(loading, pythonConnected, engineDesc, message)
    }

    val uiState: StateFlow<MatchingUiState> = combine(
        matchPairsFlow,
        filterStateFlow,
        metaStateFlow
    ) { pairs, filters, meta ->

        val filtered = pairs.filter { pair ->
            pair.match.confidenceScore >= filters.minConfidence &&
                    (filters.statusFilter == null || pair.match.status == filters.statusFilter)
        }.sortedByDescending { it.match.confidenceScore }

        MatchingUiState(
            matches = filtered,
            minConfidenceFilter = filters.minConfidence,
            selectedStatusFilter = filters.statusFilter,
            isLoading = meta.loading,
            isPythonConnected = meta.pythonConnected,
            engineDescription = meta.engineDesc,
            userMessage = meta.message
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MatchingUiState()
    )

    fun onMinConfidenceChanged(score: Float) {
        _minConfidenceFilter.value = score
    }

    fun onStatusFilterChanged(status: String?) {
        _selectedStatusFilter.value = status
    }

    fun runSmartMatchingEngine() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.recalculateMatches()
            _isPythonConnected.value = result.isPythonConnected
            _engineDescription.value = result.sourceDescription
            _userMessage.value = "Smart matching finished: ${result.candidates.size} potential pairs calculated."
            _isLoading.value = false
        }
    }

    fun updateMatchStatus(matchId: Long, status: String) {
        viewModelScope.launch {
            repository.updateMatchStatus(matchId, status)
            _userMessage.value = "Match status updated to $status"
        }
    }

    fun clearUserMessage() {
        _userMessage.value = null
    }

    class Factory(
        private val repository: ItemRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MatchingViewModel(repository) as T
        }
    }
}
