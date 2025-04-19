package com.example.gauchogymtrackerLite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gauchogymtrackerLite.ui.screens.StartScreen
import com.example.gauchogymtrackerLite.ui.screens.WorkoutScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // Configuración de la navegación
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "start_screen") {
                composable("start_screen") { StartScreen(navController) }
                composable("workout_screen") { WorkoutScreen() }
            }
        }
    }
}navController = rememberNavController()

NavHost(navController = navController, startDestination = "start_screen") {
    composable("start_screen") { StartScreen(navController) }
    composable("workout_screen") { WorkoutScreen() }
}