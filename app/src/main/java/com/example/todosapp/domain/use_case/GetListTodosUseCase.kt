package com.example.todosapp.domain.use_case

import com.example.todosapp.common.Resource
import com.example.todosapp.data.repository.TodosRepository
import com.example.todosapp.domain.model.Todos
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class GetListTodosUseCase @Inject constructor(
    private val todosRepository: TodosRepository
) {
    operator fun invoke(): Flow<Resource<List<Todos>>> {
        return todosRepository.getListTodos()
    }
}