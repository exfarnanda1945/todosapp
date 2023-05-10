package com.example.todosapp.presentation.main.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todosapp.common.Resource
import com.example.todosapp.data.local.FilterBy
import com.example.todosapp.data.local.SortBy
import com.example.todosapp.domain.model.Todos
import com.example.todosapp.domain.use_case.TodosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val todosUseCase: TodosUseCase
) : ViewModel() {
    private var _listTodos: MutableLiveData<Resource<List<Todos>>> = MutableLiveData()
    val listTodos = _listTodos


    private var _sortBy =MutableLiveData(SortBy.DATE_ASC)
    val sortBy= _sortBy

    private var _filterBy = MutableLiveData(FilterBy.ALL)
    val filterBy= _filterBy

    private var strSearch = ""


    init {
        searchAndFilterTodos(strSearch)
    }

    fun searchAndFilterTodos(
        search: String
    ) {
        strSearch = search
        viewModelScope.launch {
            todosUseCase.searchAndFilterTodos(filterBy.value!!, sortBy.value!!, search).collect {
                _listTodos.value = it
            }
        }
    }

    fun updateTodos(todos: Todos, isDone: Boolean = false, isArchive: Boolean = false) {
        viewModelScope.launch(Dispatchers.IO) {
            val data = Todos(
                id = todos.id,
                title = todos.title,
                description = todos.description,
                priority = todos.priority,
                isArchive = isArchive,
                isDone = isDone,
                date = todos.date,
                deadline = todos.deadline
            )
            todosUseCase.upsertTodos(data)
        }
    }

    fun deleteTodos(todos: Todos) {
        viewModelScope.launch(Dispatchers.IO) {
            todosUseCase.deleteTodos(todos)
        }
    }

    fun deleteAllTodos() {
        viewModelScope.launch(Dispatchers.IO) {
            todosUseCase.deleteAllTodos()
        }
    }

    fun setFilterBy(filterBy: FilterBy){
        _filterBy.value = filterBy
        searchAndFilterTodos(strSearch)
    }

    fun setSortBy(sortBy: SortBy){
        _sortBy.value = sortBy
        searchAndFilterTodos(strSearch)
    }
}