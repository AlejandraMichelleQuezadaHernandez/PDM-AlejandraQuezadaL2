package com.example.laboratorio04amqh.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.laboratorio04amqh.Model.data.TaskDao
import com.example.laboratorio04amqh.Model.data.taskEntity
import kotlinx.coroutines.launch

class GeneralViewModel(private val taskDao: TaskDao) : ViewModel() {

    var stateTitle by mutableStateOf("")
    var stateDescription by mutableStateOf("")
    var stateSearchAll by mutableStateOf(emptyList<taskEntity>())
    var stateSearchById by mutableStateOf("")
    var TaskList by mutableStateOf<taskEntity?>(null)

    fun addTask() {
        viewModelScope.launch {
            val newTask = taskEntity(
                title = stateTitle,
                description = stateDescription
            )
            taskDao.insertTask(newTask)
        }
        stateTitle = ""
        stateDescription = ""
    }

    fun getAllTasks() {
        viewModelScope.launch {
            taskDao.getAllTask().collect { task ->
                stateSearchAll = task
            }
        }
    }

    fun getTaskByTitle(title: String) {
        viewModelScope.launch {
            taskDao.getTaskByTitle(title).collect { task ->
                TaskList = task.firstOrNull()
            }
        }
    }

    fun deleteTask(id: Int) {
        viewModelScope.launch {
            taskDao.deleteTaskById(id)
        }
    }

    fun updateTask(title: String, newDescription: String) {
        viewModelScope.launch {
            taskDao.updateTaskByTitle(title, newDescription)
        }
    }
}

class GeneralViewModelFactory(private val taskDao: TaskDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GeneralViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GeneralViewModel(taskDao) as T
        }
        throw IllegalArgumentException("Clase ViewModel desconocida")
    }
}