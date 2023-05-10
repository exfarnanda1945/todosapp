package com.example.todosapp.domain.use_case

import com.example.todosapp.common.Resource
import com.example.todosapp.data.local.FilterBy
import com.example.todosapp.data.local.SortBy
import com.example.todosapp.data.repository.TodosRepository
import com.example.todosapp.domain.model.Todos
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class TodosUseCase @Inject constructor(
    private val repository: TodosRepository
) : ITodosUseCase {

    override fun searchAndFilterTodos(
        filter: FilterBy,
        sort: SortBy,
        search: String
    ): Flow<Resource<List<Todos>>> {
        return repository.searchAndFilterTodos(filter, sort, search)
    }

    override suspend fun upsertTodos(todos: Todos) {
        return repository.upsertTodos(todos)
    }

    override suspend fun deleteTodos(todos: Todos) {
        return repository.deleteTodos(todos)
    }

    override suspend fun deleteAllTodos() {
        return repository.deleteAllTodos()
    }

}