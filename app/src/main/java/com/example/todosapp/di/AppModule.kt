package com.example.todosapp.di

import com.example.todosapp.data.local.TodosDao
import com.example.todosapp.domain.repository_implementation.TodosRepositoryImplementation
import com.example.todosapp.data.repository.TodosRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideTodosRepositoryImplementation(dao:TodosDao): TodosRepository = TodosRepositoryImplementation(dao)
}