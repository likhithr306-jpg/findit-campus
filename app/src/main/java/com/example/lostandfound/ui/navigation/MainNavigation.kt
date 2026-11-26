package com.example.lostandfound.ui.navigation

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Assignment
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lostandfound.data.preferences.UserPreferencesRepository
import com.example.lostandfound.data.repository.ItemRepository
import com.example.lostandfound.ui.screens.DashboardScreen
import com.example.lostandfound.ui.screens.ItemDetailScreen
import com.example.lostandfound.ui.screens.MatchingScreen
import com.example.lostandfound.ui.screens.MyReportsScreen
import com.example.lostandfound.ui.screens.ReportFormScreen
import com.example.lostandfound.ui.viewmodel.DashboardViewModel
import com.example.lostandfound.ui.viewmodel.ItemDetailViewModel
import com.example.lostandfound.ui.viewmodel.MatchingViewModel
import com.example.lostandfound.ui.viewmodel.MyReportsViewModel
import com.example.lostandfound.ui.viewmodel.ReportViewModel

sealed class ScreenRoute {
    data object Dashboard : ScreenRoute()
    data object Report : ScreenRoute()
    data object Matching : ScreenRoute()
    data object MyReports : ScreenRoute()
    data class ItemDetail(val itemId: Long) : ScreenRoute()
}

data class BottomNavItem(
    val route: ScreenRoute,
    val title: String,
    val icon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem(ScreenRoute.Dashboard, "Dashboard", Icons.Rounded.Dashboard),
    BottomNavItem(ScreenRoute.Report, "Report", Icons.Rounded.AddCircle),
    BottomNavItem(ScreenRoute.Matching, "Smart Match", Icons.Rounded.AutoAwesome),
    BottomNavItem(ScreenRoute.MyReports, "My Reports", Icons.Rounded.Assignment)
)

@Composable
fun MainNavigation(
    repository: ItemRepository,
    preferencesRepository: UserPreferencesRepository,
    modifier: Modifier = Modifier
) {
    var currentRoute by remember { mutableStateOf<ScreenRoute>(ScreenRoute.Dashboard) }

    val dashboardViewModel: DashboardViewModel = viewModel(
        factory = DashboardViewModel.Factory(repository, preferencesRepository)
    )
    val reportViewModel: ReportViewModel = viewModel(
        factory = ReportViewModel.Factory(repository, preferencesRepository)
    )
    val matchingViewModel: MatchingViewModel = viewModel(
        factory = MatchingViewModel.Factory(repository)
    )
    val myReportsViewModel: MyReportsViewModel = viewModel(
        factory = MyReportsViewModel.Factory(repository, preferencesRepository)
    )

    val isTopLevelScreen = currentRoute is ScreenRoute.Dashboard ||
            currentRoute is ScreenRoute.Report ||
            currentRoute is ScreenRoute.Matching ||
            currentRoute is ScreenRoute.MyReports

    Scaffold(
        bottomBar = {
            if (isTopLevelScreen) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        val isSelected = currentRoute == item.route
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { currentRoute = item.route },
                            icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                            label = { Text(item.title) }
                        )
                    }
                }
            }
        },
        modifier = modifier
    ) { innerPadding ->
        Crossfade(
            targetState = currentRoute,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) { route ->
            when (route) {
                is ScreenRoute.Dashboard -> {
                    DashboardScreen(
                        viewModel = dashboardViewModel,
                        onItemClick = { id -> currentRoute = ScreenRoute.ItemDetail(id) },
                        onNavigateToReport = { currentRoute = ScreenRoute.Report },
                        onNavigateToMatching = { currentRoute = ScreenRoute.Matching }
                    )
                }

                is ScreenRoute.Report -> {
                    ReportFormScreen(
                        viewModel = reportViewModel,
                        onReportSubmitted = { newId -> currentRoute = ScreenRoute.ItemDetail(newId) }
                    )
                }

                is ScreenRoute.Matching -> {
                    MatchingScreen(
                        viewModel = matchingViewModel,
                        onViewItemClick = { id -> currentRoute = ScreenRoute.ItemDetail(id) }
                    )
                }

                is ScreenRoute.MyReports -> {
                    MyReportsScreen(
                        viewModel = myReportsViewModel,
                        onItemClick = { id -> currentRoute = ScreenRoute.ItemDetail(id) },
                        onNavigateToReport = { currentRoute = ScreenRoute.Report }
                    )
                }

                is ScreenRoute.ItemDetail -> {
                    val detailViewModel: ItemDetailViewModel = viewModel(
                        key = "item_detail_${route.itemId}",
                        factory = ItemDetailViewModel.Factory(repository, route.itemId)
                    )

                    ItemDetailScreen(
                        viewModel = detailViewModel,
                        onBackClick = { currentRoute = ScreenRoute.Dashboard },
                        onViewOtherItemClick = { otherId -> currentRoute = ScreenRoute.ItemDetail(otherId) }
                    )
                }
            }
        }
    }
}
