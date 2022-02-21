package com.example.mytodoapp.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytodoapp.ToDoApplication
import com.example.mytodoapp.data.Task
import com.example.mytodoapp.data.TaskDao
import kotlinx.coroutines.launch

class TaskDetailViewModel : ViewModel() {

    // Values for View state
    private val _isEntryValid: MutableLiveData<Boolean> = MutableLiveData()
    val isEntryValid: LiveData<Boolean> = _isEntryValid
    private val _isBackToList: MutableLiveData<Boolean> = MutableLiveData()
    val isBackToList: LiveData<Boolean> = _isBackToList

    // Values for Task data
    val taskTitle: MutableLiveData<String> = MutableLiveData()
    val taskContent: MutableLiveData<String> = MutableLiveData()

    // initialize values for view state
    init {
        _isEntryValid.value = null
        _isBackToList.value = null
    }

    // DAO to handle Task table
    private val taskDao: TaskDao = ToDoApplication.database.taskDao()

    // add new Task to Room and update values for view state
    fun addNewTask() {
        if (isEntryValid()) {
            _isEntryValid.value = true
            // check whether content is inputted
            val newTask = if (taskContent.value != null) {
                Task(title = taskTitle.value!!, content = taskContent.value!!)
            } else {
                Task(title = taskTitle.value!!)
            }
            insertTask(newTask)
            _isBackToList.value = true
        } else {
            _isEntryValid.value = false
            _isEntryValid.value = null
        }
    }

    // validation check for adding new Task
    private fun isEntryValid(): Boolean {
        return !taskTitle.value.isNullOrEmpty()
    }

    private fun insertTask(task: Task) {
        viewModelScope.launch { taskDao.insert(task) }
    }

//    private fun updateTask(task: Task) {
//        viewModelScope.launch { taskDao.update(task) }
//    }

//    fun deleteTask(task: Task) {
//        viewModelScope.launch { taskDao.delete(task) }
//    }
//
//    fun doneUndoneTask(task: Task, isDone: Boolean) {
//        val newTask = task.copy(isDone = isDone)
//        updateTask(newTask)
//    }

}