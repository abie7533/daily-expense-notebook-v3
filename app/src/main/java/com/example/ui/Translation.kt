package com.example.ui

object Translation {
    private val bnMap = mapOf(
        "app_name" to "দৈনিক খরচ খাতা",
        "today_total" to "আজকের মোট খরচ",
        "month_total" to "এই মাসের মোট খরচ",
        "total_entries" to "মোট এন্ট্রি",
        "highest_day" to "সর্বোচ্চ খরচের দিন",
        "lowest_day" to "সর্বনিম্ন খরচের দিন",
        "no_expenses" to "আজকে কোনো খরচ লেখা হয়নি। নতুন খরচ যোগ করতে নিচের '+' বাটনে চাপুন।",
        "no_expenses_search" to "অনুসন্ধানের সাথে মেলে এমন কোনো খরচ পাওয়া যায়নি।",
        "no_expenses_date" to "এই তারিখে কোনো খরচ রেকর্ড করা হয়নি।",
        "search_hint" to "শিরোনাম, নোট বা ক্যাটাগরি দিয়ে খুঁজুন...",
        "dashboard" to "খাতা",
        "calendar" to "ক্যালেন্ডার",
        "reports" to "রিপোর্ট",
        "settings" to "সেটিংস",
        
        // Add/Edit Fields
        "add_expense" to "নতুন খরচ লিখুন",
        "edit_expense" to "খরচ সংশোধন করুন",
        "amount" to "খরচের পরিমাণ (টাকা)",
        "amount_req" to "পরিমাণ আবশ্যক*",
        "title" to "শিরোনাম/খরচের বিবরণ",
        "title_req" to "শিরোনাম আবশ্যক*",
        "note" to "নোট বা অতিরিক্ত বিবরণ (ঐচ্ছিক)",
        "category" to "ক্যাটাগরি নির্বাচন করুন",
        "date" to "তারিখ",
        "time" to "সময়",
        "save" to "খাতায় লিখুন",
        "update" to "হালনাগাদ করুন",
        "cancel" to "বাতিল",
        "delete" to "ডিলিট করুন",
        "delete_confirm" to "আপনি কি নিশ্চিতভাবে এই খরচটি মুছে ফেলতে চান?",
        
        // Categories
        "Food" to "খাবার",
        "Transport" to "যাতায়াত",
        "Shopping" to "কেনাকাটা",
        "Bills" to "বিল পরিশোধ",
        "Education" to "শিক্ষা",
        "Medical" to "চিকিৎসা",
        "Entertainment" to "বিনোদন",
        "Travel" to "ভ্রমণ",
        "Others" to "অন্যান্য",
        
        // Security / PIN
        "pin_lock" to "নিরাপত্তা পিন লক",
        "pin_enter" to "আপনার ৪-ডিজিট পিন নম্বর দিন",
        "pin_set" to "নতুন ৪-ডিজিট পিন সেট করুন",
        "pin_confirm" to "পিন নম্বরটি নিশ্চিত করুন",
        "pin_wrong" to "ভুল পিন নম্বর! আবার চেষ্টা করুন।",
        "pin_mismatch" to "পিন দুটি মিলেনি! পুনরায় টাইপ করুন।",
        "pin_set_success" to "পিন লক সফলভাবে চালু করা হয়েছে।",
        "pin_disabled_success" to "পিন লক নিষ্ক্রিয় করা হয়েছে।",
        "enable_pin_toggle" to "পিন লক চালু করুন",
        "use_biometric" to "ফিঙ্গারপ্রিন্ট ব্যবহার করুন",
        "biometric_desc" to "সহজে আনলক করতে ফিঙ্গারপ্রিন্ট ব্যবহার করুন",
        
        // Settings & General
        "dark_mode" to "ডার্ক মোড",
        "light_mode" to "লাইট মোড",
        "system_mode" to "সিস্টেম ডিফল্ট",
        "currency" to "মুদ্রা (Currency)",
        "language" to "ভাষা (Language)",
        "backup" to "ব্যাকআপ ও রিস্টোর",
        "export_json" to "ডাটা এক্সপোর্ট (Export JSON)",
        "import_json" to "ডাটা ইম্পোর্ট (Import JSON)",
        "export_success" to "ডাটা সফলভাবে এক্সপোর্ট করা হয়েছে!",
        "import_success" to "ডাটা সফলভাবে ইম্পোর্ট করা হয়েছে!",
        "import_failed" to "ইম্পোর্ট ব্যর্থ হয়েছে! ফাইলটি সঠিক কিনা যাচাই করুন।",
        "clear_all" to "সকল ডাটা মুছে ফেলুন",
        "clear_all_confirm" to "আপনি কি নিশ্চিতভাবে সকল খরচ মুছে ফেলতে চান? এটি আর ফিরিয়ে আনা যাবে না!",
        "app_info" to "দৈনিক খরচ খাতা — একটি সহজ কাগজের খাতার মতো অভিজ্ঞতা। এটি সম্পূর্ণ অফলাইন এবং নিরাপদ।",
        "notebook_look" to "খাতার পাতা",
        "copied_to_clipboard" to "JSON ক্লিপবোর্ডে কপি করা হয়েছে! নিরাপদ স্থানে সংরক্ষণ করুন।"
    )

