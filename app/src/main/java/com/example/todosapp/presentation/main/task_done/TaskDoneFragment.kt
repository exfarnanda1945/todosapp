package com.example.todosapp.presentation.main.task_done

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.todosapp.R
import com.example.todosapp.common.Resource
import com.example.todosapp.databinding.FragmentTaskDoneBinding
import com.example.todosapp.domain.model.Todos
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskDoneFragment : Fragment() {

    private var _binding: FragmentTaskDoneBinding? = null
    private val binding get() = _binding!!

    private val rvAdapter by lazy { TaskDoneAdapter(requireActivity()) }
    private val mViewModel by viewModels<TaskDoneViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskDoneBinding.inflate(inflater, container, false)
        binding.rvTaskDone.apply {
            setHasFixedSize(true)
            adapter = rvAdapter
            layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        }

        mViewModel.taskDoneTodos.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    val data = it.data!!

                    if (data.isEmpty()) {
                        showEmptyScreen(true)
                    } else {
                        rvAdapter.setData(data)
                        val actionMode = TaskDoneActionMode(requireActivity())
                        rvAdapter.actionMode = actionMode
                        actionMode.onActionMenuItemClick =
                            object : TaskDoneActionMode.OnActionMenuItemClick {
                                override fun delete(listTodos: List<Todos>) {
                                    listTodos.map { todos ->
                                        mViewModel.deleteTodos(todos)
                                    }
                                    Toast.makeText(
                                        requireContext(),
                                        getString(R.string.todos_delete_info),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        showEmptyScreen(false)
                    }
                }

                is Resource.Error -> {
                    showEmptyScreen(true)
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
        return binding.root
    }

    private fun showEmptyScreen(show: Boolean) {
        if (show) {
            binding.emptyTodos.isVisible = true
            binding.rvTaskDone.isVisible = false
        } else {
            binding.emptyTodos.isVisible = false
            binding.rvTaskDone.isVisible = true
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        rvAdapter.clearActionMode()
    }
}