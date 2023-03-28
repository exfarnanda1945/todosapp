package com.example.todosapp.data.local

import androidx.room.*
import com.example.todosapp.common.Constants.TABLE_NAME

@Dao
interface TodosDao {
    @Query("SELECT * FROM $TABLE_NAME")
    suspend fun getListTodos(): List<ToDosEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createTodos(todos: ToDosEntity)

    @Update
    suspend fun updateTodos(todos:ToDosEntity)

    @Delete
    suspend fun deleteTodos(todos: ToDosEntity)
}