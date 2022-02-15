package com.example.mytodoapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val isDone: Boolean,
    val title: String,
    val content: String
)
