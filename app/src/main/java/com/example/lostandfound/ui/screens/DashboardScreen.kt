package com.example.lostandfound.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.FindInPage
import androidx.compose.material.icons.rounded.Help
import androidx.compose.material.icons.rounded.Psychology
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lostandfound.data.local.entity.LostFoundItem
import com.example.lostandfound.ui.components.CategoryFilterRow
import com.example.lostandfound.ui.components.ItemCard
import com.example.lostandfound.ui.components.StatCard
import com.example.lostandfound.ui.theme.ColorFoundBadge
import com.example.lostandfound.ui.theme.ColorHighMatch
import com.example.lostandfound.ui.theme.ColorLostBadge
import com.example.lostandfound.ui.theme.LostAndFoundTheme
import com.example.lostandfound.ui.viewmodel.DashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onItemClick: (Long) -> Unit,
    onNavigateToReport: () -> Unit,
    onNavigateToMatching: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.userMessage) {
        uiState.userMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearUserMessage()
        }
    }

    val configuration = LocalConfiguration.current
    val gridColumns = if (configuration.screenWidthDp > 600) 2 else 1

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "FindIt Campus",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Smart Lost & Found Hub",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.recalculateSmartMatches() }) {
                        Icon(
                            imageVector = Icons.Rounded.AutoAwesome,
                            contentDescription = "Trigger Smart Matching",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = { viewModel.resetSampleData() }) {
                        Icon(
                            imageVector = Icons.Rounded.Refresh,
                            contentDescription = "Reset Sample Data"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (uiState.isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            // Scrollable / Adaptive Grid Content
            LazyVerticalGrid(
                columns = GridCells.Fixed(gridColumns),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                // Section 1: Dashboard Stats Header
                item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(gridColumns) }) {
                    Column {
                        // Stats Grid Row 1
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            StatCard(
                                title = "Lost Items",
                                count = uiState.stats.totalLost,
                                icon = Icons.Rounded.Help,
                                containerColor = ColorLostBadge.copy(alpha = 0.12f),
                                contentColor = ColorLostBadge,
                                modifier = Modifier.weight(1f)
                            )
                            StatCard(
                                title = "Found Items",
                                count = uiState.stats.totalFound,
                                icon = Icons.Rounded.FindInPage,
                                containerColor = ColorFoundBadge.copy(alpha = 0.12f),
                                contentColor = ColorFoundBadge,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Stats Grid Row 2
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            StatCard(
                                title = "Returned / Claimed",
                                count = uiState.stats.totalResolved,
                                icon = Icons.Rounded.CheckCircle,
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.weight(1f)
                            )
                            StatCard(
                                title = "Smart Matches",
                                count = uiState.stats.activeMatchesCount,
                                icon = Icons.Rounded.Psychology,
                                containerColor = ColorHighMatch.copy(alpha = 0.12f),
                                contentColor = ColorHighMatch,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // Section 2: Real-time Search Input
                item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(gridColumns) }) {
                    OutlinedTextField(
                        value = uiState.searchQuery,
                        onValueChange = viewModel::onSearchQueryChanged,
                        placeholder = { Text("Search lost & found items by title or location...") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.Search,
                                contentDescription = "Search"
                            )
                        },
                        trailingIcon = {
                            if (uiState.searchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
                                    Icon(
                                        imageVector = Icons.Rounded.Clear,
                                        contentDescription = "Clear search"
                                    )
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Section 3: Type Filter Chips (All, Lost, Found)
                item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(gridColumns) }) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FilterChip(
                            selected = uiState.selectedType == null,
                            onClick = { viewModel.onTypeSelected(null) },
                            label = { Text("All Types") }
                        )
                        FilterChip(
                            selected = uiState.selectedType == LostFoundItem.TYPE_LOST,
                            onClick = {
                                if (uiState.selectedType == LostFoundItem.TYPE_LOST) viewModel.onTypeSelected(null)
                                else viewModel.onTypeSelected(LostFoundItem.TYPE_LOST)
                            },
                            label = { Text("Lost Only") }
                        )
                        FilterChip(
                            selected = uiState.selectedType == LostFoundItem.TYPE_FOUND,
                            onClick = {
                                if (uiState.selectedType == LostFoundItem.TYPE_FOUND) viewModel.onTypeSelected(null)
                                else viewModel.onTypeSelected(LostFoundItem.TYPE_FOUND)
                            },
                            label = { Text("Found Only") }
                        )

                        if (uiState.selectedCategory != null || uiState.selectedType != null || uiState.searchQuery.isNotEmpty()) {
                            Spacer(modifier = Modifier.weight(1f))
                            TextButton(onClick = viewModel::clearFilters) {
                                Text("Clear Filters")
                            }
                        }
                    }
                }

                // Section 4: Category Filter Chips Row
                item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(gridColumns) }) {
                    CategoryFilterRow(
                        selectedCategory = uiState.selectedCategory,
                        onCategorySelected = viewModel::onCategorySelected
                    )
                }

                // Section 5: Item Cards List or Empty State
                if (uiState.items.isEmpty() && !uiState.isLoading) {
                    item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(gridColumns) }) {
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.FilterList,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.outline,
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "No listings found matching your search",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Try clearing filters or report a new item.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                OutlinedButton(
                                    onClick = onNavigateToReport,
                                    shape = CircleShape
                                ) {
                                    Text("Report Lost / Found Item")
                                }
                            }
                        }
                    }
                } else {
                    items(
                        items = uiState.items,
                        key = { it.id }
                    ) { item ->
                        ItemCard(
                            item = item,
                            onItemClick = { onItemClick(item.id) }
                        )
                    }
                }
            }
        }
    }
}
