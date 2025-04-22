package com.example.passman

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.passman.ui.auth.LoginScreen
import com.example.passman.ui.home.HomeScreen
import com.example.passman.ui.passwords.AddPasswordScreen
import com.example.passman.ui.passwords.PasswordListScreen
import com.example.passman.ui.settings.SettingsScreen
import com.example.passman.ui.totp.TotpScreen
import com.example.passman.ui.theme.PassManTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PassManTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "login"
                    ) {
                        composable("login") { LoginScreen(navController) }
                        composable("home") { HomeScreen(navController) }
                        composable("passwords") { PasswordListScreen(navController) }
                        composable("add_password") { AddPasswordScreen(navController) }
                        composable("settings") { SettingsScreen(navController) }
                        composable("totp") { TotpScreen(navController) }
                        composable("categories") { /* CategoryScreen(navController) */ }
                    }
                }
            }
        }
    }
}
