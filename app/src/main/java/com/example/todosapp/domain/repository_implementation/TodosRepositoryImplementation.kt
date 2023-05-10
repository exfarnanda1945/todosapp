package com.example.todosapp.domain.repository_implementation

import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.todosapp.common.Resource
import com.example.todosapp.data.local.FilterBy
import com.example.todosapp.data.local.SortBy
import com.example.todosapp.data.local.TodosDao
import com.example.todosapp.data.local.searchAndFilerQueryBuilder
import com.example.todosapp.data.mapper.toTodos
import com.example.todosapp.data.mapper.toTodosEntity
import com.example.todosapp.data.repository.TodosRepository
import com.example.todosapp.domain.model.Todos
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class TodosRepositoryImplementation @Inject constructor(
    private val dao: TodosDao
) : TodosRepository {
    override suspend fun upsertTodos(todos: Todos) {
        try {
            val todosEntity = todos.toTodosEntity()
            dao.createTodos(todosEntity)
        } catch (e: Exception) {
            Timber.d(e)
        }
    }

    override suspend fun deleteTodos(todos: Todos) {
        try {
            val toDosEntity = todos.toTodosEntity()
            dao.deleteTodos(toDosEntity)
        } catch (e: Exception) {
            Timber.d(e)
        }
    }

    override fun searchAndFilterTodos(
        filter: FilterBy,
        sort: SortBy,
        search: String
    ): Flow<Resource<List<Todos>>> {
        val query = searchAndFilerQueryBuilder(filter, sort, search)
        return try {
            dao.searchAndFilterTodos(SimpleSQLiteQuery(query))
                .map { Resource.Success(it.map { value -> value.toTodos() }) }
                .flowOn(Dispatchers.IO)
        } catch (e: Exception) {
            flow<Resource<List<Todos>>> {
                emit(Resource.Error(e.localizedMessage ?: "Unexpected error"))
            }.flowOn(Dispatchers.IO)
        }
    }

    override suspend fun deleteAllTodos() {
        return try {
            dao.deleteAll()
        } catch (
            e: Exception
        ) {
            Timber.d(e)
        }
    }

}