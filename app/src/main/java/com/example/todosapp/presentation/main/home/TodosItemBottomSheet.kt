package com.example.todosapp.presentation.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.todosapp.R
import com.example.todosapp.common.Utils
import com.example.todosapp.databinding.FragmentTodosItemBottomSheetBinding
import com.example.todosapp.domain.model.Todos
import com.example.todosapp.presentation.addEdit.AddEditActivityArgs
import com.example.todosapp.presentation.base.BaseBottomDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TodosItemBottomSheet : BaseBottomDialogFragment() {
    private lateinit var todosParam: Todos
    private var _binding: FragmentTodosItemBottomSheetBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel by activityViewModels<HomeViewModel>()
    private val todosArgs: AddEditActivityArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (todosArgs.todos != null) {
            todosParam = todosArgs.todos!!
        }

        _binding = FragmentTodosItemBottomSheetBinding.inflate(inflater, container, false)
        binding.apply {
            doneLayout.setOnClickListener {
                homeViewModel.updateTodos(todosParam, true)
                this@TodosItemBottomSheet.dismiss()
                showToast(false, getString(R.string.todos_done_info))
            }
            archiveLayout.setOnClickListener {
                homeViewModel.updateTodos(todosParam, isArchive = true)
                this@TodosItemBottomSheet.dismiss()
                showToast(false, getString(R.string.todos_archive_info))
            }
            deleteLayout.setOnClickListener {
                deleteTodos(todosParam)
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun deleteTodos(todos: Todos) {
        Utils.createAlertDialog(requireContext(), R.string.delete, R.string.delete_confirm, {
            homeViewModel.deleteTodos(todos)
            this@TodosItemBottomSheet.dismiss()
            showToast(false, getString(R.string.todos_delete_info))
        }, {}, R.color.blue)
    }


}