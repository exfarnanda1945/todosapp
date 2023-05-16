package com.example.todosapp.common

import androidx.recyclerview.widget.DiffUtil
import com.example.todosapp.domain.model.Todos

object AdapterDiffer {

     val diffCallBack = object : DiffUtil.ItemCallback<Todos>() {
        override fun areItemsTheSame(oldItem: Todos, newItem: Todos): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Todos, newItem: Todos): Boolean {
            return oldItem == newItem
        }

    }
}