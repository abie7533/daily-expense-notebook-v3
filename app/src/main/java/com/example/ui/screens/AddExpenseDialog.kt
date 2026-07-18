package com.example.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.ExpenseViewModel
import com.example.ui.Translation
import com.example.ui.components.notebookPaper
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseDialog(
    viewModel: ExpenseViewModel,
    onDismiss: () -> Unit
) {
    val lang by viewModel.language.collectAsState()
    val context = LocalContext.current
    
    var amount by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    
    val categories = listOf("Food", "Transport", "Shopping", "Bills", "Education", "Medical", "Entertainment", "Travel", "Others")
    var selectedCategory by remember { mutableStateOf("Food") }
    
    var selectedDateStr by remember { mutableStateOf(viewModel.getCurrentDateString()) }

    var amountError by remember { mutableStateOf(false) }
    var titleError by remember { mutableStateOf(false) }

    val colorScheme = MaterialTheme.colorScheme

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
                .padding(16.dp)
                .testTag("add_expense_card"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = colorScheme.background),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .notebookPaper(
                        lineColor = colorScheme.secondary.copy(alpha = 0.15f),
                        marginColor = colorScheme.tertiary.copy(alpha = 0.15f),
                        enabled = lang == "bn"
                    )
                    .padding(24.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = Translation.get("add_expense", lang),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.primary
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_add_dialog")
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Amount Field
                    OutlinedTextField(
                        value = amount,
                        onValueChange = {
                            amount = it
                            amountError = false
                        },
                        label = { Text(Translation.get("amount", lang)) },
                        placeholder = { Text(if (viewModel.currency.value == "৳") "৳০.০০" else "$0.00") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        isError = amountError,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("amount_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colorScheme.primary,
                            unfocusedBorderColor = colorScheme.onBackground.copy(alpha = 0.3f)
                        ),
                        supportingText = {
                            if (amountError) {
                                Text(Translation.get("amount_req", lang), color = colorScheme.error)
                            }
                        }
                    )

                    // Title Field
                    OutlinedTextField(
                        value = title,
                        onValueChange = {
                            title = it
                            titleError = false
                        },
                        label = { Text(Translation.get("title", lang)) },
                        singleLine = true,
                        isError = titleError,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("title_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colorScheme.primary,
                            unfocusedBorderColor = colorScheme.onBackground.copy(alpha = 0.3f)
                        ),
                        supportingText = {
                            if (titleError) {
                                Text(Translation.get("title_req", lang), color = colorScheme.error)
                            }
                        }
                    )

                    // Note Field
                    OutlinedTextField(
                        value = note,
                        onValueChange = { note = it },
                        label = { Text(Translation.get("note", lang)) },
                        maxLines = 3,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("note_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colorScheme.primary,
                            unfocusedBorderColor = colorScheme.onBackground.copy(alpha = 0.3f)
                        )
                    )

                    // Date Selector
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(colorScheme.surface)
                            .clickable {
                                val calendar = Calendar.getInstance()
                                val parts = selectedDateStr.split("-")
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
                                        selectedDateStr = newDateStr
                                    },
                                    calendar.get(Calendar.YEAR),
                                    calendar.get(Calendar.MONTH),
                                    calendar.get(Calendar.DAY_OF_MONTH)
                                ).show()
                            }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = "Date Icon",
                                tint = colorScheme.primary,
                                modifier = Modifier.padding(end = 12.dp)
                            )
                            Text(
                                text = Translation.get("date", lang),
                                fontWeight = FontWeight.Medium,
                                color = colorScheme.onBackground
                            )
                        }
                        Text(
                            text = Translation.formatDate(selectedDateStr, lang),
                            fontWeight = FontWeight.Bold,
                            color = colorScheme.primary
                        )
                    }

                    // Category Selector Label
                    Text(
                        text = Translation.get("category", lang),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.primary,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    // Tactile Category Icon Selector
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val chunks = categories.chunked(3)
                        chunks.forEach { rowCategories ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                rowCategories.forEach { cat ->
                                    val isSelected = selectedCategory == cat
                                    val catInfo = getCategoryInfo(cat)
                                    
                                    Card(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(84.dp)
                                            .clip(RoundedCornerShape(16.dp))
                                            .clickable { selectedCategory = cat }
                                            .testTag("cat_selector_$cat"),
                                        colors = CardDefaults.cardColors(
                                            containerColor = if (isSelected) colorScheme.primary else colorScheme.surface
                                        ),
                                        border = if (isSelected) null else BorderStroke(1.dp, colorScheme.onBackground.copy(alpha = 0.1f)),
                                        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 0.dp)
                                    ) {
                                        Column(
                                            modifier = Modifier.fillMaxSize(),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(36.dp)
                                                    .clip(CircleShape)
                                                    .background(
                                                        if (isSelected) colorScheme.onPrimary.copy(alpha = 0.2f) else catInfo.color.copy(alpha = 0.15f)
                                                    ),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = catInfo.icon,
                                                    contentDescription = cat,
                                                    tint = if (isSelected) colorScheme.onPrimary else catInfo.color,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = Translation.get(cat, lang),
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.SemiBold,
                                                color = if (isSelected) colorScheme.onPrimary else colorScheme.onBackground,
                                                fontSize = 11.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("cancel_add_expense"),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, colorScheme.onBackground.copy(alpha = 0.3f))
                    ) {
                        Text(
                            text = Translation.get("cancel", lang),
                            color = colorScheme.onBackground,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Button(
                        onClick = {
                            var valid = true
                            val amtVal = amount.toDoubleOrNull()
                            if (amtVal == null || amtVal <= 0) {
                                amountError = true
                                valid = false
                            }
                            if (title.isBlank()) {
                                titleError = true
                                valid = false
                            }

                            if (valid && amtVal != null) {
                                viewModel.addExpense(
                                    amount = amtVal,
                                    title = title,
                                    note = note.takeIf { it.isNotEmpty() },
                                    category = selectedCategory,
                                    date = selectedDateStr
                                )
                                onDismiss()
                            }
                        },
                        modifier = Modifier
                            .weight(1.5f)
                            .height(48.dp)
                            .testTag("save_expense_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary)
                    ) {
                        Text(
                            text = Translation.get("save", lang),
                            color = colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

// Category helper structure
data class CategoryInfo(
    val icon: ImageVector,
    val color: Color
)

@Composable
fun getCategoryInfo(category: String): CategoryInfo {
    return when (category) {
        "Food" -> CategoryInfo(Icons.Default.Restaurant, Color(0xFF4CAF50))
        "Transport" -> CategoryInfo(Icons.Default.DirectionsBus, Color(0xFF2196F3))
        "Shopping" -> CategoryInfo(Icons.Default.LocalMall, Color(0xFFFF9800))
        "Bills" -> CategoryInfo(Icons.Default.Receipt, Color(0xFFE91E63))
        "Education" -> CategoryInfo(Icons.Default.School, Color(0xFF9C27B0))
        "Medical" -> CategoryInfo(Icons.Default.MedicalServices, Color(0xFFF44336))
        "Entertainment" -> CategoryInfo(Icons.Default.Celebration, Color(0xFFE040FB))
        "Travel" -> CategoryInfo(Icons.Default.Flight, Color(0xFF00BCD4))
        else -> CategoryInfo(Icons.Default.Category, Color(0xFF757575))
    }
}
