package com.example.todosapp.data.local

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.todosapp.common.Constants.TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface TodosDao {
    @Query("SELECT * FROM $TABLE_NAME WHERE isArchive = 0 AND isDone = 0 ORDER BY date DESC")
    fun getListTodos(): Flow<List<ToDosEntity>>

    @Upsert
    suspend fun createTodos(todos: ToDosEntity)

    @Delete
    suspend fun deleteTodos(todos: ToDosEntity)

    @Query("DELETE FROM $TABLE_NAME")
    suspend fun deleteAll()

    @RawQuery(observedEntities = [ToDosEntity::class])
    fun searchAndFilterTodos(query: SupportSQLiteQuery): Flow<List<ToDosEntity>>
}