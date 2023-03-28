package com.example.todosapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todosapp.common.Constants.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class ToDosEntity(
    @PrimaryKey
    val id: Int = 0,
    val title: String,
    val description: String,
    val date: String,
    val priority: TodosPriority,
    val isArchive:Boolean,
    val isDone:Boolean,
    val deadline: String = ""
)

enum class TodosPriority {
    LOW,
    MEDIUM,
    HIGH
}
