package com.example.laboratorio04amqh.ui.View

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import com.example.laboratorio04amqh.Model.data.taskEntity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.laboratorio04amqh.Components.TaskCard
import com.example.laboratorio04amqh.Model.Task
import com.example.laboratorio04amqh.ViewModel.GeneralViewModel
import java.time.format.TextStyle
import java.util.Date


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting(navController: NavController, viewModel: GeneralViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    val taskList = viewModel.stateSearchAll
    var showUpdateDialog by remember { mutableStateOf(false) }
    var selectedTask by remember { mutableStateOf<com.example.laboratorio04amqh.Model.data.taskEntity?>(null) }
    var searchTitle by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        viewModel.getAllTasks()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Tasks list") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Añadir")
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                OutlinedTextField(
                    value = searchTitle,
                    onValueChange = { searchTitle = it },
                    label = { Text("Buscar por título") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            viewModel.getTaskByTitle(searchTitle)
                            keyboardController?.hide()
                        }
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        viewModel.getTaskByTitle(searchTitle)
                        keyboardController?.hide()
                    }
                ) {
                    Text("Buscar")
                }
            }

            viewModel.TaskList?.let { task ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(text = "Resultado de la búsqueda")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Título: ${task.title}")
                        Text(text = "Descripción: ${task.description}")
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(taskList) { task ->
                    TaskCard(
                        Task(
                            id = task.id,
                            title = task.title,
                            description = task.description
                        )
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { viewModel.deleteTask(task.id) }
                        ) {
                            Text("Eliminar")
                        }
                        Button(
                            onClick = {
                                selectedTask = task
                                showUpdateDialog = true
                            }
                        ) {
                            Text("Actualizar")
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        if (showDialog) {
            CreateTask(
                onDismiss = { showDialog = false },
                onTaskCreated = { newTitle, newDescription ->
                    viewModel.stateTitle = newTitle
                    viewModel.stateDescription = newDescription
                    viewModel.addTask()
                    showDialog = false
                }
            )
        }

        if (showUpdateDialog && selectedTask != null) {
            UpdateTaskDialog(
                task = selectedTask!!,
                onDismiss = {
                    showUpdateDialog = false
                },
                onUpdate = { newDescription ->
                    viewModel.updateTask(
                        selectedTask!!.title,
                        newDescription
                    )
                    showUpdateDialog = false
                }
            )
        }
    }
}

@Composable
fun CreateTask(
    onDismiss: () -> Unit,
    onTaskCreated: (String, String) -> Unit
){
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    Dialog(
        onDismissRequest = {
            onDismiss()
        },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        ),
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .background(Color.Black),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Nueva Tarea",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))


            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 16.dp),
            ) {
                Button(onClick = { onDismiss()}) {
                    Text(text = "Cerrar")
                }

                Button(onClick = {
                    if (title.isNotBlank()){
                        onTaskCreated(title,description)
                    }
                },
                    enabled = title.isNotBlank()) {
                    Text("Crear")
                }
            }
        }
    }
}

@Composable
fun UpdateTaskDialog(
    task: taskEntity,
    onDismiss: () -> Unit,
    onUpdate: (String) -> Unit
) {
    var description by remember {
        mutableStateOf(task.description)
    }

    Dialog(
        onDismissRequest = { onDismiss() }
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .background(Color.Black),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text("Actualizar tarea")

            Spacer(modifier = Modifier.height(16.dp))

            Text("Título: ${task.title}")

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Nueva descripción") }
            )

            Row {
                Button(
                    onClick = { onDismiss() }
                ) {
                    Text("Cancelar")
                }

                Button(
                    onClick = {
                        onUpdate(description)
                    }
                ) {
                    Text("Guardar")
                }
            }
        }
    }
}
/*
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Greeting()
}*/