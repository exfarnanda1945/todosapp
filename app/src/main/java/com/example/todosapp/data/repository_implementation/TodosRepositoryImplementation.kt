package com.example.todosapp.data.repository_implementation

import com.example.todosapp.base.Resource
import com.example.todosapp.data.local.TodosDao
import com.example.todosapp.data.mapper.toTodos
import com.example.todosapp.data.mapper.toTodosEntity
import com.example.todosapp.domain.model.Todos
import com.example.todosapp.domain.repository.TodosRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

class TodosRepositoryImplementation @Inject constructor(
    private val dao: TodosDao
) : TodosRepository {
    override suspend fun createTodos(todos: Todos) {
        try {
            val todosEntity = todos.toTodosEntity()
            dao.createTodos(todosEntity)
        } catch (e: Exception) {
            Timber.d(e)
        }
    }

    override suspend fun getListTodos(): Flow<Resource<List<Todos>>> = flow {
        emit(Resource.Loading())
        try {
            val getTodos = dao.getListTodos()
            val todos = getTodos.map { it.toTodos() }
            emit(Resource.Success(todos))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Unexpected error from cache"))
            Timber.d(e)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun updateTodos(todos: Todos) {
        try {
            val todosEntity = todos.toTodosEntity()
            dao.updateTodos(todosEntity)
        } catch (e: Exception) {
            Timber.d(e)
        }
    }

    override suspend fun deleteTodos(todos: Todos) {
        try {
            val todosEntity = todos.toTodosEntity()
            dao.deleteTodos(todosEntity)
        } catch (e: Exception) {
            Timber.d(e)
        }
    }
}