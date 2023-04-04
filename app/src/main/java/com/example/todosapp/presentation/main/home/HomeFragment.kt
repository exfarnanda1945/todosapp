package com.example.todosapp.presentation.main.home

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.todosapp.R
import com.example.todosapp.databinding.FragmentHomeBinding
import com.example.todosapp.domain.model.Todos
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel by viewModels<HomeViewModel>()
    private val todosAdapter by lazy {
        TodosListAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupHomeMenu()
        binding.btnAdd.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAddEditActivity(null))
        }

        binding.rvTodos.apply {
            setHasFixedSize(true)
            adapter = todosAdapter
            layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        }

        homeViewModel.listTodos.observe(viewLifecycleOwner) { result ->
            when {
                result.listTodos.isNotEmpty() -> {
                    binding.emptyTodos.isVisible = false
                    binding.rvTodos.isVisible = true
                    todosAdapter.setData(result.listTodos)
                    todosAdapter.todosCardEvent = object : TodosListAdapter.TodosCardEvent {
                        override fun onItemClick(todos: Todos) {
                            val action =
                                HomeFragmentDirections.actionHomeFragmentToAddEditActivity(todos)
                            findNavController().navigate(action)
                        }

                    }
                }
                result.listTodos.isEmpty() -> {
                    binding.emptyTodos.isVisible = true
                    binding.rvTodos.isVisible = false
                }
                result.error.isNotEmpty() -> {
                    binding.emptyTodos.isVisible = true
                    binding.rvTodos.isVisible = false
                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        return binding.root
    }

    private fun setupHomeMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.home_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

}