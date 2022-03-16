package com.example.mytodoapp.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytodoapp.ToDoApplication
import com.example.mytodoapp.data.Task
import com.example.mytodoapp.data.TaskDao
import com.example.mytodoapp.ui.views.Mode
import com.example.mytodoapp.ui.views.TaskDetailUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TaskDetailViewModel : ViewModel() {

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
                    taskDeadline.value = it.toString()
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
    fun onCompleteEvent() {
        _uiState.value = _uiState.value.copy(mode = Mode.DEFAULT)
    }

    fun setDeadline() {
        _uiState.value = _uiState.value.copy(mode = Mode.UPDATE_DEADLINE)
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
        val newTask = if (taskContent.value != null) {
            Task(title = taskTitle.value!!, content = taskContent.value, deadLine = null)
        } else {
            Task(title = taskTitle.value!!, content = null, deadLine = null)
        }
        insertTask(newTask)
    }

    // Update the current existingTask on the Detail Screen
    private fun updateExistingTask() {
        val updatedTask = if (taskContent.value != null) {
            Task(
                id = taskId,
                title = taskTitle.value!!,
                content = taskContent.value!!,
                deadLine = null
            )
        } else {
            Task(id = taskId, title = taskTitle.value!!, content = null, deadLine = null)
        }
        updateTask(updatedTask)
    }

    private fun insertTask(task: Task) {
        viewModelScope.launch { taskDao.insert(task) }
    }

    private fun updateTask(task: Task) {
        viewModelScope.launch { taskDao.update(task) }
    }

}