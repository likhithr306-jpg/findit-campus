package com.example.lostandfound.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.lostandfound.data.local.entity.LostFoundItem
import com.example.lostandfound.data.preferences.UserPreferencesRepository
import com.example.lostandfound.data.repository.ItemRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class DashboardStats(
    val totalLost: Int = 0,
    val totalFound: Int = 0,
    val totalResolved: Int = 0,
    val activeMatchesCount: Int = 0
)

data class DashboardUiState(
    val searchQuery: String = "",
    val selectedCategory: String? = null,
    val selectedType: String? = null,
    val selectedLocation: String? = null,
    val items: List<LostFoundItem> = emptyList(),
    val stats: DashboardStats = DashboardStats(),
    val isLoading: Boolean = false,
    val isPythonBackendConnected: Boolean = false,
    val userMessage: String? = null
)

class DashboardViewModel(
    private val repository: ItemRepository,
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _selectedCategory = MutableStateFlow<String?>(null)
    private val _selectedType = MutableStateFlow<String?>(null)
    private val _selectedLocation = MutableStateFlow<String?>(null)
    private val _isLoading = MutableStateFlow(false)
    private val _isPythonConnected = MutableStateFlow(false)
    private val _userMessage = MutableStateFlow<String?>(null)

    init {
        viewModelScope.launch {
            repository.ensureInitialData()
        }
    }

    private data class FilterParams(
        val query: String,
        val type: String?,
        val category: String?,
        val location: String?
    )

    private data class MetaState(
        val loading: Boolean,
        val pythonConnected: Boolean,
        val message: String?
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    private val filterParamsFlow = combine(
        _searchQuery,
        _selectedType,
        _selectedCategory,
        _selectedLocation
    ) { query, type, category, location ->
        FilterParams(query, type, category, location)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val filteredItems = filterParamsFlow.flatMapLatest { params ->
        repository.filterItems(
            query = params.query,
            type = params.type,
            category = params.category,
            location = params.location
        )
    }

    private val statsFlow = combine(
        repository.allItems,
        repository.allMatches
    ) { items, matches ->
        val totalLost = items.count { it.type == LostFoundItem.TYPE_LOST && it.status == LostFoundItem.STATUS_OPEN }
        val totalFound = items.count { it.type == LostFoundItem.TYPE_FOUND && it.status == LostFoundItem.STATUS_OPEN }
        val totalResolved = items.count { it.status == LostFoundItem.STATUS_CLAIMED || it.status == LostFoundItem.STATUS_RETURNED }
        val activeMatches = matches.size
        DashboardStats(totalLost, totalFound, totalResolved, activeMatches)
    }

    private val metaStateFlow = combine(
        _isLoading,
        _isPythonConnected,
        _userMessage
    ) { loading, pythonConnected, message ->
        MetaState(loading, pythonConnected, message)
    }

    val uiState: StateFlow<DashboardUiState> = combine(
        filterParamsFlow,
        filteredItems,
        statsFlow,
        metaStateFlow
    ) { filters, items, stats, meta ->
        DashboardUiState(
            searchQuery = filters.query,
            selectedCategory = filters.category,
            selectedType = filters.type,
            selectedLocation = filters.location,
            items = items,
            stats = stats,
            isLoading = meta.loading,
            isPythonBackendConnected = meta.pythonConnected,
            userMessage = meta.message
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardUiState()
    )

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onCategorySelected(category: String?) {
        _selectedCategory.value = category
    }

    fun onTypeSelected(type: String?) {
        _selectedType.value = type
    }

    fun onLocationSelected(location: String?) {
        _selectedLocation.value = location
    }

    fun clearFilters() {
        _searchQuery.value = ""
        _selectedCategory.value = null
        _selectedType.value = null
        _selectedLocation.value = null
    }

    fun recalculateSmartMatches() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.recalculateMatches()
            _isPythonConnected.value = result.isPythonConnected
            _userMessage.value = result.sourceDescription
            _isLoading.value = false
        }
    }

    fun resetSampleData() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.resetDatabaseToSampleData()
            _userMessage.value = "Database reset to initial sample data."
            _isLoading.value = false
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
            return DashboardViewModel(repository, preferencesRepository) as T
        }
    }
}
