package com.example.data

import kotlinx.coroutines.flow.Flow

class ExpenseRepository(private val expenseDao: ExpenseDao) {
    val allExpenses: Flow<List<Expense>> = expenseDao.getAllExpenses()

    suspend fun insertExpense(expense: Expense) {
        expenseDao.insertExpense(expense)
    }

    suspend fun deleteExpense(expense: Expense) {
        expenseDao.deleteExpense(expense)
    }

    fun getExpensesByDate(date: String): Flow<List<Expense>> {
        return expenseDao.getExpensesByDate(date)
    }

    fun searchExpenses(query: String): Flow<List<Expense>> {
        return expenseDao.searchExpenses(query)
    }

    fun getExpensesBetween(startDate: String, endDate: String): Flow<List<Expense>> {
        return expenseDao.getExpensesBetween(startDate, endDate)
    }

    suspend fun clearAllExpenses() {
        expenseDao.clearAllExpenses()
    }
}
