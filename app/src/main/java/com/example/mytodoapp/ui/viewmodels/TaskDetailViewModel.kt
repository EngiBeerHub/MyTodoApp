package com.example.mytodoapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.mytodoapp.Constants
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
import java.time.ZoneId
import java.time.ZonedDateTime

class TaskDetailViewModel(application: Application) : ViewModel() {

    // Values for View state
    private val _uiState = MutableStateFlow(TaskDetailUiState(Mode.DEFAULT, true))
    val uiState = _uiState.asStateFlow()

    // Values for Task data
    private var taskId: Int = 0
    val taskTitle: MutableLiveData<String> = MutableLiveData()
    val taskContent: MutableLiveData<String> = MutableLiveData()
    val taskDeadline: MutableLiveData<String> = MutableLiveData()
    private var yearOfDeadLine: Int = 0
    private var monthOfDeadLine: Int = 0
    private var dayOfMonthOfDeadLine: Int = 0
    private var hourOfDayOfDeadLine: Int = 0
    private var minuteOfDeadLine: Int = 0

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

    // Called when specific date is picked in DatePickerDialog
    fun onDeadLineDatePicked(year: Int, month: Int, dayOfMonth: Int) {
        yearOfDeadLine = year
        monthOfDeadLine = month
        dayOfMonthOfDeadLine = dayOfMonth
        taskDeadline.value =
            "$year/${month.toString().padStart(2, '0')}/${dayOfMonth.toString().padStart(2, '0')}"
        _uiState.value = _uiState.value.copy(mode = Mode.UPDATE_DEADLINE_TIME)
    }

    // Called when specific time is picked in TimePickerDialog
    fun onDeadLineTimePicked(hourOfDay: Int, minute: Int) {
        hourOfDayOfDeadLine = hourOfDay
        minuteOfDeadLine = minute
        taskDeadline.value = "${taskDeadline.value} ${hourOfDay.toString().padStart(2, '0')}:${
            minute.toString().padStart(2, '0')
        }"
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
        if (!taskDeadline.value.isNullOrEmpty()) createNotificationForDeadLine()
        val newTask = Task(
            title = taskTitle.value!!,
            content = taskContent.value,
            deadLine = taskDeadline.value
        )
        insertTask(newTask)
    }

    // Update the current existingTask on the Detail Screen
    private fun updateExistingTask() {
        if (!taskDeadline.value.isNullOrEmpty()) createNotificationForDeadLine()
        val updatedTask =
            Task(
                id = taskId,
                // taskTitle has already been validated by isEntryValid()
                title = taskTitle.value!!,
                content = taskContent.value,
                deadLine = taskDeadline.value
            )
        updateTask(updatedTask)
    }

    // Create Notification when saving Task
    private fun createNotificationForDeadLine() {
        val notificationWorkRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(Duration.ofSeconds(getSecondsUntilTaskDeadLine()))
            .setInputData(
                workDataOf(
                    Constants.KEY_WORK_DATA_TASK_ID to taskId,
                    Constants.KEY_WORK_DATA_TASK_TITLE to taskTitle.value,
                    Constants.KEY_WORK_DATA_TASK_CONTENT to taskContent.value
                )
            )
            .build()
        workManager.enqueue(notificationWorkRequest)
    }

    private fun getSecondsUntilTaskDeadLine(): Long {
        val now = ZonedDateTime.now().toEpochSecond()
        val deadLine = ZonedDateTime.of(
            this.yearOfDeadLine,
            this.monthOfDeadLine,
            this.dayOfMonthOfDeadLine,
            this.hourOfDayOfDeadLine,
            this.minuteOfDeadLine,
            0,
            0,
            ZoneId.systemDefault()
        ).toEpochSecond()
        return deadLine - now
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
            @Suppress("UNCHECKED_CAST")
            TaskDetailViewModel(application) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}