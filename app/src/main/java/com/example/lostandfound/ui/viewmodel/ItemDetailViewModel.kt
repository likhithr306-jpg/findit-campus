package com.example.lostandfound.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.lostandfound.data.local.entity.LostFoundItem
import com.example.lostandfound.data.local.entity.MatchCandidate
import com.example.lostandfound.data.repository.ItemRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class MatchCandidateWithItem(
    val candidate: MatchCandidate,
    val matchedItem: LostFoundItem
)

data class ItemDetailUiState(
    val item: LostFoundItem? = null,
    val matchCandidates: List<MatchCandidateWithItem> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val actionMessage: String? = null
)

class ItemDetailViewModel(
    private val repository: ItemRepository,
    private val itemId: Long
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    private val _errorMessage = MutableStateFlow<String?>(null)
    private val _actionMessage = MutableStateFlow<String?>(null)

    private val itemFlow = repository.getItemById(itemId)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val matchCandidatesFlow = itemFlow.flatMapLatest { currentItem ->
        if (currentItem == null) {
            flowOf(emptyList())
        } else {
            val matchesFlow = if (currentItem.type == LostFoundItem.TYPE_LOST) {
                repository.getMatchesForLostItem(currentItem.id)
            } else {
                repository.getMatchesForFoundItem(currentItem.id)
            }

            combine(matchesFlow, repository.allItems) { matches, allItems ->
                val itemMap = allItems.associateBy { it.id }
                matches.mapNotNull { match ->
                    val otherId = if (currentItem.type == LostFoundItem.TYPE_LOST) match.foundItemId else match.lostItemId
                    val otherItem = itemMap[otherId]
                    if (otherItem != null) {
                        MatchCandidateWithItem(candidate = match, matchedItem = otherItem)
                    } else null
                }
            }
        }
    }

    val uiState: StateFlow<ItemDetailUiState> = combine(
        itemFlow,
        matchCandidatesFlow,
        _isLoading,
        _errorMessage,
        _actionMessage
    ) { item, candidates, loading, error, actionMsg ->
        ItemDetailUiState(
            item = item,
            matchCandidates = candidates,
            isLoading = item == null && loading,
            errorMessage = error ?: if (item == null && !loading) "Item not found" else null,
            actionMessage = actionMsg
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ItemDetailUiState(isLoading = true)
    )

    fun markAsClaimed() {
        updateStatus(LostFoundItem.STATUS_CLAIMED, "Item successfully marked as Claimed!")
    }

    fun markAsReturned() {
        updateStatus(LostFoundItem.STATUS_RETURNED, "Item successfully marked as Returned!")
    }

    fun reopenItem() {
        updateStatus(LostFoundItem.STATUS_OPEN, "Item reopened.")
    }

    private fun updateStatus(status: String, successMsg: String) {
        viewModelScope.launch {
            try {
                repository.updateItemStatus(itemId, status)
                _actionMessage.value = successMsg
            } catch (e: Exception) {
                _errorMessage.value = "Failed to update status: ${e.localizedMessage}"
            }
        }
    }

    fun confirmMatch(matchId: Long) {
        viewModelScope.launch {
            try {
                repository.updateMatchStatus(matchId, MatchCandidate.STATUS_CONFIRMED)
                _actionMessage.value = "Match confirmed! Item status updated."
            } catch (e: Exception) {
                _errorMessage.value = "Failed to confirm match: ${e.localizedMessage}"
            }
        }
    }

    fun rejectMatch(matchId: Long) {
        viewModelScope.launch {
            try {
                repository.updateMatchStatus(matchId, MatchCandidate.STATUS_REJECTED)
                _actionMessage.value = "Match candidate rejected."
            } catch (e: Exception) {
                _errorMessage.value = "Failed to reject match: ${e.localizedMessage}"
            }
        }
    }

    fun deleteItem() {
        viewModelScope.launch {
            val item = itemFlow.stateIn(viewModelScope).value
            if (item != null) {
                repository.deleteItem(item)
                _actionMessage.value = "Item deleted."
            }
        }
    }

    fun clearActionMessage() {
        _actionMessage.value = null
    }

    class Factory(
        private val repository: ItemRepository,
        private val itemId: Long
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ItemDetailViewModel(repository, itemId) as T
        }
    }
}
