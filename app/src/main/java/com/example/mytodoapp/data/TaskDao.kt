package com.example.mytodoapp.data

import androidx.paging.PagingSource
import androidx.room.*

@Dao
interface TaskDao {
    @Query("select * from Task")
    fun getAll(): PagingSource<Int, Task>

    @Query("select * from Task where id = :id")
    suspend fun findById(id: Int): Task

    @Query("select count(*) from Task")
    suspend fun getRowCount(): Int

    @Insert
    suspend fun insert(task: Task)

    @Insert
    suspend fun insertAndRetrieve(task: Task): Long

    @Insert
    suspend fun insertAll(vararg tasks: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)
}