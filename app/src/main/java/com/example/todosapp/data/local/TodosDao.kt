package com.example.todosapp.data.local

import androidx.room.*
import com.example.todosapp.common.Constants.TABLE_NAME

@Dao
interface TodosDao {
    @Query("SELECT * FROM $TABLE_NAME WHERE isArchive = 0 AND isDone = 0 ORDER BY date DESC")
    fun getListTodos(): List<ToDosEntity>

    @Upsert
    suspend fun createTodos(todos: ToDosEntity)

    @Delete
    suspend fun deleteTodos(todos: ToDosEntity)
}