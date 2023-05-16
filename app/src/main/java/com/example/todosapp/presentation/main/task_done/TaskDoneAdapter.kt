package com.example.todosapp.presentation.main.task_done


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.example.todosapp.R
import com.example.todosapp.common.AdapterDiffer
import com.example.todosapp.common.Utils
import com.example.todosapp.data.local.TodosPriority
import com.example.todosapp.databinding.TodosCardBinding
import com.example.todosapp.domain.model.Todos
import com.example.todosapp.presentation.util.ColorInfoTodosCard

class TaskDoneAdapter(
    private val requireActivity: FragmentActivity,
) : RecyclerView.Adapter<TaskDoneAdapter.MainViewHolder>() {

    lateinit var actionMode: TaskDoneActionMode

    private val differ = AsyncListDiffer(this, AdapterDiffer.diffCallBack)

    inner class MainViewHolder(private val binding: TodosCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val cardLayout = binding.root

        fun bind(item: Todos) {
            binding.itemMenu.isVisible = false
            binding.titleTodosCard.text = item.title
            binding.descTodosCard.text = item.description
            binding.todosCreatedDate.text = Utils.convertLongToTime(item.date!!).replace(".", " ")
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

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
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

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.bind(item)
        holder.cardLayout.apply {
            setOnClickListener {
                if (actionMode.multiSelection) {
                   actionMode.addToSelection(item, this@apply)
                }
            }

            setOnLongClickListener {
                if (actionMode.multiSelection){
                  actionMode.addToSelection(item, this@apply)
                }else{
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

    fun clearActionMode(){
        if(this::actionMode.isInitialized){
            actionMode.clearActionMode()
        }
    }

    private fun getColorPriority(todosPriority: TodosPriority): ColorInfoTodosCard {
        return when (todosPriority) {
            TodosPriority.LOW -> ColorInfoTodosCard(R.color.blue, R.color.alice_blue)
            TodosPriority.MEDIUM -> ColorInfoTodosCard(R.color.marigold, R.color.cosmic_late)
            TodosPriority.HIGH -> ColorInfoTodosCard(R.color.red_vermilion, R.color.pink_linen)
        }
    }

}