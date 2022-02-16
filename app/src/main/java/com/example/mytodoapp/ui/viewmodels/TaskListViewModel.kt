package com.example.mytodoapp.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.mytodoapp.ToDoApplication
import com.example.mytodoapp.data.Task
import com.example.mytodoapp.data.TaskDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TaskListViewModel() : ViewModel() {
    // get all Tasks from Room database. Any updates are reflected realtime.
    private val taskDao: TaskDao = ToDoApplication.database.taskDao()

    // All Tasks not using Paging
    val tasks: LiveData<List<Task>> = taskDao.getAll().asLiveData()

    companion object {
        // Constants for paging
        private const val PAGE_SIZE = 30
        private const val ENABLE_PLACEHOLDERS = true
        private const val INITIAL_LOAD_SIZE_HINT = 40
        private const val PREFETCH_DISTANCE = 20
    }

    // All Tasks using Paging
    val tasksByTitle = LivePagedListBuilder(
        taskDao.getAllByTitle(),
        PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)
            .setEnablePlaceholders(ENABLE_PLACEHOLDERS)
            .setInitialLoadSizeHint(INITIAL_LOAD_SIZE_HINT)
            .setPrefetchDistance(PREFETCH_DISTANCE)
            .build()
    ).build()

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