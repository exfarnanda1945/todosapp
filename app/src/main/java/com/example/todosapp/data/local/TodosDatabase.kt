package com.example.todosapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [ToDosEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(TodosTypeConverter::class)
abstract class TodosDatabase : RoomDatabase() {
    abstract val dao: TodosDao
}