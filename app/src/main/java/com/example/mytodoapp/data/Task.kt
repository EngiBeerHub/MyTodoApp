package com.example.mytodoapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val isDone: Boolean = false,
    val title: String,
    val content: String?,
    val deadLine: String?
)
