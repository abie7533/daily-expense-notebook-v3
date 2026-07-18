package com.example.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
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
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CalendarScreen(viewModel: ExpenseViewModel) {
    val lang by viewModel.language.collectAsState()
    val currency by viewModel.currency.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val calendarExpenses by viewModel.calendarExpenses.collectAsState()
    
    val context = LocalContext.current
    val colorScheme = MaterialTheme.colorScheme

    var expenseToDelete by remember { mutableStateOf<Expense?>(null) }

    // Generate last 7 days and next 7 days for horizontal selection
    val calendarList = remember {
        val list = mutableListOf<String>()
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -10)
        repeat(21) {
            list.add(sdf.format(cal.time))
            cal.add(Calendar.DAY_OF_YEAR, 1)
        }
        list
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
    ) {
        // Upper Date Picker Row Card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                .background(colorScheme.surface)
                .padding(vertical = 20.dp, horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = Translation.get("calendar", lang),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.primary
                )

                // DatePicker Fab Button
                IconButton(
                    onClick = {
                        val calendar = Calendar.getInstance()
                        val parts = selectedDate.split("-")
                        if (parts.size == 3) {
                            calendar.set(Calendar.YEAR, parts[0].toInt())
                            calendar.set(Calendar.MONTH, parts[1].toInt() - 1)
                            calendar.set(Calendar.DAY_OF_MONTH, parts[2].toInt())
                        }

                        DatePickerDialog(
                            context,
                            { _, year, monthOfYear, dayOfMonth ->
                                val newDateStr = String.format(
                                    Locale.ENGLISH,
                                    "%04d-%02d-%02d",
                                    year,
                                    monthOfYear + 1,
                                    dayOfMonth
                                )
                                viewModel.selectedDate.value = newDateStr
                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    },
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(colorScheme.primary.copy(alpha = 0.1f))
                        .testTag("calendar_datepicker_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = "Pick Date",
                        tint = colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Horizontally Scrollable Calendar Tape List
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                items(calendarList) { dateStr ->
                    val isSelected = selectedDate == dateStr
                    val parts = dateStr.split("-")
                    val dayNum = parts.getOrNull(2)?.toInt() ?: 1
                    
                    // Format weekday name
                    val sdfIn = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                    val parsedDate = sdfIn.parse(dateStr)
                    val weekdaySdf = SimpleDateFormat("EEE", Locale.ENGLISH)
                    val weekdayEn = weekdaySdf.format(parsedDate ?: Date())
                    val weekdayDisplay = getWeekdayTranslation(weekdayEn, lang)

                    Card(
                        modifier = Modifier
                            .width(56.dp)
                            .height(84.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { viewModel.selectedDate.value = dateStr }
                            .testTag("calendar_strip_$dateStr"),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) colorScheme.primary else colorScheme.background
                        ),
                        border = if (isSelected) null else BorderStroke(1.dp, colorScheme.onBackground.copy(alpha = 0.1f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 0.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = weekdayDisplay,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Medium,
                                color = if (isSelected) colorScheme.onPrimary.copy(alpha = 0.8f) else colorScheme.onBackground.copy(alpha = 0.5f),
                                fontSize = 11.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = Translation.formatNumber(dayNum, lang),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) colorScheme.onPrimary else colorScheme.onBackground
                            )
                        }
                    }
                }
            }
        }

        // Selected Date Label Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = Translation.formatDate(selectedDate, lang),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = colorScheme.tertiary
            )
        }

        // Ruled Notebook Expenses for Selected Date
        if (calendarExpenses.isEmpty()) {
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
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.EventNote,
                        contentDescription = "No Entries Today",
                        tint = colorScheme.primary.copy(alpha = 0.3f),
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = Translation.get("no_expenses_date", lang),
                        style = MaterialTheme.typography.bodyLarge,
                        color = colorScheme.onBackground.copy(alpha = 0.6f),
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp,
                        modifier = Modifier.widthIn(max = 240.dp)
                    )
                }
            }
        } else {
            val totalAmt = calendarExpenses.sumOf { it.amount }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .notebookPaper(
                        lineColor = colorScheme.secondary.copy(alpha = 0.15f),
                        marginColor = colorScheme.tertiary.copy(alpha = 0.15f),
                        enabled = lang == "bn"
                    )
                    .testTag("calendar_expenses_list"),
                contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp, start = 48.dp, end = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(calendarExpenses, key = { it.id }) { expense ->
                    NotebookExpenseItem(
                        expense = expense,
                        currency = currency,
                        lang = lang,
                        onDeleteClick = { expenseToDelete = expense }
                    )
                }

                item(key = "day_total") {
                    NotebookDayTotal(
                        total = totalAmt,
                        currency = currency,
                        lang = lang
                    )
                }
            }
        }
    }

    // Deletion Dialog
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
            text = { Text(Translation.get("delete_confirm", lang)) },
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

// Map short English weekdays to Bengali
private fun getWeekdayTranslation(day: String, lang: String): String {
    if (lang != "bn") return day
    return when (day) {
        "Sat" -> "শনি"
        "Sun" -> "রবি"
        "Mon" -> "সোম"
        "Tue" -> "মঙ্গল"
        "Wed" -> "বুধ"
        "Thu" -> "বৃহ"
        "Fri" -> "শুক্র"
        else -> day
    }
}
