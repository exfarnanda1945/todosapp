package com.example.todosapp.presentation.main.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todosapp.common.Resource
import com.example.todosapp.domain.use_case.GetListTodosUseCase
import com.example.todosapp.presentation.util.TodosListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getListTodosUseCase: GetListTodosUseCase
) : ViewModel() {
    private var _listTodos = MutableLiveData(TodosListState())
    val listTodos = _listTodos

    init {
        getListTodos()
    }

    private fun getListTodos() {
        viewModelScope.launch {
            getListTodosUseCase().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        val data = result.data!!
                        if (data.isEmpty()) {
                            _listTodos.value =
                                TodosListState(error = "Todos Empty")
                        }
                        _listTodos.value = TodosListState(data)
                    }
                    is Resource.Error -> {
                        _listTodos.value =
                            TodosListState(error = result.message.toString())
                    }
                }
            }
        }
    }
}