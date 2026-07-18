package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.ExpenseViewModel
import com.example.ui.Translation
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: ExpenseViewModel = viewModel()
            
            val themeMode by viewModel.themeMode.collectAsState()
            val isDarkTheme = when (themeMode) {
                "light" -> false
                "dark" -> true
                else -> isSystemInDarkTheme()
            }

            MyApplicationTheme(darkTheme = isDarkTheme) {
                val isAppLocked by viewModel.isAppLocked.collectAsState()

                if (isAppLocked) {
                    PinLockScreen(
                        viewModel = viewModel,
                        onUnlockSuccess = {
                            // PIN correctly verified
                        }
                    )
                } else {
                    MainNotebookShell(viewModel = viewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MainNotebookShell(viewModel: ExpenseViewModel) {
    val lang by viewModel.language.collectAsState()
    val currentTab by viewModel.currentTab.collectAsState()
    
    var showAddDialog by remember { mutableStateOf(false) }
    
    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("bottom_nav_bar")
                    .windowInsetsPadding(WindowInsets.navigationBars),
                containerColor = colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                val tabs = listOf(
                    Triple(0, Translation.get("dashboard", lang), Icons.Default.Book),
                    Triple(1, Translation.get("calendar", lang), Icons.Default.CalendarMonth),
                    Triple(2, Translation.get("reports", lang), Icons.Default.InsertChart),
                    Triple(3, Translation.get("settings", lang), Icons.Default.Settings)
                )

                tabs.forEach { (index, title, icon) ->
                    NavigationBarItem(
                        selected = currentTab == index,
                        onClick = { viewModel.currentTab.value = index },
                        icon = { Icon(imageVector = icon, contentDescription = title) },
                        label = {
                            Text(
                                text = title,
                                fontWeight = if (currentTab == index) FontWeight.Bold else FontWeight.Medium,
                                maxLines = 1
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = colorScheme.primary,
                            selectedTextColor = colorScheme.primary,
                            indicatorColor = colorScheme.primary.copy(alpha = 0.12f),
                            unselectedIconColor = colorScheme.onSurface.copy(alpha = 0.6f),
                            unselectedTextColor = colorScheme.onSurface.copy(alpha = 0.6f)
                        ),
                        modifier = Modifier.testTag("nav_tab_$index")
                    )
                }
            }
        },
        floatingActionButton = {
            // Only show floating button on notebook dashboard and calendar views
            if (currentTab == 0 || currentTab == 1) {
                FloatingActionButton(
                    onClick = { showAddDialog = true },
                    containerColor = colorScheme.tertiary,
                    contentColor = Color.White,
                    shape = CircleShape,
                    modifier = Modifier
                        .padding(bottom = 12.dp, end = 12.dp)
                        .testTag("fab_add_expense"),
                    elevation = FloatingActionButtonDefaults.elevation(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = Translation.get("add_expense", lang),
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Screen switching with smooth crossfade animations
            Crossfade(
                targetState = currentTab,
                animationSpec = tween(durationMillis = 300),
                label = "ScreenCrossfade"
            ) { tabIndex ->
                when (tabIndex) {
                    0 -> DashboardScreen(
                        viewModel = viewModel,
                        onAddExpenseClick = { showAddDialog = true }
                    )
                    1 -> CalendarScreen(viewModel = viewModel)
                    2 -> ReportsScreen(viewModel = viewModel)
                    3 -> SettingsScreen(viewModel = viewModel)
                }
            }
        }
    }

    // Add Expense dialog trigger
    if (showAddDialog) {
        AddExpenseDialog(
            viewModel = viewModel,
            onDismiss = { showAddDialog = false }
        )
    }
}
