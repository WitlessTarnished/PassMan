package com.example.passman.data.repository

import com.example.passman.data.database.CategoryDao
import com.example.passman.data.database.PasswordDao
import com.example.passman.data.model.Category
import com.example.passman.data.model.PasswordEntry
import kotlinx.coroutines.flow.Flow

class PasswordRepository(
    private val passwordDao: PasswordDao,
    private val categoryDao: CategoryDao
) {

    val allPasswords: Flow<List<PasswordEntry>> = passwordDao.getAll()
    val allCategories: Flow<List<Category>> = categoryDao.getAll()

    fun getPasswordsByCategory(categoryId: Int): Flow<List<PasswordEntry>> {
        return passwordDao.getByCategory(categoryId)
    }

    fun searchPasswords(query: String): Flow<List<PasswordEntry>> {
        return passwordDao.search("%$query%")
    }

    suspend fun insertPassword(entry: PasswordEntry) {
        passwordDao.insert(entry)
    }

    suspend fun updatePassword(entry: PasswordEntry) {
        passwordDao.update(entry)
    }

    suspend fun deletePassword(id: Int) {
        passwordDao.delete(id)
    }

    suspend fun insertCategory(category: Category) {
        categoryDao.insert(category)
    }

    suspend fun deleteCategory(id: Int) {
        categoryDao.delete(id)
    }
}
