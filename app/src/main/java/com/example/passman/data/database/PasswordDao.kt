package com.example.passman.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.passman.data.model.PasswordEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface PasswordDao {
    @Query("SELECT * FROM passwords")
    fun getAll(): Flow<List<PasswordEntry>>

    @Query("SELECT * FROM passwords WHERE categoryId = :categoryId")
    fun getByCategory(categoryId: Int): Flow<List<PasswordEntry>>

    @Query("SELECT * FROM passwords WHERE title LIKE :query OR username LIKE :query")
    fun search(query: String): Flow<List<PasswordEntry>>

    @Insert
    suspend fun insert(entry: PasswordEntry)

    @Update
    suspend fun update(entry: PasswordEntry)

    @Query("DELETE FROM passwords WHERE id = :id")
    suspend fun delete(id: Int)
}