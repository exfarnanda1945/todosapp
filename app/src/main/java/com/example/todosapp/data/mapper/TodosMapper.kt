package com.example.todosapp.data.mapper

import com.example.todosapp.data.local.ToDosEntity
import com.example.todosapp.domain.model.Todos

fun ToDosEntity.toTodos(): Todos {
    return Todos(id, title, description, date, priority, isArchive, isDone, deadline)
}

fun Todos.toTodosEntity(): ToDosEntity {
    return ToDosEntity(id, title, description, date, priority, isArchive, isDone, deadline)
}