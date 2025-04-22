package com.example.passman.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.passman.data.database.DatabaseProvider
import com.example.passman.data.repository.PasswordRepository
import com.example.passman.security.CryptoManager
import com.example.passman.ui.passwords.PasswordViewModel
import com.example.passman.ui.passwords.PasswordViewModelFactory
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun SettingsScreen(navController: NavController) {
    val context = LocalContext.current
    val database = DatabaseProvider.getDatabase(context)
    val repository = PasswordRepository(database.passwordDao(), database.categoryDao())
    val cryptoManager = CryptoManager(context)
    val viewModel: PasswordViewModel = viewModel(
        factory = PasswordViewModelFactory(repository, cryptoManager)
    )
    val passwords by viewModel.passwords.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.width(48.dp)) // Balance the layout
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Data Management", style = MaterialTheme.typography.titleMedium)

            Button(
                onClick = {
                    val file = File(context.getExternalFilesDir(null), "passman_export.json")
                    cryptoManager.exportPasswords(passwords, file)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Export Passwords")
            }

            Button(
                onClick = {
                    val file = File(context.getExternalFilesDir(null), "passman_export.json")
                    if (file.exists()) {
                        coroutineScope.launch {
                            val imported = cryptoManager.importPasswords(file)
                            imported.forEach { viewModel.addPassword(it) }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Import Passwords")
            }
        }
    }
}
