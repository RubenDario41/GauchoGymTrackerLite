package com.example.gauchogymtrackerlite.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun StartScreen(navController: NavController) {
    var showDialog by remember { mutableStateOf(true) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { /* No hacer nada si se presiona fuera */ },
            title = { Text(text = "Repeticiones") },
            text = { Text(text = "Bienvenido a Gaucho Gym Tracker Lite. ¿Deseas comenzar?") },
            confirmButton = {
                Button(onClick = {
                    showDialog = false
                    navController.navigate("workout_screen") // Navega a la siguiente pantalla
                }) {
                    Text(text = "Comenzar")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text(text = "Cancelar")
                }
            }
        )
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Presiona 'Comenzar' para continuar.")
        }
    }
}