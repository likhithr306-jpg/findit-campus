package com.example.lostandfound

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.lostandfound.data.local.AppDatabase
import com.example.lostandfound.data.preferences.UserPreferencesRepository
import com.example.lostandfound.data.repository.ItemRepository
import com.example.lostandfound.ui.navigation.MainNavigation
import com.example.lostandfound.ui.theme.LostAndFoundTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getDatabase(applicationContext)
        val repository = ItemRepository(
            itemDao = database.itemDao(),
            matchDao = database.matchDao()
        )
        val preferencesRepository = UserPreferencesRepository(applicationContext)

        setContent {
            LostAndFoundTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainNavigation(
                        repository = repository,
                        preferencesRepository = preferencesRepository
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainActivityPreview() {
    LostAndFoundTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            // Preview placeholder
        }
    }
}
