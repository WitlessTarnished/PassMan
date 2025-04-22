package com.example.passman.ui.passwords

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.passman.data.database.DatabaseProvider
import com.example.passman.data.model.PasswordEntry
import com.example.passman.data.repository.PasswordRepository
import com.example.passman.security.CryptoManager
import com.example.passman.security.PasswordGenerator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPasswordScreen(navController: NavController) {
    val context = LocalContext.current
    val database = DatabaseProvider.getDatabase(context)
    val repository = PasswordRepository(database.passwordDao(), database.categoryDao())
    val cryptoManager = CryptoManager(context)
    val viewModel: PasswordViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
        factory = PasswordViewModelFactory(repository, cryptoManager)
    )
    val passwordGenerator = PasswordGenerator()

    var title by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var url by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var totpSecret by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Password") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = { password = passwordGenerator.generatePassword() },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("Generate")
                }
            }
            OutlinedTextField(
                value = url,
                onValueChange = { url = it },
                label = { Text("URL") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notes") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = totpSecret,
                onValueChange = { totpSecret = it },
                label = { Text("TOTP Secret") },
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = {
                    viewModel.addPassword(
                        PasswordEntry(
                            title = title,
                            username = username,
                            password = password,
                            url = url,
                            notes = notes,
                            totpSecret = totpSecret,
                            categoryId = null
                        )
                    )
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save")
            }
        }
    }
}
