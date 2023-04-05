package com.example.todosapp.presentation.main.home

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.todosapp.R
import com.example.todosapp.databinding.FragmentTodosItemBottomSheetBinding
import com.example.todosapp.domain.model.Todos
import com.example.todosapp.presentation.addEdit.AddEditActivityArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TodosItemBottomSheet : BottomSheetDialogFragment() {
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
            }
            archiveLayout.setOnClickListener {
                homeViewModel.updateTodos(todosParam, isArchive = true)
                this@TodosItemBottomSheet.dismiss()
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
        val alertBuilder = AlertDialog.Builder(requireContext())
        alertBuilder.apply {
            setTitle(getString(R.string.delete))
            setMessage(getString(R.string.delete_confirm))
            setNegativeButton(getString(R.string.no)) { _, _ -> }
            setPositiveButton(getString(R.string.yes)) { _, _ ->
                homeViewModel.deleteTodos(todos)
                Toast.makeText(
                    requireContext(), getString(R.string.todos_delete_info),
                    Toast.LENGTH_SHORT
                ).show()
                this@TodosItemBottomSheet.dismiss()
            }
        }

        val dialog = alertBuilder.create()
        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).apply {
            setTextColor(ContextCompat.getColor(requireContext(), R.color.blue))
        }
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).apply {
            setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blue))
            setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        }

    }


}