package com.example.mytodoapp.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mytodoapp.data.model.Task
import com.example.mytodoapp.data.repository.TaskDataSource

class TaskListViewModel : ViewModel() {
    // task list to show task list fragment
    private val tasks = MutableLiveData<List<Task>>()

    // load tasks in this view model
    fun getTasks(): LiveData<List<Task>> {
        if (tasks.value == null) {
            loadTasks()
        }
        return tasks
    }

    // a simple fun to load Task list from data layer.
    private fun loadTasks() {
        tasks.value = TaskDataSource().loadTasks()
    }
}