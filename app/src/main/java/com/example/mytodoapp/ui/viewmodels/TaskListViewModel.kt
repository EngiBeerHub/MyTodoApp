package com.example.mytodoapp.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mytodoapp.ToDoApplication
import com.example.mytodoapp.data.Task
import com.example.mytodoapp.data.TaskDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TaskListViewModel() : ViewModel() {
    // get all Tasks from Room database. Any updates are reflected realtime.
    private var taskDao: TaskDao = ToDoApplication.database.taskDao()
    val tasks: LiveData<List<Task>> = taskDao.getAll().asLiveData()

    init {
        // Insert sample data when no rows for test purpose
        viewModelScope.launch {
            val rowCount = withContext(Dispatchers.Default) { taskDao.getRowCount() }
            if (rowCount == 0) {
                // TODO: insert initial Task data
                taskDao.insertAll(
                    Task(0, true, "Sample Task1", "This is a sample task."),
                    Task(0, false, "Sample Task2", "This is a sample task."),
                    Task(0, true, "Sample Task3", "This is a sample task."),
                    Task(0, false, "Sample Task4", "This is a sample task."),
                    Task(0, true, "Sample Task5", "This is a sample task."),
                    Task(0, false, "Sample Task6", "This is a sample task."),
                    Task(0, true, "Sample Task7", "This is a sample task."),
                    Task(0, false, "Sample Task8", "This is a sample task."),
                    Task(0, true, "Sample Task9", "This is a sample task."),
                    Task(0, false, "Sample Task10", "This is a sample task."),
                    Task(0, true, "Sample Task1", "This is a sample task."),
                    Task(0, false, "Sample Task2", "This is a sample task."),
                    Task(0, true, "Sample Task3", "This is a sample task."),
                    Task(0, false, "Sample Task4", "This is a sample task."),
                    Task(0, true, "Sample Task5", "This is a sample task."),
                    Task(0, false, "Sample Task6", "This is a sample task."),
                    Task(0, true, "Sample Task7", "This is a sample task."),
                    Task(0, false, "Sample Task8", "This is a sample task."),
                    Task(0, true, "Sample Task9", "This is a sample task."),
                    Task(0, false, "Sample Task10", "This is a sample task."),
                    Task(0, true, "Sample Task1", "This is a sample task."),
                    Task(0, false, "Sample Task2", "This is a sample task."),
                    Task(0, true, "Sample Task3", "This is a sample task."),
                    Task(0, false, "Sample Task4", "This is a sample task."),
                    Task(0, true, "Sample Task5", "This is a sample task."),
                    Task(0, false, "Sample Task6", "This is a sample task."),
                    Task(0, true, "Sample Task7", "This is a sample task."),
                    Task(0, false, "Sample Task8", "This is a sample task."),
                    Task(0, true, "Sample Task9", "This is a sample task."),
                    Task(0, false, "Sample Task10", "This is a sample task.")
                )
            }
        }
    }
}