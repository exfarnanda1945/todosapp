package com.example.todosapp.presentation.main.archive

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
class ArchiveViewModel @Inject constructor(private val todosUseCase: TodosUseCase) : ViewModel() {
    val taskDoneTodos = todosUseCase.getListArchiveTodos().asLiveData()

    fun deleteTodos(todos: Todos) {
        viewModelScope.launch(Dispatchers.IO) {
            todosUseCase.deleteTodos(todos)
        }
    }
}