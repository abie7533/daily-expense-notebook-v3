package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Expense
import com.example.ui.ExpenseViewModel
import com.example.ui.Translation
import com.example.ui.components.notebookPaper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: ExpenseViewModel,
    onAddExpenseClick: () -> Unit
) {
    val lang by viewModel.language.collectAsState()
    val currency by viewModel.currency.collectAsState()
    val allExpenses by viewModel.allExpenses.collectAsState()
    val searchedExpenses by viewModel.searchedExpenses.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    val todayTotal by viewModel.todayTotal.collectAsState()
    val thisMonthTotal by viewModel.thisMonthTotal.collectAsState()

    val colorScheme = MaterialTheme.colorScheme

    // Dialog deletion confirmation state
    var expenseToDelete by remember { mutableStateOf<Expense?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
    ) {
        // Upper Notebook Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                .background(colorScheme.surface)
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            // App Name & Subtitle as requested by user's design layout
            Column {
                Text(
                    text = Translation.get("app_name", lang),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.primary
                )
                Text(
                    text = "Daily Expense Notebook",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.onSurface.copy(alpha = 0.5f),
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Stat Cards Layout
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // This Month's Total Card (Full-width col-span-2 equivalent)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("month_total_card"),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = colorScheme.primaryContainer),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = Translation.get("month_total", lang),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = Translation.formatAmount(thisMonthTotal, currency, lang),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Black,
                            color = colorScheme.onPrimaryContainer,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Two side-by-side cards (Today's Total & Total Entries)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Today's Total Card
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .testTag("today_total_card"),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = colorScheme.surfaceVariant),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = Translation.get("today_total", lang),
                                style = MaterialTheme.typography.labelSmall,
                                color = colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = Translation.formatAmount(todayTotal, currency, lang),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = colorScheme.primary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    // Total Entries Card
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .testTag("total_entries_card"),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = colorScheme.surfaceVariant),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = Translation.get("total_entries", lang),
                                style = MaterialTheme.typography.labelSmall,
                                color = colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            val entryUnit = if (lang == "bn") " টি" else " entries"
                            Text(
                                text = "${Translation.formatNumber(allExpenses.size, lang)}$entryUnit",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = colorScheme.primary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Minimal Notebook Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.searchQuery.value = it },
                placeholder = {
                    Text(
                        text = Translation.get("search_hint", lang),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = colorScheme.primary
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.searchQuery.value = "" }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("search_bar"),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorScheme.primary.copy(alpha = 0.6f),
                    unfocusedBorderColor = colorScheme.onBackground.copy(alpha = 0.1f),
                    focusedContainerColor = colorScheme.background,
                    unfocusedContainerColor = colorScheme.background
                ),
                singleLine = true
            )
        }

        // Ruled Paper Notebook Content Section
        if (searchedExpenses.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .notebookPaper(
                        lineColor = colorScheme.secondary.copy(alpha = 0.15f),
                        marginColor = colorScheme.tertiary.copy(alpha = 0.15f),
                        enabled = lang == "bn"
                    )
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(top = 40.dp)
                ) {
                    Icon(
                        imageVector = if (searchQuery.isEmpty()) Icons.Default.EditNote else Icons.Default.SearchOff,
                        contentDescription = "Empty Notebook",
                        tint = colorScheme.primary.copy(alpha = 0.4f),
                        modifier = Modifier.size(72.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = if (searchQuery.isEmpty()) Translation.get("no_expenses", lang) else Translation.get("no_expenses_search", lang),
                        style = MaterialTheme.typography.bodyLarge,
                        color = colorScheme.onBackground.copy(alpha = 0.6f),
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp,
                        modifier = Modifier.widthIn(max = 280.dp)
                    )
                }
            }
        } else {
            // Group expenses by date (Chronological/Reverse Chronological grouping)
            val groupedExpenses = searchedExpenses.groupBy { it.date }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .notebookPaper(
                        lineColor = colorScheme.secondary.copy(alpha = 0.15f),
                        marginColor = colorScheme.tertiary.copy(alpha = 0.15f),
                        enabled = lang == "bn"
                    )
                    .testTag("expenses_list"),
                contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp, start = 48.dp, end = 16.dp), // Leaves 48dp on start for margin!
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                groupedExpenses.forEach { (date, items) ->
                    val dayTotal = items.sumOf { it.amount }
                    
                    item(key = date) {
                        NotebookDayHeader(date = date, lang = lang)
                    }

                    items(items, key = { it.id }) { expense ->
                        NotebookExpenseItem(
                            expense = expense,
                            currency = currency,
                            lang = lang,
                            onDeleteClick = { expenseToDelete = expense }
                        )
                    }

                    item(key = "total_$date") {
                        NotebookDayTotal(
                            total = dayTotal,
                            currency = currency,
                            lang = lang
                        )
                    }
                }
            }
        }
    }

    // Deletion confirmation dialog
    if (expenseToDelete != null) {
        AlertDialog(
            onDismissRequest = { expenseToDelete = null },
            title = {
                Text(
                    text = Translation.get("delete", lang),
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.error
                )
            },
            text = {
                Text(Translation.get("delete_confirm", lang))
            },
            confirmButton = {
                Button(
                    onClick = {
                        expenseToDelete?.let { viewModel.deleteExpense(it) }
                        expenseToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = colorScheme.error)
                ) {
                    Text(Translation.get("delete", lang), color = colorScheme.onError)
                }
            },
            dismissButton = {
                TextButton(onClick = { expenseToDelete = null }) {
                    Text(Translation.get("cancel", lang))
                }
            },
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@Composable
fun NotebookDayHeader(
    date: String,
    lang: String
) {
    val colorScheme = MaterialTheme.colorScheme
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = Translation.formatDate(date, lang),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold,
            color = colorScheme.tertiary,
            fontSize = 17.sp
        )
        Spacer(modifier = Modifier.height(2.dp))
        // Dotted/Dashed ruled line divider
        Text(
            text = "--------------------------------------------------------",
            color = colorScheme.secondary.copy(alpha = 0.5f),
            maxLines = 1,
            overflow = TextOverflow.Clip,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Light,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun NotebookExpenseItem(
    expense: Expense,
    currency: String,
    lang: String,
    onDeleteClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    val catInfo = getCategoryInfo(expense.category)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .testTag("expense_item_${expense.id}"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Small category icon bullet
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(catInfo.color.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = catInfo.icon,
                contentDescription = expense.category,
                tint = catInfo.color,
                modifier = Modifier.size(14.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Title and optional note
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = expense.title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = colorScheme.onBackground
            )
            if (!expense.note.isNullOrEmpty()) {
                Text(
                    text = expense.note,
                    style = MaterialTheme.typography.bodySmall,
                    color = colorScheme.onBackground.copy(alpha = 0.5f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        // Amount
        Text(
            text = Translation.formatAmount(expense.amount, currency, lang),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.ExtraBold,
            color = colorScheme.primary,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        // Delete pen-erasure button (elegant trash bin)
        IconButton(
            onClick = onDeleteClick,
            modifier = Modifier
                .size(36.dp)
                .testTag("delete_expense_${expense.id}")
        ) {
            Icon(
                imageVector = Icons.Default.DeleteOutline,
                contentDescription = "Delete",
                tint = colorScheme.error.copy(alpha = 0.5f),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun NotebookDayTotal(
    total: Double,
    currency: String,
    lang: String
) {
    val colorScheme = MaterialTheme.colorScheme
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 12.dp),
        horizontalAlignment = Alignment.End
    ) {
        // Divider line
        Text(
            text = "........................................................................",
            color = colorScheme.secondary.copy(alpha = 0.3f),
            maxLines = 1,
            overflow = TextOverflow.Clip,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Light,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(4.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (lang == "bn") "মোট: " else "Total: ",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = colorScheme.onBackground.copy(alpha = 0.7f)
            )
            Text(
                text = Translation.formatAmount(total, currency, lang),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = colorScheme.tertiary
            )
        }
    }
}
