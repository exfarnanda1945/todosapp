package com.example.todosapp.domain.use_case

import com.example.todosapp.data.repository.TodosRepository
import com.example.todosapp.domain.model.Todos
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class DeleteTodosUseCase @Inject constructor(
    private val todosRepository: TodosRepository
) {
    suspend operator fun invoke(todos: Todos) {
        return todosRepository.deleteTodos(todos)
    }
}