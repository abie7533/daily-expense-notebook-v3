package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.ExpenseViewModel
import com.example.ui.Translation
import com.example.ui.components.notebookPaper

@Composable
fun PinLockScreen(
    viewModel: ExpenseViewModel,
    onUnlockSuccess: () -> Unit
) {
    val lang by viewModel.language.collectAsState()
    val isBiometricEnabled by viewModel.biometricEnabled.collectAsState()
    
    var enteredPin by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    
    val colorScheme = MaterialTheme.colorScheme
    
    // Offset state for shake animation
    val shakeOffset = remember { Animatable(0f) }
    
    LaunchedEffect(errorMessage) {
        if (errorMessage.isNotEmpty()) {
            // Simple shake animation
            repeat(4) {
                shakeOffset.animateTo(15f, animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy))
                shakeOffset.animateTo(-15f, animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy))
            }
            shakeOffset.animateTo(0f)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .notebookPaper(
                lineColor = colorScheme.secondary.copy(alpha = 0.2f),
                marginColor = colorScheme.tertiary.copy(alpha = 0.2f),
                enabled = lang == "bn"
            )
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .offset(x = shakeOffset.value.dp)
                .fillMaxWidth()
                .widthIn(max = 400.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Lock Icon",
                tint = colorScheme.primary,
                modifier = Modifier
                    .size(64.dp)
                    .padding(bottom = 16.dp)
            )

            Text(
                text = Translation.get("app_name", lang),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = Translation.get("pin_enter", lang),
                style = MaterialTheme.typography.bodyLarge,
                color = colorScheme.onBackground.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Pin Dots
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                repeat(4) { index ->
                    val filled = index < enteredPin.length
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(
                                if (filled) colorScheme.primary else colorScheme.onBackground.copy(alpha = 0.2f)
                            )
                    )
                }
            }

            // Error Message
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            } else {
                Spacer(modifier = Modifier.height(44.dp))
            }

            // Numeric Keypad
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val digits = listOf(
                    listOf("1", "2", "3"),
                    listOf("4", "5", "6"),
                    listOf("7", "8", "9")
                )

                digits.forEach { row ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        row.forEach { digit ->
                            KeypadButton(
                                text = Translation.formatNumber(digit.toInt(), lang),
                                onClick = {
                                    if (enteredPin.length < 4) {
                                        errorMessage = ""
                                        enteredPin += digit
                                        if (enteredPin.length == 4) {
                                            val success = viewModel.unlockApp(enteredPin)
                                            if (success) {
                                                onUnlockSuccess()
                                            } else {
                                                errorMessage = Translation.get("pin_wrong", lang)
                                                enteredPin = ""
                                            }
                                        }
                                    }
                                },
                                modifier = Modifier.testTag("pin_key_$digit")
                            )
                        }
                    }
                }

                // Last Row: Biometrics (or blank), 0, Backspace
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Biometric unlock shortcut button if enabled
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(
                                if (isBiometricEnabled) colorScheme.surface else Color.Transparent
                            )
                            .clickable(enabled = isBiometricEnabled) {
                                // Simulate Biometric authentication immediately succeeding
                                viewModel.unlockApp(viewModel.pinValue.value)
                                onUnlockSuccess()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (isBiometricEnabled) {
                            Icon(
                                imageVector = Icons.Default.Fingerprint,
                                contentDescription = "Biometric Unlock",
                                tint = colorScheme.primary,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }

                    KeypadButton(
                        text = Translation.formatNumber(0, lang),
                        onClick = {
                            if (enteredPin.length < 4) {
                                errorMessage = ""
                                enteredPin += "0"
                                if (enteredPin.length == 4) {
                                    val success = viewModel.unlockApp(enteredPin)
                                    if (success) {
                                        onUnlockSuccess()
                                    } else {
                                        errorMessage = Translation.get("pin_wrong", lang)
                                        enteredPin = ""
                                    }
                                }
                            }
                        },
                        modifier = Modifier.testTag("pin_key_0")
                    )

                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .clickable {
                                if (enteredPin.isNotEmpty()) {
                                    errorMessage = ""
                                    enteredPin = enteredPin.dropLast(1)
                                }
                            }
                            .testTag("pin_key_backspace"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Backspace,
                            contentDescription = "Backspace",
                            tint = colorScheme.onBackground.copy(alpha = 0.7f),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun KeypadButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    Box(
        modifier = modifier
            .size(72.dp)
            .clip(CircleShape)
            .background(colorScheme.surface)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = colorScheme.onSurface,
            fontSize = 24.sp
        )
    }
}
