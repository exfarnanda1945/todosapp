package com.example.todosapp.presentation.main.task_done

import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.todosapp.R
import com.example.todosapp.domain.model.Todos
import com.google.android.material.card.MaterialCardView

class TaskDoneActionMode(
    private val requireActivity: FragmentActivity
) : ActionMode.Callback {
    lateinit var mActionMode: ActionMode
    lateinit var onActionMenuItemClick: OnActionMenuItemClick

    private val selectionTodos = mutableListOf<Todos>()
    var multiSelection = false

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mActionMode = mode!!
        mode.menuInflater.inflate(R.menu.contextual_menu, menu)
        applyStatusBarColor(R.color.contextualStatusBarColor)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        if (item?.itemId == R.id.contextual_delete) {
            onActionMenuItemClick.delete(selectionTodos)
            multiSelection = false
            mActionMode.finish()
        }
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        multiSelection = false
        selectionTodos.clear()
        applyStatusBarColor(R.color.white)
    }

    fun clearActionMode() {
        if (this::mActionMode.isInitialized){
            mActionMode.finish()
        }
    }

    private fun changeTodosCardStyle(backgroundColor: Int, cardLayout: MaterialCardView) {
        cardLayout.setCardBackgroundColor(
            ContextCompat.getColor(
                requireActivity,
                backgroundColor
            )
        )
    }


    private fun changeTitleToolbar() {
        if (selectionTodos.isEmpty()) {
            multiSelection = false
            clearActionMode()
        } else {
            mActionMode.title = "${selectionTodos.size} items selected."
        }
    }

    fun addToSelection(item: Todos, cardLayout: MaterialCardView) {
        val listTodos = selectionTodos
        if (listTodos.contains(item)) {
            listTodos.remove(item)
            changeTitleToolbar()
            changeTodosCardStyle(R.color.white, cardLayout)
        } else {
            listTodos.add(item)
            changeTitleToolbar()
            changeTodosCardStyle(R.color.contextualCardSelected, cardLayout)
        }
    }

    interface OnActionMenuItemClick {
        fun delete(listTodos: List<Todos>)
    }

    private fun  applyStatusBarColor(color: Int) {
        requireActivity.window.statusBarColor = ContextCompat.getColor(requireActivity, color)
    }

}