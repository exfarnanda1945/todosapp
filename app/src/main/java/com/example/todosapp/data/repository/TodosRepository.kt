package com.example.todosapp.data.repository

import com.example.todosapp.common.Resource
import com.example.todosapp.domain.model.Todos
import kotlinx.coroutines.flow.Flow

interface TodosRepository {
    suspend fun upsertTodos(todos:Todos)
     fun getListTodos(): Flow<Resource<List<Todos>>>
    suspend fun deleteTodos(todos: Todos)
}