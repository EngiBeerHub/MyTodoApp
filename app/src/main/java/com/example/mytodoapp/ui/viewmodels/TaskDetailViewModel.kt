package com.example.mytodoapp.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytodoapp.ToDoApplication
import com.example.mytodoapp.data.Task
import com.example.mytodoapp.data.TaskDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TaskDetailViewModel : ViewModel() {

    enum class Mode {
        DEFAULT,
        CREATE,
        UPDATE,
        SUCCESS_CREATE,
        SUCCESS_UPDATE,
        ERROR_VALIDATION
    }

    // Values for View state
    private val _mode: MutableLiveData<Mode> = MutableLiveData()
    val mode: LiveData<Mode> = _mode

    // Values for Task data
    private var taskId: Int = 0
    val taskTitle: MutableLiveData<String> = MutableLiveData()
    val taskContent: MutableLiveData<String> = MutableLiveData()

    // initialize values for view state
    init {
        _mode.value = Mode.DEFAULT
    }

    // DAO to handle Task table
    private val taskDao: TaskDao = ToDoApplication.database.taskDao()

    // Bind current Task to the Detail if it exist
    fun bindTask(taskId: Int) {
        // If taskId is default value, this is a new Task
        if (taskId == 0) {
            _mode.value = Mode.CREATE
        } else {
            // Else, bind the existing Task to the view
            _mode.value = Mode.UPDATE
            this.taskId = taskId
            viewModelScope.launch {
                val task = withContext(Dispatchers.Default) {
                    taskDao.findById(taskId)
                }
                taskTitle.value = task.title
                taskContent.value = task.content
            }
        }
    }

    // Save current Task on the Detail screen
    fun saveCurrentTask() {
        // If validation is OK, create or update the current Task on the Detail screen.
        if (isEntryValid()) {
            if (_mode.value == Mode.CREATE) {
                createNewTask()
                _mode.value = Mode.SUCCESS_CREATE
            } else if (_mode.value == Mode.UPDATE) {
                updateExistingTask()
                _mode.value = Mode.SUCCESS_UPDATE
            }
        } else {
            _mode.value = Mode.ERROR_VALIDATION
        }
        // Reset mode
        _mode.value = Mode.DEFAULT
    }

    // validation check for adding new Task
    private fun isEntryValid(): Boolean {
        return !taskTitle.value.isNullOrEmpty()
    }

    // Create new Task from the current Task on the Detail Screen
    private fun createNewTask() {
        // check whether content is inputted
        val newTask = if (taskContent.value != null) {
            Task(title = taskTitle.value!!, content = taskContent.value!!)
        } else {
            Task(title = taskTitle.value!!)
        }
        insertTask(newTask)
    }

    // Update the current existingTask on the Detail Screen
    private fun updateExistingTask() {
        val updatedTask = if (taskContent.value != null) {
            Task(id = taskId, title = taskTitle.value!!, content = taskContent.value!!)
        } else {
            Task(id = taskId, title = taskTitle.value!!)
        }
        updateTask(updatedTask)
    }

    private fun insertTask(task: Task) {
        viewModelScope.launch { taskDao.insert(task) }
    }

    private fun updateTask(task: Task) {
        viewModelScope.launch { taskDao.update(task) }
    }

//    fun deleteTask(task: Task) {
//        viewModelScope.launch { taskDao.delete(task) }
//    }
//
//    fun doneUndoneTask(task: Task, isDone: Boolean) {
//        val newTask = task.copy(isDone = isDone)
//        updateTask(newTask)
//    }

}