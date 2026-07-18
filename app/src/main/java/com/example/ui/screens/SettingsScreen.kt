package com.example.ui.screens

import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.ExpenseViewModel
import com.example.ui.Translation
import com.example.ui.components.notebookPaper
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(viewModel: ExpenseViewModel) {
    val lang by viewModel.language.collectAsState()
    val currency by viewModel.currency.collectAsState()
    val themeMode by viewModel.themeMode.collectAsState()
    val pinEnabled by viewModel.pinEnabled.collectAsState()
    val biometricEnabled by viewModel.biometricEnabled.collectAsState()

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val colorScheme = MaterialTheme.colorScheme

    // Dialog state for setting PIN
    var showPinDialog by remember { mutableStateOf(false) }
    // Dialog state for importing JSON
    var showImportDialog by remember { mutableStateOf(false) }
    // Confirm clear all state
    var showClearConfirm by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
    ) {
        // Upper Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                .background(colorScheme.surface)
                .padding(vertical = 20.dp, horizontal = 24.dp)
        ) {
            Text(
                text = Translation.get("settings", lang),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = colorScheme.primary
            )
        }

        // Ruled Paper Settings List
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Section: Preferences
            SettingsSectionHeader(title = if (lang == "bn") "সাধারণ কনফিগারেশন" else "General Customization", color = colorScheme.primary)

            // Language Toggle Option
            SettingsRow(
                title = Translation.get("language", lang),
                subtitle = if (lang == "bn") "বর্তমান: বাংলা (বাংলা খাতা)" else "Current: English (Notebook)",
                icon = Icons.Default.Language
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    FilterChip(
                        selected = lang == "bn",
                        onClick = { viewModel.setLanguage("bn") },
                        label = { Text("বাংলা", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        modifier = Modifier.testTag("lang_bn_chip")
                    )
                    FilterChip(
                        selected = lang == "en",
                        onClick = { viewModel.setLanguage("en") },
                        label = { Text("English", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        modifier = Modifier.testTag("lang_en_chip")
                    )
                }
            }

            // Currency Selector Option
            SettingsRow(
                title = Translation.get("currency", lang),
                subtitle = if (lang == "bn") "মুদ্রা প্রতীক" else "Active symbol",
                icon = Icons.Default.Paid
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    FilterChip(
                        selected = currency == "৳",
                        onClick = { viewModel.setCurrency("৳") },
                        label = { Text("BDT (৳)", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        modifier = Modifier.testTag("currency_bdt_chip")
                    )
                    FilterChip(
                        selected = currency == "$",
                        onClick = { viewModel.setCurrency("$") },
                        label = { Text("USD ($)", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        modifier = Modifier.testTag("currency_usd_chip")
                    )
                }
            }

            // Theme Selection Option
            SettingsRow(
                title = if (lang == "bn") "থিম মোড" else "Appearance",
                subtitle = when (themeMode) {
                    "light" -> Translation.get("light_mode", lang)
                    "dark" -> Translation.get("dark_mode", lang)
                    else -> Translation.get("system_mode", lang)
                },
                icon = Icons.Default.Palette
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    IconButton(
                        onClick = { viewModel.setThemeMode("light") },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (themeMode == "light") colorScheme.primary else colorScheme.surface)
                            .testTag("theme_light_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.LightMode,
                            contentDescription = "Light Mode",
                            tint = if (themeMode == "light") colorScheme.onPrimary else colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    IconButton(
                        onClick = { viewModel.setThemeMode("dark") },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (themeMode == "dark") colorScheme.primary else colorScheme.surface)
                            .testTag("theme_dark_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.DarkMode,
                            contentDescription = "Dark Mode",
                            tint = if (themeMode == "dark") colorScheme.onPrimary else colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    IconButton(
                        onClick = { viewModel.setThemeMode("system") },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (themeMode == "system") colorScheme.primary else colorScheme.surface)
                            .testTag("theme_system_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.SettingsSuggest,
                            contentDescription = "System Mode",
                            tint = if (themeMode == "system") colorScheme.onPrimary else colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            // Section: Security
            Spacer(modifier = Modifier.height(8.dp))
            SettingsSectionHeader(title = Translation.get("pin_lock", lang), color = colorScheme.primary)

            // PIN Lock Switch
            SettingsRow(
                title = Translation.get("enable_pin_toggle", lang),
                subtitle = if (pinEnabled) "৪-ডিজিট সিকিউরিটি পিন সক্রিয়" else "অ্যাপ খুলতে পিন লক ব্যবহার করুন",
                icon = Icons.Default.Security
            ) {
                Switch(
                    checked = pinEnabled,
                    onCheckedChange = { checked ->
                        if (checked) {
                            showPinDialog = true
                        } else {
                            viewModel.setPinLock(false, "")
                            Toast.makeText(context, Translation.get("pin_disabled_success", lang), Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.testTag("pin_switch")
                )
            }

            // Biometric Fingerprint Switch
            SettingsRow(
                title = Translation.get("use_biometric", lang),
                subtitle = Translation.get("biometric_desc", lang),
                icon = Icons.Default.Fingerprint
            ) {
                Switch(
                    checked = biometricEnabled,
                    onCheckedChange = { checked -> viewModel.setBiometric(checked) },
                    enabled = pinEnabled,
                    modifier = Modifier.testTag("biometric_switch")
                )
            }

            // Section: Backup & Restore
            Spacer(modifier = Modifier.height(8.dp))
            SettingsSectionHeader(title = Translation.get("backup", lang), color = colorScheme.primary)

            // Export Button
            SettingsRowButton(
                title = Translation.get("export_json", lang),
                subtitle = if (lang == "bn") "ব্যাকআপ ফাইল শেয়ার বা কপি করুন" else "Copy or share offline JSON dump",
                icon = Icons.Default.CloudUpload,
                tag = "export_backup_button"
            ) {
                val json = viewModel.exportBackupJson()
                
                // Copy to clipboard
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = android.content.ClipData.newPlainText("Notebook Expense Backup", json)
                clipboard.setPrimaryClip(clip)

                // Native Share sheet
                try {
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_SUBJECT, "Daily Expense Notebook Backup")
                        putExtra(Intent.EXTRA_TEXT, json)
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Save Notebook JSON Backup"))
                } catch (e: Exception) {
                    Toast.makeText(context, Translation.get("copied_to_clipboard", lang), Toast.LENGTH_LONG).show()
                }
            }

            // Import Button
            SettingsRowButton(
                title = Translation.get("import_json", lang),
                subtitle = if (lang == "bn") "পূর্বের JSON ডাটা খাতায় ফিরিয়ে আনুন" else "Paste JSON dump to load entries",
                icon = Icons.Default.CloudDownload,
                tag = "import_backup_button"
            ) {
                showImportDialog = true
            }

            // Section: Dangerous Area
            Spacer(modifier = Modifier.height(8.dp))
            SettingsSectionHeader(title = if (lang == "bn") "বিপদজনক এলাকা" else "Danger Zone", color = colorScheme.error)

            // Clear All Data
            SettingsRowButton(
                title = Translation.get("clear_all", lang),
                subtitle = if (lang == "bn") "খাতার সমস্ত খরচ চিরতরে মুছে ফেলুন" else "Erase all transactions from this device",
                icon = Icons.Default.DeleteForever,
                iconColor = colorScheme.error,
                tag = "clear_all_data_button"
            ) {
                showClearConfirm = true
            }

            // App Info Description
            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = colorScheme.secondary.copy(alpha = 0.2f))
            Text(
                text = Translation.get("app_info", lang),
                style = MaterialTheme.typography.bodySmall,
                color = colorScheme.onBackground.copy(alpha = 0.4f),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            )
        }
    }

    // Set PIN Dialog Form
    if (showPinDialog) {
        var pinInput by remember { mutableStateOf("") }
        var pinConfirmInput by remember { mutableStateOf("") }
        var errorMessage by remember { mutableStateOf("") }

        Dialog(onDismissRequest = { showPinDialog = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = colorScheme.background)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(imageVector = Icons.Default.LockOpen, contentDescription = null, tint = colorScheme.primary, modifier = Modifier.size(44.dp))
                    
                    Text(
                        text = Translation.get("pin_set", lang),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        color = colorScheme.primary
                    )

                    OutlinedTextField(
                        value = pinInput,
                        onValueChange = { if (it.length <= 4) pinInput = it },
                        label = { Text(Translation.get("pin_set", lang)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("pin_setup_input_1")
                    )

                    OutlinedTextField(
                        value = pinConfirmInput,
                        onValueChange = { if (it.length <= 4) pinConfirmInput = it },
                        label = { Text(Translation.get("pin_confirm", lang)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("pin_setup_input_2")
                    )

                    if (errorMessage.isNotEmpty()) {
                        Text(text = errorMessage, color = colorScheme.error, style = MaterialTheme.typography.bodySmall)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        TextButton(
                            onClick = { showPinDialog = false },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(Translation.get("cancel", lang))
                        }

                        Button(
                            onClick = {
                                if (pinInput.length != 4) {
                                    errorMessage = if (lang == "bn") "পিন অবশ্যই ৪ ডিজিটের হতে হবে!" else "PIN must be 4 digits!"
                                } else if (pinInput != pinConfirmInput) {
                                    errorMessage = Translation.get("pin_mismatch", lang)
                                } else {
                                    viewModel.setPinLock(true, pinInput)
                                    showPinDialog = false
                                    Toast.makeText(context, Translation.get("pin_set_success", lang), Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier.weight(1.5f).testTag("pin_setup_save")
                        ) {
                            Text(Translation.get("save", lang))
                        }
                    }
                }
            }
        }
    }

    // Import JSON Dialog Form
    if (showImportDialog) {
        var importInput by remember { mutableStateOf("") }
        var isImporting by remember { mutableStateOf(false) }

        Dialog(onDismissRequest = { showImportDialog = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = colorScheme.background)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(imageVector = Icons.Default.SettingsBackupRestore, contentDescription = null, tint = colorScheme.primary, modifier = Modifier.size(44.dp))
                    
                    Text(
                        text = Translation.get("import_json", lang),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        color = colorScheme.primary
                    )

                    OutlinedTextField(
                        value = importInput,
                        onValueChange = { importInput = it },
                        label = { Text(if (lang == "bn") "এখানে JSON ব্যাকআপ ডাটা পেস্ট করুন" else "Paste JSON string here") },
                        maxLines = 6,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .testTag("import_json_input")
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        TextButton(
                            onClick = { showImportDialog = false },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(Translation.get("cancel", lang))
                        }

                        Button(
                            onClick = {
                                if (importInput.isNotBlank()) {
                                    isImporting = true
                                    coroutineScope.launch {
                                        val success = viewModel.importBackupJson(importInput)
                                        isImporting = false
                                        if (success) {
                                            showImportDialog = false
                                            Toast.makeText(context, Translation.get("import_success", lang), Toast.LENGTH_LONG).show()
                                        } else {
                                            Toast.makeText(context, Translation.get("import_failed", lang), Toast.LENGTH_LONG).show()
                                        }
                                    }
                                }
                            },
                            modifier = Modifier.weight(1.5f).testTag("import_json_save_btn"),
                            enabled = !isImporting
                        ) {
                            Text(if (isImporting) "..." else Translation.get("save", lang))
                        }
                    }
                }
            }
        }
    }

    // Clear confirmation dialog
    if (showClearConfirm) {
        AlertDialog(
            onDismissRequest = { showClearConfirm = false },
            title = {
                Text(
                    text = Translation.get("clear_all", lang),
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.error
                )
            },
            text = { Text(Translation.get("clear_all_confirm", lang)) },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.clearAllData()
                        showClearConfirm = false
                        Toast.makeText(context, if (lang == "bn") "সকল খরচ ডিলিট করা হয়েছে!" else "All data erased!", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = colorScheme.error),
                    modifier = Modifier.testTag("clear_all_confirm_btn")
                ) {
                    Text(Translation.get("clear_all", lang), color = colorScheme.onError)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearConfirm = false }) {
                    Text(Translation.get("cancel", lang))
                }
            },
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@Composable
fun SettingsSectionHeader(title: String, color: Color) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Black,
        color = color,
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

@Composable
fun SettingsRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    action: @Composable () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    Card(
        modifier = Modifier.fillMaxWidth(),
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
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(colorScheme.primary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = colorScheme.primary, modifier = Modifier.size(18.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = colorScheme.onSurface)
                    Text(text = subtitle, style = MaterialTheme.typography.labelSmall, color = colorScheme.onSurface.copy(alpha = 0.5f))
                }
            }
            Box(modifier = Modifier.padding(start = 8.dp)) {
                action()
            }
        }
    }
}

@Composable
fun SettingsRowButton(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconColor: Color = MaterialTheme.colorScheme.primary,
    tag: String = "",
    onClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .testTag(tag),
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
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(iconColor.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(18.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = if (iconColor == colorScheme.error) colorScheme.error else colorScheme.onSurface)
                    Text(text = subtitle, style = MaterialTheme.typography.labelSmall, color = colorScheme.onSurface.copy(alpha = 0.5f))
                }
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = colorScheme.onSurface.copy(alpha = 0.3f)
            )
        }
    }
}
