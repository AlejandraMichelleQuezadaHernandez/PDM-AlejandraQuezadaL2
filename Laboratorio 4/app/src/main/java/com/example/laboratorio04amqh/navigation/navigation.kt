package com.example.laboratorio04amqh.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.laboratorio04amqh.ViewModel.GeneralViewModel
import com.example.laboratorio04amqh.ui.View.home
import com.example.laboratorio04amqh.ui.View.Greeting
import kotlinx.serialization.Serializable

@Composable
fun navigationScreens() {
    val navController = rememberNavController()
    val viewModel: GeneralViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable(route = "Greeting") {
            Greeting(navController, viewModel = viewModel)
        }
        composable(route = "home") {
            home(navController, viewModel = viewModel)
        }
    }
}
