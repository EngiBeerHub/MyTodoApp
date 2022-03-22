package com.example.mytodoapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.mytodoapp.ToDoApplication
import com.example.mytodoapp.data.Task
import com.example.mytodoapp.data.TaskDao
import com.example.mytodoapp.ui.Mode
import com.example.mytodoapp.ui.TaskDetailUiState
import com.example.mytodoapp.workers.NotificationWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Duration

class TaskDetailViewModel(application: Application) : ViewModel() {

    // Values for View state
    private val _uiState = MutableStateFlow(TaskDetailUiState(Mode.DEFAULT, true))
    val uiState = _uiState.asStateFlow()

    // Values for Task data
    private var taskId: Int = 0
    val taskTitle: MutableLiveData<String> = MutableLiveData()
    val taskContent: MutableLiveData<String> = MutableLiveData()
    val taskDeadline: MutableLiveData<String> = MutableLiveData()

    // DAO to handle Task table
    private val taskDao: TaskDao = ToDoApplication.database.taskDao()

    // WorkManager to handle Notification
    private val workManager = WorkManager.getInstance(application)

    // Bind current Task to the Detail if it exist
    fun bindTask(taskId: Int) {
        // If taskId is default value, this is a new Task
        if (taskId == 0) {
            _uiState.value = _uiState.value.copy(mode = Mode.CREATE, btDeleteVisible = false)
        } else {
            // Else, bind the existing Task to the view
            this.taskId = taskId
            viewModelScope.launch {
                val task = withContext(Dispatchers.Default) {
                    taskDao.findById(taskId)
                }
                taskTitle.value = task.title
                task.content?.let {
                    taskContent.value = task.content
                }
                task.deadLine?.let {
                    taskDeadline.value = it
                }
            }
            _uiState.value = _uiState.value.copy(mode = Mode.UPDATE_COMMON)
        }
    }

    // Save current Task on the Detail screen
    fun saveCurrentTask() {
        // If validation is OK, create or update the current Task on the Detail screen.
        if (isEntryValid()) {
            if (_uiState.value.mode == Mode.CREATE) {
                createNewTask()
                _uiState.value = _uiState.value.copy(mode = Mode.SUCCESS_CREATE)
            } else if (_uiState.value.mode == Mode.UPDATE_COMMON) {
                updateExistingTask()
                _uiState.value = _uiState.value.copy(mode = Mode.SUCCESS_UPDATE)
            }
        } else {
            _uiState.value = _uiState.value.copy(mode = Mode.ERROR_VALIDATION)
        }
    }

    // Observer of this ViewModel must call this method after handling the ViewModel event like CRUD.
    fun onEventCompleted() {
        _uiState.value = _uiState.value.copy(mode = Mode.DEFAULT)
    }

    fun setDeadline() {
        _uiState.value = _uiState.value.copy(mode = Mode.UPDATE_DEADLINE_DATE)
    }

    fun onSetDeadLineCanceled() {
        resetStatus()
    }

    fun onDeadLineDatePicked(year: Int, month: Int, dayOfMonth: Int) {
        taskDeadline.value = "$year/${month}/${dayOfMonth}"
        _uiState.value = _uiState.value.copy(mode = Mode.UPDATE_DEADLINE_TIME)
    }

    fun onDeadLineTimePicked(hourOfDay: Int, minute: Int) {
        taskDeadline.value = "${taskDeadline.value} ${hourOfDay}:${minute}"
        resetStatus()
    }

    // Confirm before deleting
    fun confirmDeletingTask() {
        _uiState.value = _uiState.value.copy(mode = Mode.CONFIRM_DELETE)
    }

    // Delete the existing Task on the Detail screen
    fun deleteCurrentTask() {
        // Only in the existing Task is retrieved and mode UPDATE_COMMON
        if (taskId != 0 && _uiState.value.mode == Mode.CONFIRM_DELETE) {
            viewModelScope.launch {
                val task = taskDao.findById(taskId)
                taskDao.delete(task)
            }
            _uiState.value = _uiState.value.copy(mode = Mode.SUCCESS_DELETE)
        }
    }

    // validation check for adding new Task
    private fun isEntryValid(): Boolean {
        return !taskTitle.value.isNullOrEmpty()
    }

    // Create new Task from the current Task on the Detail Screen
    private fun createNewTask() {
        // check whether content is inputted
        val newTask = Task(
            title = taskTitle.value!!,
            content = taskContent.value,
            deadLine = taskDeadline.value
        )
        createNotificationForDeadLine()
        insertTask(newTask)
    }

    // Update the current existingTask on the Detail Screen
    private fun updateExistingTask() {
        val updatedTask =
            Task(
                id = taskId,
                // taskTitle has already been validated by isEntryValid()
                title = taskTitle.value!!,
                content = taskContent.value,
                deadLine = taskDeadline.value
            )
        createNotificationForDeadLine()
        updateTask(updatedTask)
    }

    private fun createNotificationForDeadLine() {
        val notificationWorkRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(Duration.ofSeconds(10))
            .build()
        workManager.enqueue(notificationWorkRequest)
    }

    private fun insertTask(task: Task) {
        viewModelScope.launch { taskDao.insert(task) }
    }

    private fun updateTask(task: Task) {
        viewModelScope.launch { taskDao.update(task) }
    }

    private fun resetStatus() {
        if (taskId == 0) {
            _uiState.value = _uiState.value.copy(mode = Mode.CREATE)
        } else {
            _uiState.value = _uiState.value.copy(mode = Mode.UPDATE_COMMON)
        }
    }

}

class TaskDetailViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(TaskDetailViewModel::class.java)) {
            TaskDetailViewModel(application) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}