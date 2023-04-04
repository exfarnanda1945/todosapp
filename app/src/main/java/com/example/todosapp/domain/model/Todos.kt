package com.example.todosapp.domain.model

import android.os.Parcelable
import com.example.todosapp.data.local.TodosPriority
import kotlinx.parcelize.Parcelize

@Parcelize
data class Todos(
    var id: Int = 0,
    val title: String,
    val description: String,
    val priority: TodosPriority,
    val isArchive: Boolean,
    val isDone: Boolean,
    val date: Long? = null,
    val deadline: Long? = null
):Parcelable
