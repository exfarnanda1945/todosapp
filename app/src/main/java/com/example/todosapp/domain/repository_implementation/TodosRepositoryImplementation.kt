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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.transform
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
        return dao.searchAndFilterTodos(SimpleSQLiteQuery(query))
            .transform { value ->
                emit(Resource.Success(value.map { item -> item.toTodos() }) as Resource<List<Todos>>)
            }
            .catch { value ->
                emit(Resource.Error(value.localizedMessage ?: "Unexpected error"))
            }
            .flowOn(Dispatchers.IO)
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

    override fun getListDoneTodos(): Flow<Resource<List<Todos>>> {
        return dao.getListDoneTodos()
            .transform {
            emit(Resource.Success(it.map { value -> value.toTodos() }) as Resource<List<Todos>>)
            }
            .catch { value ->
            emit(Resource.Error(value.localizedMessage ?: "Unexpected error"))
        }
            .flowOn(Dispatchers.IO)
    }

    override fun getListArchiveTodos(): Flow<Resource<List<Todos>>> {
        return dao.getListArchiveTodos()
            .transform {
                emit(Resource.Success(it.map { value -> value.toTodos() }) as Resource<List<Todos>>)
            }
            .catch { value ->
                emit(Resource.Error(value.localizedMessage ?: "Unexpected error"))
            }
            .flowOn(Dispatchers.IO)
    }


}