package com.example.todosapp.presentation.main.archive

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
import com.example.todosapp.databinding.FragmentArchiveBinding
import com.example.todosapp.domain.model.Todos
import com.example.todosapp.presentation.base.TodosActionMode
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArchiveFragment : Fragment() {

    private var _binding:FragmentArchiveBinding? = null
    private val binding get() = _binding!!

    private val archiveAdapter by lazy { ArchiveAdapter(requireActivity()) }
    private val archiveViewModel by viewModels<ArchiveViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArchiveBinding.inflate(layoutInflater,container,false)

        binding.rvArchive.apply {
            setHasFixedSize(true)
            adapter = archiveAdapter
            layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        }

        archiveViewModel.taskDoneTodos.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    val data = it.data!!

                    if (data.isEmpty()) {
                        showEmptyScreen(true)
                    } else {
                        archiveAdapter.setData(data)
                        val actionMode = ArchiveActionMode(requireActivity())
                        archiveAdapter.actionMode = actionMode
                        actionMode.onActionMenuItemClick =
                            object : TodosActionMode.OnActionMenuItemClick {
                                override fun delete(listTodos: List<Todos>) {
                                    listTodos.map { todos ->
                                        archiveViewModel.deleteTodos(todos)
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
                else -> {}
            }
        }

        return binding.root
    }

    private fun showEmptyScreen(show: Boolean) {
        if (show) {
            binding.emptyTodos.isVisible = true
            binding.rvArchive.isVisible = false
        } else {
            binding.emptyTodos.isVisible = false
            binding.rvArchive.isVisible = true
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        archiveAdapter.clearActionMode()
    }

}