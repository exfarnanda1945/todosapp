package com.example.todosapp.presentation.main.task_done

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.todosapp.domain.model.Todos
import com.example.todosapp.domain.use_case.TodosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskDoneViewModel @Inject constructor( private val todosUseCase: TodosUseCase) : ViewModel() {
    val taskDoneTodos = todosUseCase.getListDoneTodos().asLiveData()

    fun deleteTodos(todos: Todos) {
        viewModelScope.launch(Dispatchers.IO) {
            todosUseCase.deleteTodos(todos)
        }
    }
}