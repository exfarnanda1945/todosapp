package com.example.todosapp.presentation.util

import com.example.todosapp.domain.model.Todos

data class TodosListState(
    val listTodos: List<Todos> = emptyList(),
    val error: String = "",
)
