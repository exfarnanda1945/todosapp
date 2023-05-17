package com.example.todosapp.presentation.base

import com.example.todosapp.domain.model.Todos
import com.google.android.material.card.MaterialCardView

interface ITodosActionMode {
    fun clearActionMode()
    fun changeTitleToolbar()
    fun changeTodosCardStyle(backgroundColor: Int, cardLayout: MaterialCardView)
    fun addToSelection(item: Todos, cardLayout: MaterialCardView)
    fun  applyStatusBarColor(color: Int)
}