package com.example.mytodoapp.data.repository

import androidx.lifecycle.MutableLiveData
import com.example.mytodoapp.data.model.Task

class TaskDataSource {
    fun loadTasks(): List<Task> {
        return listOf(
            Task(false, "task1", "this is the task1."),
            Task(false, "task2", "this is the task2."),
            Task(true, "task3", "this is the task3."),
            Task(false, "task4", "this is the task4."),
            Task(true, "task5", "this is the task5."),
            Task(false, "task6", "this is the task6."),
            Task(true, "task7", "this is the task7."),
            Task(false, "task8", "this is the task8."),
            Task(true, "task9", "this is the task9."),
            Task(false, "task10", "this is the task10."),
            Task(true, "task11", "this is the task11."),
            Task(false, "task12", "this is the task12."),
            Task(true, "task13", "this is the task13."),
            Task(false, "task14", "this is the task14."),
            Task(true, "task15", "this is the task15."),
            Task(false, "task16", "this is the task16."),
            Task(true, "task17", "this is the task17."),
            Task(false, "task18", "this is the task18.")
        )
    }
}