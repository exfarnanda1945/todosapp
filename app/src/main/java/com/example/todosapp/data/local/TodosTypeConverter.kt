package com.example.todosapp.data.local

import androidx.room.TypeConverter
import java.util.*

object TodosTypeConverter {
    @TypeConverter
    fun fromDate(value: Date?): Long? {
        return value?.time
    }

    @TypeConverter
    fun toDate(value: Long?): Date? {
        return if (value != null) Date(value) else null
    }

    @TypeConverter
    fun fromTodosPriority(value: TodosPriority): Int = when (value) {
        TodosPriority.LOW -> TodosPriority.LOW.ordinal
        TodosPriority.MEDIUM -> TodosPriority.MEDIUM.ordinal
        TodosPriority.HIGH -> TodosPriority.HIGH.ordinal
    }

    @TypeConverter
    fun toTodosPriority(value: Int): TodosPriority = when (value) {
        TodosPriority.LOW.ordinal -> TodosPriority.LOW
        TodosPriority.MEDIUM.ordinal -> TodosPriority.MEDIUM
        TodosPriority.HIGH.ordinal -> TodosPriority.HIGH
        else -> TodosPriority.LOW
    }

}