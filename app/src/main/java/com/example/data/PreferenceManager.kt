package com.example.data

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("expense_notebook_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_THEME = "key_theme" // "light", "dark", "system"
        private const val KEY_CURRENCY = "key_currency" // "৳", "$"
        private const val KEY_LANGUAGE = "key_language" // "bn", "en"
        private const val KEY_PIN_ENABLED = "key_pin_enabled"
        private const val KEY_PIN_VALUE = "key_pin_value"
        private const val KEY_BIOMETRIC_ENABLED = "key_biometric_enabled"
    }

    var themeMode: String
        get() = prefs.getString(KEY_THEME, "system") ?: "system"
        set(value) = prefs.edit().putString(KEY_THEME, value).apply()

    var currency: String
        get() = prefs.getString(KEY_CURRENCY, "৳") ?: "৳"
        set(value) = prefs.edit().putString(KEY_CURRENCY, value).apply()

    var language: String
        get() = prefs.getString(KEY_LANGUAGE, "bn") ?: "bn"
        set(value) = prefs.edit().putString(KEY_LANGUAGE, value).apply()

    var pinEnabled: Boolean
        get() = prefs.getBoolean(KEY_PIN_ENABLED, false)
        set(value) = prefs.edit().putBoolean(KEY_PIN_ENABLED, value).apply()

    var pinValue: String
        get() = prefs.getString(KEY_PIN_VALUE, "") ?: ""
        set(value) = prefs.edit().putString(KEY_PIN_VALUE, value).apply()

    var biometricEnabled: Boolean
        get() = prefs.getBoolean(KEY_BIOMETRIC_ENABLED, false)
        set(value) = prefs.edit().putBoolean(KEY_BIOMETRIC_ENABLED, value).apply()
}
