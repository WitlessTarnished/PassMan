package com.example.passman.ui.passwords

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.passman.data.model.PasswordEntry
import com.example.passman.data.repository.PasswordRepository
import com.example.passman.security.CryptoManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PasswordViewModel(
    private val repository: PasswordRepository,
    private val cryptoManager: CryptoManager
) : ViewModel() {

    private val _passwords = MutableStateFlow<List<PasswordEntry>>(emptyList())
    val passwords: StateFlow<List<PasswordEntry>> = _passwords

    init {
        // Load all passwords and decrypt them when the ViewModel is created
        viewModelScope.launch {
            repository.allPasswords.collectLatest { entries ->
                _passwords.value = entries.map {
                    it.copy(password = cryptoManager.decryptData(it.password))
                }
            }
        }
    }

    // Function to search passwords based on the query
    fun searchPasswords(query: String) {
        viewModelScope.launch {
            repository.searchPasswords(query).collectLatest { entries ->
                _passwords.value = entries.map {
                    it.copy(password = cryptoManager.decryptData(it.password))
                }
            }
        }
    }

    // Function to add a new password, encrypting the password before saving
    fun addPassword(entry: PasswordEntry) {
        viewModelScope.launch {
            repository.insertPassword(entry.copy(password = cryptoManager.encryptData(entry.password)))
        }
    }
}

class PasswordViewModelFactory(
    private val repository: PasswordRepository,
    private val cryptoManager: CryptoManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PasswordViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PasswordViewModel(repository, cryptoManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
