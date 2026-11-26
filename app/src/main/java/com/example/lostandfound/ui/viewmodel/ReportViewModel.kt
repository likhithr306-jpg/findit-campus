package com.example.lostandfound.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.lostandfound.data.local.entity.LostFoundItem
import com.example.lostandfound.data.preferences.UserPreferencesRepository
import com.example.lostandfound.data.repository.ItemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ReportUiState(
    val title: String = "",
    val description: String = "",
    val category: String = "Electronics",
    val type: String = LostFoundItem.TYPE_LOST,
    val location: String = "Central Library",
    val dateReported: Long = System.currentTimeMillis(),
    val contactInfo: String = "",
    val reporterName: String = "",
    val imageUrl: String? = null,
    val titleError: String? = null,
    val locationError: String? = null,
    val contactInfoError: String? = null,
    val isSubmitting: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val createdItemId: Long? = null
)

class ReportViewModel(
    private val repository: ItemRepository,
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReportUiState())
    val uiState: StateFlow<ReportUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val defaultName = preferencesRepository.userName.first()
            val defaultEmail = preferencesRepository.userEmail.first()
            _uiState.update { current ->
                current.copy(
                    reporterName = defaultName,
                    contactInfo = defaultEmail
                )
            }
        }
    }

    fun onTitleChanged(title: String) {
        _uiState.update { current ->
            current.copy(
                title = title,
                titleError = if (title.isBlank()) "Title is required" else null
            )
        }
    }

    fun onDescriptionChanged(description: String) {
        _uiState.update { current ->
            current.copy(description = description)
        }
    }

    fun onCategoryChanged(category: String) {
        _uiState.update { current ->
            current.copy(category = category)
        }
    }

    fun onTypeChanged(type: String) {
        _uiState.update { current ->
            current.copy(type = type)
        }
    }

    fun onLocationChanged(location: String) {
        _uiState.update { current ->
            current.copy(
                location = location,
                locationError = if (location.isBlank()) "Location is required" else null
            )
        }
    }

    fun onDateReportedChanged(timestamp: Long) {
        _uiState.update { current ->
            current.copy(dateReported = timestamp)
        }
    }

    fun onContactInfoChanged(contactInfo: String) {
        _uiState.update { current ->
            current.copy(
                contactInfo = contactInfo,
                contactInfoError = if (contactInfo.isBlank()) "Contact info is required" else null
            )
        }
    }

    fun onReporterNameChanged(reporterName: String) {
        _uiState.update { current ->
            current.copy(reporterName = reporterName)
        }
    }

    fun onImageUrlChanged(url: String?) {
        _uiState.update { current ->
            current.copy(imageUrl = url)
        }
    }

    fun submitReport() {
        val currentState = _uiState.value

        var hasError = false
        var titleErr: String? = null
        var locationErr: String? = null
        var contactErr: String? = null

        if (currentState.title.isBlank()) {
            titleErr = "Title cannot be empty"
            hasError = true
        }

        if (currentState.location.isBlank()) {
            locationErr = "Campus location is required"
            hasError = true
        }

        if (currentState.contactInfo.isBlank()) {
            contactErr = "Contact details are required"
            hasError = true
        }

        if (hasError) {
            _uiState.update { current ->
                current.copy(
                    titleError = titleErr,
                    locationError = locationErr,
                    contactInfoError = contactErr,
                    errorMessage = "Please fill in all required fields."
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { current -> current.copy(isSubmitting = true, errorMessage = null) }
            try {
                val newItem = LostFoundItem(
                    title = currentState.title.trim(),
                    description = currentState.description.trim(),
                    category = currentState.category,
                    type = currentState.type,
                    location = currentState.location.trim(),
                    dateReported = currentState.dateReported,
                    contactInfo = currentState.contactInfo.trim(),
                    reporterName = currentState.reporterName.ifBlank { "Campus Member" },
                    imageUrl = currentState.imageUrl,
                    status = LostFoundItem.STATUS_OPEN
                )

                val newId = repository.insertItem(newItem)

                _uiState.update { current ->
                    current.copy(
                        isSubmitting = false,
                        isSuccess = true,
                        createdItemId = newId
                    )
                }

            } catch (e: Exception) {
                _uiState.update { current ->
                    current.copy(
                        isSubmitting = false,
                        errorMessage = "Failed to submit report: ${e.localizedMessage}"
                    )
                }
            }
        }
    }

    fun resetForm() {
        viewModelScope.launch {
            val defaultName = preferencesRepository.userName.first()
            val defaultEmail = preferencesRepository.userEmail.first()
            _uiState.value = ReportUiState(
                reporterName = defaultName,
                contactInfo = defaultEmail
            )
        }
    }

    fun clearSuccessState() {
        _uiState.update { current -> current.copy(isSuccess = false, createdItemId = null) }
    }

    class Factory(
        private val repository: ItemRepository,
        private val preferencesRepository: UserPreferencesRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ReportViewModel(repository, preferencesRepository) as T
        }
    }
}
