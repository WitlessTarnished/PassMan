package com.example.passman.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun Sidebar(navController: NavController, onClose: () -> Unit) {
    Surface(
        modifier = Modifier
            .width(250.dp)
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("PassMan", style = MaterialTheme.typography.headlineSmall)
            Divider()
            SidebarItem("All Items", onClick = { navController.navigate("home"); onClose() })
            SidebarItem("Categories", onClick = { navController.navigate("categories"); onClose() })
            SidebarItem("Settings", onClick = { navController.navigate("settings"); onClose() })
        }
    }
}

@Composable
fun SidebarItem(text: String, onClick: () -> Unit) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() }
    )
}
