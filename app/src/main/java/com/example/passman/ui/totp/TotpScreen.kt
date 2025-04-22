package com.example.passman.ui.totp

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
import com.example.passman.security.TotpManager
import com.example.passman.ui.passwords.PasswordViewModel
import com.example.passman.ui.passwords.PasswordViewModelFactory
import kotlinx.coroutines.delay

@Composable
fun TotpScreen(navController: NavController) {
    val context = LocalContext.current
    val database = DatabaseProvider.getDatabase(context)
    val repository = PasswordRepository(database.passwordDao(), database.categoryDao())
    val cryptoManager = CryptoManager(context)
    val viewModel: PasswordViewModel = viewModel(
        factory = PasswordViewModelFactory(repository, cryptoManager)
    )
    val passwords by viewModel.passwords.collectAsState()
    val totpManager = TotpManager()
    var remainingSeconds by remember { mutableStateOf(totpManager.getTotpRemainingSeconds()) }
    var progress by remember { mutableStateOf(remainingSeconds / 30f) }

    LaunchedEffect(Unit) {
        while (true) {
            val newRemainingSeconds = totpManager.getTotpRemainingSeconds()
            if (newRemainingSeconds > remainingSeconds) {
                // TOTP period reset: recomposition can be forced if needed
                passwords.forEach { /* trigger recomposition placeholder */ }
            }
            remainingSeconds = newRemainingSeconds
            progress = remainingSeconds / 30f
            delay(1000)
        }
    }

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
                        text = "TOTP Codes",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.width(48.dp)) // visual balance for IconButton space
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Time Remaining: $remainingSeconds s",
                    style = MaterialTheme.typography.bodyMedium
                )
                CircularProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
            }

            passwords.filter { it.totpSecret != null }.forEach { entry ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = entry.title,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = totpManager.generateTotp(entry.totpSecret!!),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}
