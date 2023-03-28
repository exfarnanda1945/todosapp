package com.example.todosapp.domain.use_case

import com.example.todosapp.base.Resource
import com.example.todosapp.domain.model.Todos
import com.example.todosapp.domain.repository.TodosRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class TodosUseCase @Inject constructor(
    private val todosRepository: TodosRepository
) {
    suspend fun getListTodos(): Flow<Resource<List<Todos>>> {
        return todosRepository.getListTodos()
    }

    suspend fun createTodos(todos: Todos) {
        return todosRepository.createTodos(todos)
    }

    suspend fun updateTodos(todos: Todos) {
        return todosRepository.updateTodos(todos)
    }

    suspend fun deleteTodos(todos: Todos) {
        return todosRepository.deleteTodos(todos)
    }
}