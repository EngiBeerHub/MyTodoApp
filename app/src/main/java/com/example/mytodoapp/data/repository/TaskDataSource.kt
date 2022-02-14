package com.example.mytodoapp.data.repository

import androidx.lifecycle.MutableLiveData
import com.example.mytodoapp.data.model.Task

class TaskDataSource {
    fun loadTasks(): List<Task> {
        return listOf(
            Task(1, false, "task1", "this is the task1."),
            Task(2, false, "task2", "this is the task2."),
            Task(3, true, "task3", "this is the task3."),
            Task(4, false, "task4", "this is the task4."),
            Task(5, true, "task5", "this is the task5."),
            Task(6, false, "task6", "this is the task6."),
            Task(7, true, "task7", "this is the task7."),
            Task(8, false, "task8", "this is the task8."),
            Task(9, true, "task9", "this is the task9."),
            Task(10, false, "task10", "this is the task10.")
        )
    }
}