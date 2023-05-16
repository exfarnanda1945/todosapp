package com.example.todosapp.domain.use_case

import com.example.todosapp.common.Resource
import com.example.todosapp.data.local.FilterBy
import com.example.todosapp.data.local.SortBy
import com.example.todosapp.domain.model.Todos
import kotlinx.coroutines.flow.Flow

interface ITodosUseCase {
    fun searchAndFilterTodos(
        filter: FilterBy,
        sort: SortBy,
        search: String
    ): Flow<Resource<List<Todos>>>

    suspend fun upsertTodos(todos: Todos)
    suspend fun deleteTodos(todos: Todos)
    suspend fun deleteAllTodos()

    fun getListDoneTodos(): Flow<Resource<List<Todos>>>
}