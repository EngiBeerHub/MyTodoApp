package com.example.mytodoapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("select * from Task")
    fun getAll(): Flow<List<Task>>

    @Query("select * from Task where id = :id")
    suspend fun findById(id: Int): Task

    @Query("select count(*) from Task")
    suspend fun getRowCount(): Int

    @Insert
    suspend fun insert(task: Task)

    @Insert
    suspend fun insertAll(vararg tasks: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)
}