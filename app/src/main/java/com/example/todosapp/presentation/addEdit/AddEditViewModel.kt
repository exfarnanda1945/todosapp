package com.example.todosapp.presentation.addEdit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todosapp.domain.model.Todos
import com.example.todosapp.domain.use_case.TodosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel @Inject constructor(
    private val todosUseCase:TodosUseCase,
) : ViewModel() {
    fun upsertTodos(todos: Todos) {
        viewModelScope.launch(Dispatchers.IO) {
            todosUseCase.upsertTodos(todos)
        }
    }
}