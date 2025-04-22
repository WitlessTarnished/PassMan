package com.example.passman.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.passman.data.model.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories")
    fun getAll(): Flow<List<Category>>

    @Insert
    suspend fun insert(category: Category)

    @Query("DELETE FROM categories WHERE id = :id")
    suspend fun delete(id: Int)
}