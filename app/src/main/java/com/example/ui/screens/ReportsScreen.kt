package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.ExpenseViewModel
import com.example.ui.Translation
import com.example.ui.components.notebookPaper

@Composable
fun ReportsScreen(viewModel: ExpenseViewModel) {
    val lang by viewModel.language.collectAsState()
    val currency by viewModel.currency.collectAsState()
    val stats by viewModel.monthlyStats.collectAsState()

    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
    ) {
        // Upper Title Header Card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                .background(colorScheme.surface)
                .padding(vertical = 20.dp, horizontal = 24.dp)
        ) {
            Text(
                text = Translation.get("reports", lang),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            // Large Monthly Total display card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("report_monthly_total_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = colorScheme.primary)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = Translation.get("month_total", lang),
                            style = MaterialTheme.typography.labelMedium,
                            color = colorScheme.onPrimary.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = Translation.formatAmount(stats.totalAmount, currency, lang),
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = colorScheme.onPrimary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(colorScheme.onPrimary.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Analytics,
                            contentDescription = "Analytics",
                            tint = colorScheme.onPrimary,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        }

        // Ruled Notebook Stats Page
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .notebookPaper(
                    lineColor = colorScheme.secondary.copy(alpha = 0.15f),
                    marginColor = colorScheme.tertiary.copy(alpha = 0.15f),
                    enabled = lang == "bn"
                )
                .verticalScroll(rememberScrollState())
                .padding(top = 16.dp, bottom = 80.dp, start = 48.dp, end = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // General Counters Group
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Entries count
                StatCard(
                    title = Translation.get("total_entries", lang),
                    value = Translation.formatNumber(stats.totalEntries, lang),
                    icon = Icons.Default.FormatListBulleted,
                    modifier = Modifier.weight(1f)
                )
            }

            // Highest & Lowest Days Highlight Sheets
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Highest spending day card
                HighlightDayCard(
                    title = Translation.get("highest_day", lang),
                    dateStr = stats.highestDayDate,
                    amount = stats.highestDayAmount,
                    currency = currency,
                    lang = lang,
                    icon = Icons.Default.TrendingUp,
                    accentColor = colorScheme.tertiary,
                    modifier = Modifier.fillMaxWidth()
                )

                // Lowest spending day card
                HighlightDayCard(
                    title = Translation.get("lowest_day", lang),
                    dateStr = stats.lowestDayDate,
                    amount = stats.lowestDayAmount,
                    currency = currency,
                    lang = lang,
                    icon = Icons.Default.TrendingDown,
                    accentColor = Color(0xFF4CAF50),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Category Breakdown Chart Section
            if (stats.categoryBreakdown.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Text(
                        text = if (lang == "bn") "ক্যাটাগরি অনুযায়ী খরচ" else "Category Breakdown",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.primary,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    stats.categoryBreakdown.entries
                        .sortedByDescending { it.value }
                        .forEach { (cat, amt) ->
                            val percent = if (stats.totalAmount > 0) (amt / stats.totalAmount) else 0.0
                            CategoryRatioBar(
                                category = cat,
                                amount = amt,
                                currency = currency,
                                percent = percent,
                                lang = lang
                            )
                        }
                }
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall,
                    color = colorScheme.onBackground.copy(alpha = 0.5f)
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.onBackground
                )
            }
        }
    }
}

@Composable
fun HighlightDayCard(
    title: String,
    dateStr: String,
    amount: Double,
    currency: String,
    lang: String,
    icon: ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                    Text(
                        text = if (dateStr.isNotEmpty()) Translation.formatDate(dateStr, lang) else "N/A",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorScheme.onBackground,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Text(
                text = if (amount > 0) Translation.formatAmount(amount, currency, lang) else Translation.formatAmount(0.0, currency, lang),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = colorScheme.primary
            )
        }
    }
}

@Composable
fun CategoryRatioBar(
    category: String,
    amount: Double,
    currency: String,
    percent: Double,
    lang: String
) {
    val colorScheme = MaterialTheme.colorScheme
    val catInfo = getCategoryInfo(category)
    val pctString = Translation.formatNumber(String.format("%.0f", percent * 100).toInt(), lang) + "%"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(catInfo.color.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = catInfo.icon,
                        contentDescription = null,
                        tint = catInfo.color,
                        modifier = Modifier.size(14.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = Translation.get(category, lang),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.onBackground
                )
            }

            Row {
                Text(
                    text = Translation.formatAmount(amount, currency, lang),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "($pctString)",
                    style = MaterialTheme.typography.bodySmall,
                    color = colorScheme.onBackground.copy(alpha = 0.5f)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Custom M3 linear progress bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(CircleShape)
                .background(colorScheme.onBackground.copy(alpha = 0.08f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(percent.toFloat())
                    .clip(CircleShape)
                    .background(catInfo.color)
            )
        }
    }
}
