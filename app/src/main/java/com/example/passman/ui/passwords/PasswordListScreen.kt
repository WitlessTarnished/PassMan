package com.example.passman.ui.passwords

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.passman.data.database.DatabaseProvider
import com.example.passman.data.model.PasswordEntry
import com.example.passman.data.repository.PasswordRepository
import com.example.passman.security.CryptoManager

@Composable
fun PasswordListScreen(navController: NavController) {
    val context = LocalContext.current
    val database = DatabaseProvider.getDatabase(context)
    val repository = PasswordRepository(database.passwordDao(), database.categoryDao())
    val viewModel: PasswordViewModel = viewModel(
        factory = PasswordViewModelFactory(repository, CryptoManager(context))
    )
    val passwords by viewModel.passwords.collectAsState()
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

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
                    Text(
                        text = "Passwords",
                        style = MaterialTheme.typography.titleLarge
                    )
                    IconButton(onClick = { navController.navigate("add_password") }) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Add Password"
                        )
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    viewModel.searchPasswords(it.text)
                },
                label = { Text("Search") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search"
                    )
                }
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(passwords) { entry ->
                    PasswordItem(entry = entry, onClick = {
                        navController.navigate("password_details/${entry.id}")
                    })
                }
            }
        }
    }
}

@Composable
fun PasswordItem(entry: PasswordEntry, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(entry.title, style = MaterialTheme.typography.titleMedium)
                Text(entry.username, style = MaterialTheme.typography.bodyMedium)
            }
            if (entry.totpSecret != null) {
                Text("TOTP", style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}
