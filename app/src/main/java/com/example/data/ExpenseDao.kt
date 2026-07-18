package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses ORDER BY timestamp DESC")
    fun getAllExpenses(): Flow<List<Expense>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: Expense)

    @Delete
    suspend fun deleteExpense(expense: Expense)

    @Query("SELECT * FROM expenses WHERE date = :date ORDER BY timestamp DESC")
    fun getExpensesByDate(date: String): Flow<List<Expense>>

    @Query("""
        SELECT * FROM expenses 
        WHERE title LIKE '%' || :query || '%' 
        OR note LIKE '%' || :query || '%' 
        OR category LIKE '%' || :query || '%' 
        OR date LIKE '%' || :query || '%'
        ORDER BY timestamp DESC
    """)
    fun searchExpenses(query: String): Flow<List<Expense>>

    @Query("SELECT * FROM expenses WHERE date >= :startDate AND date <= :endDate ORDER BY timestamp DESC")
    fun getExpensesBetween(startDate: String, endDate: String): Flow<List<Expense>>

    @Query("DELETE FROM expenses")
    suspend fun clearAllExpenses()
}
