package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class ExpenseViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val repository = ExpenseRepository(db.expenseDao())
    private val prefManager = PreferenceManager(application)

    // Settings States
    val language = MutableStateFlow(prefManager.language)
    val currency = MutableStateFlow(prefManager.currency)
    val themeMode = MutableStateFlow(prefManager.themeMode)
    val pinEnabled = MutableStateFlow(prefManager.pinEnabled)
    val pinValue = MutableStateFlow(prefManager.pinValue)
    val biometricEnabled = MutableStateFlow(prefManager.biometricEnabled)

    // Security Lock State
    val isAppLocked = MutableStateFlow(prefManager.pinEnabled && prefManager.pinValue.isNotEmpty())

    // All Expenses flow
    val allExpenses = repository.allExpenses.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Search query
    val searchQuery = MutableStateFlow("")

    // Calendar selected date (Format: YYYY-MM-DD, default is today)
    val selectedDate = MutableStateFlow(getCurrentDateString())

    // Active screen navigation state (just local state for bottom nav or general screens)
    val currentTab = MutableStateFlow(0) // 0: Home, 1: Calendar, 2: Reports, 3: Settings

    // Filtered expenses for selected date
    val calendarExpenses = combine(allExpenses, selectedDate) { expenses, date ->
        expenses.filter { it.date == date }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Filtered expenses based on search query
    val searchedExpenses = combine(allExpenses, searchQuery) { expenses, query ->
        if (query.isBlank()) {
            expenses
        } else {
            expenses.filter {
                it.title.contains(query, ignoreCase = true) ||
                (it.note ?: "").contains(query, ignoreCase = true) ||
                it.category.contains(query, ignoreCase = true) ||
                it.date.contains(query, ignoreCase = true)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Today's total expense
    val todayTotal = allExpenses.map { expenses ->
        val todayStr = getCurrentDateString()
        expenses.filter { it.date == todayStr }.sumOf { it.amount }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )

    // Current month's total expense
    val thisMonthTotal = allExpenses.map { expenses ->
        val currentMonthYear = getCurrentMonthYearString() // Format: YYYY-MM
        expenses.filter { it.date.startsWith(currentMonthYear) }.sumOf { it.amount }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )

    // Monthly stats calculations (Highest spending day, lowest spending day, entries count)
    data class MonthlyStats(
        val totalAmount: Double = 0.0,
        val totalEntries: Int = 0,
        val highestDayDate: String = "",
        val highestDayAmount: Double = 0.0,
        val lowestDayDate: String = "",
        val lowestDayAmount: Double = 0.0,
        val categoryBreakdown: Map<String, Double> = emptyMap()
    )

    val monthlyStats = allExpenses.map { expenses ->
        val currentMonthYear = getCurrentMonthYearString()
        val monthExpenses = expenses.filter { it.date.startsWith(currentMonthYear) }
        
        if (monthExpenses.isEmpty()) {
            MonthlyStats()
        } else {
            val totalAmt = monthExpenses.sumOf { it.amount }
            val totalEnt = monthExpenses.size
            
            // Group by date
            val groupedByDate = monthExpenses.groupBy { it.date }
            val dailySums = groupedByDate.mapValues { entry -> entry.value.sumOf { it.amount } }
            
            val highestDay = dailySums.maxByOrNull { it.value }
            val lowestDay = dailySums.minByOrNull { it.value }

            // Group by category
            val categorySums = monthExpenses.groupBy { it.category }
                .mapValues { entry -> entry.value.sumOf { it.amount } }

            MonthlyStats(
                totalAmount = totalAmt,
                totalEntries = totalEnt,
                highestDayDate = highestDay?.key ?: "",
                highestDayAmount = highestDay?.value ?: 0.0,
                lowestDayDate = lowestDay?.key ?: "",
                lowestDayAmount = lowestDay?.value ?: 0.0,
                categoryBreakdown = categorySums
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MonthlyStats()
    )

    // Expense operations
    fun addExpense(amount: Double, title: String, note: String?, category: String, date: String) {
        viewModelScope.launch {
            val calendar = Calendar.getInstance()
            val timeFormat = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
            val currentTime = timeFormat.format(calendar.time)
            
            // Build absolute timestamp for chronological sorting
            val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.ENGLISH)
            val parsedDate = try {
                dateFormat.parse("$date $currentTime")
            } catch (e: Exception) {
                null
            }
            val timestamp = parsedDate?.time ?: System.currentTimeMillis()

            val expense = Expense(
                amount = amount,
                title = title.trim(),
                note = note?.trim()?.takeIf { it.isNotEmpty() },
                category = category,
                date = date,
                time = currentTime,
                timestamp = timestamp
            )
            repository.insertExpense(expense)
        }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            repository.deleteExpense(expense)
        }
    }

    // Settings actions
    fun setLanguage(lang: String) {
        prefManager.language = lang
        language.value = lang
    }

    fun setCurrency(curr: String) {
        prefManager.currency = curr
        currency.value = curr
    }

    fun setThemeMode(mode: String) {
        prefManager.themeMode = mode
        themeMode.value = mode
    }

    fun setPinLock(enabled: Boolean, pin: String) {
        prefManager.pinEnabled = enabled
        prefManager.pinValue = pin
        pinEnabled.value = enabled
        pinValue.value = pin
        
        // If enabling PIN and it's not empty, make sure isAppLocked represents the lock
        isAppLocked.value = enabled && pin.isNotEmpty()
    }

    fun unlockApp(pin: String): Boolean {
        return if (pin == pinValue.value) {
            isAppLocked.value = false
            true
        } else {
            false
        }
    }

    fun setBiometric(enabled: Boolean) {
        prefManager.biometricEnabled = enabled
        biometricEnabled.value = enabled
    }

    fun clearAllData() {
        viewModelScope.launch {
            repository.clearAllExpenses()
        }
    }

    // Backup & Restore
    fun exportBackupJson(): String {
        val array = JSONArray()
        allExpenses.value.forEach {
            array.put(it.toJsonObject())
        }
        return array.toString(4)
    }

    suspend fun importBackupJson(jsonString: String): Boolean {
        return try {
            val array = JSONArray(jsonString)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val expense = Expense.fromJsonObject(obj)
                // Insert into db (will overwrite/insert with 0 or correct ID)
                repository.insertExpense(expense)
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    // Date Utilities
    fun getCurrentDateString(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        return sdf.format(Date())
    }

    private fun getCurrentMonthYearString(): String {
        val sdf = SimpleDateFormat("yyyy-MM", Locale.ENGLISH)
        return sdf.format(Date())
    }
}
