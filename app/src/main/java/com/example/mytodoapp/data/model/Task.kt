package com.example.mytodoapp.data.model

import androidx.lifecycle.MutableLiveData

data class Task(
    val id: Int,
    val isDone: Boolean,
//    val isDone: MutableLiveData<Boolean> = MutableLiveData(false),
    val title: String,
    val content: String
)
