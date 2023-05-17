package com.example.todosapp.presentation.main.task_done


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.example.todosapp.common.AdapterDiffer
import com.example.todosapp.databinding.TodosCardBinding
import com.example.todosapp.domain.model.Todos

class TaskDoneAdapter(
    private val requireActivity: FragmentActivity,
) : RecyclerView.Adapter<TaskDoneViewHolder>() {

    lateinit var actionMode: TaskDoneActionMode
    private val differ = AsyncListDiffer(this, AdapterDiffer.diffCallBack)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskDoneViewHolder {
        return TaskDoneViewHolder(
            TodosCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: TaskDoneViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.bind(item)
        holder.cardLayout.apply {
            setOnClickListener {
                if (actionMode.multiSelection) {
                    actionMode.addToSelection(item, this@apply)
                }
            }

            setOnLongClickListener {
                if (actionMode.multiSelection) {
                    actionMode.addToSelection(item, this@apply)
                } else {
                    requireActivity.startActionMode(actionMode)
                    actionMode.multiSelection = true
                    actionMode.addToSelection(item, this@apply)
                }
                true
            }
        }
    }

    fun setData(items: List<Todos>) {
        differ.submitList(items)
    }

    fun clearActionMode() {
        if (this::actionMode.isInitialized) {
            actionMode.clearActionMode()
        }
    }
}

