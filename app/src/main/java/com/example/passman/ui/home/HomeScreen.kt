package com.example.passman.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.passman.ui.components.Sidebar

@Composable
fun HomeScreen(navController: NavController) {
    var isSidebarOpen by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxSize()
    ) {
        AnimatedVisibility(visible = isSidebarOpen) {
            Sidebar(
                navController = navController,
                onClose = { isSidebarOpen = false }
            )
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
                        IconButton(onClick = { isSidebarOpen = !isSidebarOpen }) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "Menu"
                            )
                        }

                        Text(
                            text = "All Items",
                            style = MaterialTheme.typography.titleLarge
                        )

                        IconButton(onClick = { navController.navigate("passwords") }) {
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
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Welcome to Your Vault",
                    style = MaterialTheme.typography.headlineSmall
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { navController.navigate("passwords") }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Passwords",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Manage your saved passwords",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { navController.navigate("categories") }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Categories",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Organize your passwords",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}
