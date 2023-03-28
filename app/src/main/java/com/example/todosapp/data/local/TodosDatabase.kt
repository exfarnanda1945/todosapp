package com.example.todosapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ToDosEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TodosDatabase:RoomDatabase() {
    abstract val dao:TodosDao
}