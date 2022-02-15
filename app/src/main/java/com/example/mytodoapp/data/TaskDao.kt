package com.example.mytodoapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("select * from Task")
    fun getAll(): Flow<List<Task>>

    @Query("select * from Task where id = :id")
    fun findById(id: Int): Task

    @Insert
    fun insert(task: Task)

    @Update
    fun update(task: Task)

    @Delete
    fun delete(task: Task)
}