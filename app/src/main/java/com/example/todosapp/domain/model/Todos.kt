package com.example.todosapp.domain.model

import com.example.todosapp.data.local.TodosPriority

data class Todos(
    val id: Int = 0,
    val title: String,
    val description: String,
    val date: String,
    val priority: TodosPriority,
    val isArchive:Boolean,
    val isDone:Boolean,
    val deadline: String = ""
)
