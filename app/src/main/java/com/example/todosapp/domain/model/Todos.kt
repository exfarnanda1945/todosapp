package com.example.todosapp.domain.model

import com.example.todosapp.data.local.TodosPriority

data class Todos(
    val id: Int = 0,
    val title: String,
    val description: String,
    val priority: TodosPriority,
    val isArchive: Boolean,
    val isDone: Boolean,
    val date: Long? = null,
    val deadline: Long? = null
)
