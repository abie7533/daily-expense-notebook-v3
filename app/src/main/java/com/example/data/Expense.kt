package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.json.JSONObject

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val amount: Double,
    val title: String,
    val note: String?,
    val category: String, // Category key (e.g. "Food", "Transport")
    val date: String,     // Format: YYYY-MM-DD
    val time: String,     // Format: HH:MM AM/PM
    val timestamp: Long   // Unix epoch milliseconds for sorting
) {
    fun toJsonObject(): JSONObject {
        val json = JSONObject()
        json.put("id", id)
        json.put("amount", amount)
        json.put("title", title)
        json.put("note", note ?: "")
        json.put("category", category)
        json.put("date", date)
        json.put("time", time)
        json.put("timestamp", timestamp)
        return json
    }

    companion object {
        fun fromJsonObject(json: JSONObject): Expense {
            return Expense(
                id = json.optInt("id", 0),
                amount = json.getDouble("amount"),
                title = json.getString("title"),
                note = json.optString("note", "").takeIf { it.isNotEmpty() },
                category = json.getString("category"),
                date = json.getString("date"),
                time = json.getString("time"),
                timestamp = json.optLong("timestamp", System.currentTimeMillis())
            )
        }
    }
}