    private val enMap = mapOf(
        "app_name" to "Daily Expense Notebook",
        "today_total" to "Today's Total Expense",
        "month_total" to "This Month's Total",
        "total_entries" to "Total Entries",
        "highest_day" to "Highest Spending Day",
        "lowest_day" to "Lowest Spending Day",
        "no_expenses" to "No expenses recorded today. Tap the '+' button below to add your first expense.",
        "no_expenses_search" to "No expenses matched your search query.",
        "no_expenses_date" to "No expenses recorded on this date.",
        "search_hint" to "Search by title, note, or category...",
        "dashboard" to "Notebook",
        "calendar" to "Calendar",
        "reports" to "Reports",
        "settings" to "Settings",
        
        // Add/Edit Fields
        "add_expense" to "Write New Expense",
        "edit_expense" to "Edit Expense Entry",
        "amount" to "Expense Amount",
        "amount_req" to "Amount is required*",
        "title" to "Title / Expense Description",
        "title_req" to "Title is required*",
        "note" to "Note / Extra details (Optional)",
        "category" to "Select Category",
        "date" to "Date",
        "time" to "Time",
        "save" to "Write to Notebook",
        "update" to "Update Entry",
        "cancel" to "Cancel",
        "delete" to "Delete",
        "delete_confirm" to "Are you sure you want to delete this expense?",
        
        // Categories
        "Food" to "Food",
        "Transport" to "Transport",
        "Shopping" to "Shopping",
        "Bills" to "Bills & Utilities",
        "Education" to "Education",
        "Medical" to "Medical & Health",
        "Entertainment" to "Entertainment",
        "Travel" to "Travel",
        "Others" to "Others",
        
        // Security / PIN
        "pin_lock" to "Security PIN Lock",
        "pin_enter" to "Enter Your 4-Digit PIN",
        "pin_set" to "Set New 4-Digit PIN",
        "pin_confirm" to "Confirm Your PIN",
        "pin_wrong" to "Incorrect PIN! Please try again.",
        "pin_mismatch" to "PINs do not match! Try again.",
        "pin_set_success" to "PIN lock enabled successfully.",
        "pin_disabled_success" to "PIN lock disabled successfully.",
        "enable_pin_toggle" to "Enable PIN Security",
        "use_biometric" to "Use Biometric/Fingerprint",
        "biometric_desc" to "Quick unlock with biometric sensor",
        
        // Settings & General
        "dark_mode" to "Dark Mode",
        "light_mode" to "Light Mode",
        "system_mode" to "System Default",
        "currency" to "Currency",
        "language" to "Language",
        "backup" to "Backup & Restore",
        "export_json" to "Export Data (JSON)",
        "import_json" to "Import Data (JSON)",
        "export_success" to "Data exported successfully!",
        "import_success" to "Data imported successfully!",
        "import_failed" to "Import failed! Please verify the file.",
        "clear_all" to "Clear All Data",
        "clear_all_confirm" to "Are you sure you want to erase all expenses? This cannot be undone!",
        "app_info" to "Daily Expense Notebook — A simple paper notebook experience. Offline-first and secure.",
        "notebook_look" to "Paper Notebook Style",
        "copied_to_clipboard" to "JSON copied to clipboard! Save it in a safe place."
    )

    fun get(key: String, lang: String): String {
        return if (lang == "bn") {
            bnMap[key] ?: enMap[key] ?: key
        } else {
            enMap[key] ?: key
        }
    }

    // Number translation helpers (e.g. 123.45 -> ১২৩.৪৫)
    fun formatNumber(number: Number, lang: String): String {
        return formatNumber(number.toString(), lang)
    }

    fun formatNumber(str: String, lang: String): String {
        if (lang != "bn") return str
        
        return str.map { char ->
            when (char) {
                '0' -> '০'
                '1' -> '১'
                '2' -> '২'
                '3' -> '৩'
                '4' -> '৪'
                '5' -> '৫'
                '6' -> '৬'
                '7' -> '৭'
                '8' -> '৮'
                '9' -> '৯'
                '.' -> '.'
                else -> char
            }
        }.joinToString("")
    }

    fun formatAmount(amount: Double, currency: String, lang: String): String {
        val formattedNum = formatNumber(String.format("%.2f", amount), lang)
        return if (currency == "৳") {
            if (lang == "bn") "৳$formattedNum" else "৳$formattedNum"
        } else {
            "$currency$formattedNum"
        }
    }

    // Convert date format (e.g. "2026-07-17" to "১৭ জুলাই ২০২৬")
    fun formatDate(dateStr: String, lang: String): String {
        val parts = dateStr.split("-")
        if (parts.size != 3) return dateStr
        val year = parts[0]
        val month = parts[1]
        val day = parts[2]

        val monthNamesBn = listOf(
            "জানুয়ারি", "ফেব্রুয়ারি", "মার্চ", "এপ্রিল", "মে", "জুন",
            "জুলাই", "আগস্ট", "সেপ্টেম্বর", "অক্টোবর", "নভেম্বর", "ডিসেম্বর"
        )
        val monthNamesEn = listOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )

        val monthIdx = month.toIntOrNull()?.minus(1) ?: 0
        val safeMonthIdx = monthIdx.coerceIn(0, 11)

        return if (lang == "bn") {
            val formattedDay = formatNumber(day.toInt(), "bn")
            val formattedYear = formatNumber(year.toInt(), "bn")
            "$formattedDay ${monthNamesBn[safeMonthIdx]} $formattedYear"
        } else {
            val formattedDay = day.toInt().toString()
            "$formattedDay ${monthNamesEn[safeMonthIdx]} $year"
        }
    }

    fun formatTime(timeStr: String, lang: String): String {
        if (lang != "bn") return timeStr
        var result = timeStr
        // Translate AM / PM to পূর্বাহ্ণ / অপরাহ্ণ or just keep AM/PM but translate digits
        result = result.replace("AM", "পূর্বাহ্ণ").replace("PM", "অপরাহ্ণ")
        return formatNumber(result, "bn")
    }
}
