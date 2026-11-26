package com.example.lostandfound.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.lostandfound.data.local.entity.LostFoundItem
import com.example.lostandfound.data.preferences.UserPreferencesRepository
import com.example.lostandfound.data.repository.ItemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class MyReportsUiState(
    val myItems: List<LostFoundItem> = emptyList(),
    val statusFilter: String? = null, // null for All
    val userEmail: String = "",
    val userName: String = "",
    val isLoading: Boolean = false,
    val userMessage: String? = null
)

class MyReportsViewModel(
    private val repository: ItemRepository,
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _statusFilter = MutableStateFlow<String?>(null)
    private val _userMessage = MutableStateFlow<String?>(null)

    val uiState: StateFlow<MyReportsUiState> = combine(
        repository.allItems,
        preferencesRepository.userEmail,
        preferencesRepository.userName,
        _statusFilter,
        _userMessage
    ) { allItems, email, name, statusFilter, message ->

        // Filter items that belong to user or show all reported items if user info matches
        val userItems = allItems.filter { item ->
            val matchesUser = item.contactInfo.contains(email, ignoreCase = true) ||
                    item.reporterName.contains(name, ignoreCase = true) ||
                    email.isBlank()
            val matchesStatus = statusFilter == null || item.status == statusFilter
            matchesUser && matchesStatus
        }

        MyReportsUiState(
            myItems = userItems,
            statusFilter = statusFilter,
            userEmail = email,
            userName = name,
            userMessage = message
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MyReportsUiState()
    )

    fun onStatusFilterChanged(status: String?) {
        _statusFilter.value = status
    }

    fun updateItemStatus(itemId: Long, status: String) {
        viewModelScope.launch {
            try {
                repository.updateItemStatus(itemId, status)
                _userMessage.value = "Report status changed to $status"
            } catch (e: Exception) {
                _userMessage.value = "Failed to update status: ${e.localizedMessage}"
            }
        }
    }

    fun deleteReport(item: LostFoundItem) {
        viewModelScope.launch {
            try {
                repository.deleteItem(item)
                _userMessage.value = "Report deleted successfully."
            } catch (e: Exception) {
                _userMessage.value = "Failed to delete report: ${e.localizedMessage}"
            }
        }
    }

    fun clearUserMessage() {
        _userMessage.value = null
    }

    class Factory(
        private val repository: ItemRepository,
        private val preferencesRepository: UserPreferencesRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MyReportsViewModel(repository, preferencesRepository) as T
        }
    }
}
