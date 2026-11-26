package com.example.lostandfound.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "findit_user_preferences")

class UserPreferencesRepository(private val context: Context) {

    companion object {
        val SELECTED_LOCATION = stringPreferencesKey("selected_location")
        val SELECTED_CATEGORY = stringPreferencesKey("selected_category")
        val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val PYTHON_BACKEND_URL = stringPreferencesKey("python_backend_url")
        val IS_FIRST_LAUNCH = booleanPreferencesKey("is_first_launch")
    }

    val selectedLocation: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[SELECTED_LOCATION]
    }

    val selectedCategory: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[SELECTED_CATEGORY]
    }

    val notificationsEnabled: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[NOTIFICATIONS_ENABLED] ?: true
    }

    val userName: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[USER_NAME] ?: "Campus Student"
    }

    val userEmail: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[USER_EMAIL] ?: "student@campus.edu"
    }

    val pythonBackendUrl: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[PYTHON_BACKEND_URL] ?: "http://10.0.2.2:8000"
    }

    val isFirstLaunch: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_FIRST_LAUNCH] ?: true
    }

    suspend fun saveSelectedLocation(location: String?) {
        context.dataStore.edit { preferences ->
            if (location == null) {
                preferences.remove(SELECTED_LOCATION)
            } else {
                preferences[SELECTED_LOCATION] = location
            }
        }
    }

    suspend fun saveSelectedCategory(category: String?) {
        context.dataStore.edit { preferences ->
            if (category == null) {
                preferences.remove(SELECTED_CATEGORY)
            } else {
                preferences[SELECTED_CATEGORY] = category
            }
        }
    }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[NOTIFICATIONS_ENABLED] = enabled
        }
    }

    suspend fun saveUserProfile(name: String, email: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_NAME] = name
            preferences[USER_EMAIL] = email
        }
    }

    suspend fun savePythonBackendUrl(url: String) {
        context.dataStore.edit { preferences ->
            preferences[PYTHON_BACKEND_URL] = url
        }
    }

    suspend fun setFirstLaunchCompleted() {
        context.dataStore.edit { preferences ->
            preferences[IS_FIRST_LAUNCH] = false
        }
    }
}
