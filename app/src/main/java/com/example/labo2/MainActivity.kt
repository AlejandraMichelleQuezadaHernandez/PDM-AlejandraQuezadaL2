package com.example.labo2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.labo2.pantallas.* // Importamos todas tus pantallas
import com.example.labo2.ui.theme.Labo2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Labo2Theme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Home
                ) {
                    composable<Home> {
                        home(
                            onNavigateToNombres = { navController.navigate(ListaNombres) },
                            onNavigateToSensores = { navController.navigate(sensores) }
                        )
                    }

                    composable<ListaNombres> {
                        ListaNombresLazy(
                            onNavigateToHome = { navController.popBackStack() }
                        )
                    }
                    composable<sensores> {
                        GyroscopeSensor(
                            onNavigateToMain = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}


