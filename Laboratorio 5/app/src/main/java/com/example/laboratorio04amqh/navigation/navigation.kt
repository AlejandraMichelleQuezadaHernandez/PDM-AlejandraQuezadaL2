package com.example.laboratorio04amqh.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.laboratorio04amqh.Model.data.TaskDatabase
import com.example.laboratorio04amqh.ViewModel.GeneralViewModel
import com.example.laboratorio04amqh.ViewModel.GeneralViewModelFactory
import com.example.laboratorio04amqh.ui.View.home
import com.example.laboratorio04amqh.ui.View.Greeting


@Composable
fun navigationScreens() {
    val navController = rememberNavController()

    val context = LocalContext.current


    val database = TaskDatabase.getDatabase(context)
    val taskDao = database.TaskDao()

    val viewModel: GeneralViewModel = viewModel(
        factory = GeneralViewModelFactory(taskDao)
    )

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