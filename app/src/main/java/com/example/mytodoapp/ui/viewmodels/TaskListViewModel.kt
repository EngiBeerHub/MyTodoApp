package com.example.mytodoapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.mytodoapp.ToDoApplication
import com.example.mytodoapp.data.Converters
import com.example.mytodoapp.data.Task
import com.example.mytodoapp.data.TaskDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

class TaskListViewModel : ViewModel() {

    companion object {
        // Constants for Paging
        private const val PAGE_SIZE = 30
    }

    // DAO to handle Task table
    private val taskDao: TaskDao = ToDoApplication.database.taskDao()

    // All Tasks using Paging 3
    val liveAllTasks = Pager(
        PagingConfig(
            pageSize = PAGE_SIZE,
            initialLoadSize = PAGE_SIZE
        )
    ) {
        taskDao.getAll()
    }.flow

    // Make the Task done or undone.
    fun doneUndoneTask(task: Task, isDone: Boolean) {
        val updatedTask = Task(task.id, isDone, task.title, task.content, null)
        viewModelScope.launch {
            taskDao.update(updatedTask)
        }
    }

    init {
        // Insert sample data when no rows for test purpose
        viewModelScope.launch {
            val rowCount = withContext(Dispatchers.Default) { taskDao.getRowCount() }
            if (rowCount == 0) {
                val now: Date = Date(ZonedDateTime.now().toInstant().toEpochMilli())
                taskDao.insertAll(
                    // TODO: format the dead line to simple Date Time.
                    Task(0, true, "Sample Task1", "This is a sample task.", now),
                    Task(0, false, "Sample Task2", "This is a sample task.", null),
                    Task(0, true, "Sample Task3", "This is a sample task.", null),
                    Task(0, false, "Sample Task4", "This is a sample task.", null),
                    Task(0, true, "Sample Task5", "This is a sample task.", null),
                    Task(0, false, "Sample Task6", "This is a sample task.", null),
                    Task(0, true, "Sample Task7", "This is a sample task.", null),
                    Task(0, false, "Sample Task8", "This is a sample task.", null),
                    Task(0, true, "Sample Task9", "This is a sample task.", null),
                    Task(0, false, "Sample Task10", "This is a sample task.", null),
                    Task(0, true, "Sample Task1", "This is a sample task.", null),
                    Task(0, false, "Sample Task2", "This is a sample task.", null),
                    Task(0, true, "Sample Task3", "This is a sample task.", null),
                    Task(0, false, "Sample Task4", "This is a sample task.", null),
                    Task(0, true, "Sample Task5", "This is a sample task.", null),
                    Task(0, false, "Sample Task6", "This is a sample task.", null),
                    Task(0, true, "Sample Task7", "This is a sample task.", null),
                    Task(0, false, "Sample Task8", "This is a sample task.", null),
                    Task(0, true, "Sample Task9", "This is a sample task.", null),
                    Task(0, false, "Sample Task10", "This is a sample task.", null),
                    Task(0, true, "Sample Task1", "This is a sample task.", null),
                    Task(0, false, "Sample Task2", "This is a sample task.", null),
                    Task(0, true, "Sample Task3", "This is a sample task.", null),
                    Task(0, false, "Sample Task4", "This is a sample task.", null),
                    Task(0, true, "Sample Task5", "This is a sample task.", null),
                    Task(0, false, "Sample Task6", "This is a sample task.", null),
                    Task(0, true, "Sample Task7", "This is a sample task.", null),
                    Task(0, false, "Sample Task8", "This is a sample task.", null),
                    Task(0, true, "Sample Task9", "This is a sample task.", null),
                    Task(0, false, "Sample Task10", "This is a sample task.", null)
                )
            }
        }
    }
}