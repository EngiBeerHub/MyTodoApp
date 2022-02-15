package com.example.mytodoapp.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mytodoapp.data.model.Task
import com.example.mytodoapp.data.repository.TaskDataSource

class TaskListViewModel : ViewModel() {
    // task list to show task list fragment
    // backing property
    private val _tasks = MutableLiveData<List<Task>>()

    // property accessible from other classes
    val tasks: LiveData<List<Task>> = _tasks

    // a simple fun to load Task list from data layer.
    // TODO: make asynchronous after implementing Room library.
    private fun loadTasks() {
        _tasks.value = TaskDataSource().loadTasks()
    }

    // Initially load tasks
    init {
        loadTasks()
    }
}