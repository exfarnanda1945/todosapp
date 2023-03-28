package com.example.todosapp.domain.repository

import com.example.todosapp.base.Resource
import com.example.todosapp.domain.model.Todos
import kotlinx.coroutines.flow.Flow

interface TodosRepository {
    suspend fun createTodos(todos: Todos)
    suspend fun getListTodos(): Flow<Resource<List<Todos>>>
    suspend fun updateTodos(todos: Todos)
    suspend fun deleteTodos(todos: Todos)
}