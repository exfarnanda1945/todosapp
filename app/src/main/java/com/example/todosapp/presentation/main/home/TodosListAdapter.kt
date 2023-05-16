package com.example.todosapp.presentation.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.example.todosapp.R
import com.example.todosapp.common.AdapterDiffer
import com.example.todosapp.common.Utils
import com.example.todosapp.data.local.TodosPriority
import com.example.todosapp.databinding.TodosCardBinding
import com.example.todosapp.domain.model.Todos
import com.example.todosapp.presentation.util.ColorInfoTodosCard

class TodosListAdapter : RecyclerView.Adapter<TodosListAdapter.TodosListViewHolder>() {

     lateinit var todosCardEvent: TodosCardEvent

    private val differ = AsyncListDiffer(this, AdapterDiffer.diffCallBack)

    inner class TodosListViewHolder(private val binding: TodosCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Todos) {
            binding.titleTodosCard.text = item.title
            binding.descTodosCard.text = item.description
            binding.todosCreatedDate.text = Utils.convertLongToTime(item.date!!).replace("."," ")
            binding.txtPriority.apply {
                val color = getColorPriority(item.priority)
                text = item.priority.name
                setBackgroundColor(ContextCompat.getColor(context, color.backgroundColor))
                setTextColor(ContextCompat.getColor(context, color.textColor))
            }

            if (item.deadline != null) {
                binding.txtDeadline.apply {
                    text = Utils.convertLongToTime(item.deadline)
                        .replace(".", " ")
                    setTextColor(ContextCompat.getColor(context, R.color.red_vermilion))
                    setBackgroundColor(ContextCompat.getColor(context, R.color.pastel_red))
                }
            } else {
                binding.txtDeadline.apply {
                    text = context.getString(R.string.no_deadline)
                    setTextColor(ContextCompat.getColor(context, R.color.may_green))
                    setBackgroundColor(ContextCompat.getColor(context, R.color.green_alabaster))
                }
            }
            binding.todosCard.setOnClickListener {
                todosCardEvent.onItemClick(item)
            }
            binding.itemMenu.setOnClickListener {
                todosCardEvent.onItemMenuClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodosListViewHolder {
        return TodosListViewHolder(
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

    override fun onBindViewHolder(holder: TodosListViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    private fun getColorPriority(todosPriority: TodosPriority): ColorInfoTodosCard {
        return when (todosPriority) {
            TodosPriority.LOW -> ColorInfoTodosCard(R.color.blue, R.color.alice_blue)
            TodosPriority.MEDIUM -> ColorInfoTodosCard(R.color.marigold, R.color.cosmic_late)
            TodosPriority.HIGH -> ColorInfoTodosCard(R.color.red_vermilion, R.color.pink_linen)
        }
    }

    fun setData(todos: List<Todos>) {
        differ.submitList(todos)
    }

    interface TodosCardEvent {
        fun onItemClick(todos:Todos)
        fun onItemMenuClick(todos:Todos)
    }

}