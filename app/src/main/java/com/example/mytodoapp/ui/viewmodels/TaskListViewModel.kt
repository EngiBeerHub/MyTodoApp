package com.example.mytodoapp.ui.viewmodels

import androidx.lifecycle.*
import com.example.mytodoapp.ToDoApplication
import com.example.mytodoapp.data.Task

class TaskListViewModel() : ViewModel() {
    // get all Tasks from Room database. Any updates are reflected realtime.
    val tasks: LiveData<List<Task>> = ToDoApplication.database.taskDao().getAll().asLiveData()
}