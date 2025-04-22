package com.example.passman.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.passman.data.model.Category
import com.example.passman.data.model.PasswordEntry

@Database(entities = [PasswordEntry::class, Category::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun passwordDao(): PasswordDao
    abstract fun categoryDao(): CategoryDao
}