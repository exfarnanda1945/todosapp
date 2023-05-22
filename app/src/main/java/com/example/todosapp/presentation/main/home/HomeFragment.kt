package com.example.todosapp.presentation.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.forEach
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.todosapp.R
import com.example.todosapp.common.Resource
import com.example.todosapp.common.Utils
import com.example.todosapp.data.local.FilterBy
import com.example.todosapp.data.local.SortBy
import com.example.todosapp.databinding.FragmentHomeBinding
import com.example.todosapp.domain.model.Todos
import com.example.todosapp.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : BaseFragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel by activityViewModels<HomeViewModel>()
    private val todosAdapter by lazy {
        TodosListAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.btnAdd.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToAddEditActivity(
                    null
                )
            )
        }

        binding.rvTodos.apply {
            setHasFixedSize(true)
            adapter = todosAdapter
            layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        }

        homeViewModel.listTodos.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Success -> {
                    val data = result.data!!

                    if (data.isEmpty()) {
                        showEmptyScreen(true)
                    }

                    if (data.isNotEmpty()) {
                        showEmptyScreen(false)
                        todosAdapter.setData(result.data)
                        todosAdapter.todosCardEvent = object : TodosListAdapter.TodosCardEvent {
                            override fun onItemClick(todos: Todos) {
                                val action =
                                    HomeFragmentDirections.actionHomeFragmentToAddEditActivity(todos)
                                findNavController().navigate(action)
                            }

                            override fun onItemMenuClick(todos: Todos) {
                                val action =
                                    HomeFragmentDirections.actionHomeFragmentToTodosItemBottomSheet(
                                        todos
                                    )
                                findNavController().navigate(action)
                            }
                        }
                    }
                }

                is Resource.Error -> {
                    showEmptyScreen(true)
                    showToast(true, result.message!!)
                }

                else -> {}
            }
        }


        setupHomeMenu()
        return binding.root
    }


    private fun setupHomeMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.home_menu, menu)

                homeViewModel.sortBy.observe(viewLifecycleOwner) { sortBy ->
                    setSelectedSubMenu(menu, R.id.sortByMenu, setSortMenuItemId(sortBy))
                }
                homeViewModel.filterBy.observe(viewLifecycleOwner) { filterBy ->
                    setSelectedSubMenu(menu, R.id.filterByMenu, setFilterMenuItemId(filterBy))
                }


                val searchMenu = menu.findItem(R.id.search)
                val searchView = searchMenu.actionView as SearchView
                searchView.isIconified = true
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        homeViewModel.searchAndFilterTodos(
                            query ?: ""
                        )
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        homeViewModel.searchAndFilterTodos(
                            newText!!
                        )
                        return true
                    }

                })
            }


            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.deleteAllTodos -> {
                        Utils.createAlertDialog(
                            context = requireContext(),
                            title = R.string.delete_all,
                            message = R.string.delete_all_todos,
                            onPositive = {
                                homeViewModel.deleteAllTodos()
                                showToast(false, getString(R.string.delete_all_confirmation))
                            },
                            onNegative = {},
                            primaryColor = R.color.red_pink
                        )
                    }
                }
                setSortBy(menuItem.itemId)
                setFilterBy(menuItem.itemId)
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showEmptyScreen(show: Boolean) {
        if (show) {
            binding.emptyTodos.isVisible = true
            binding.rvTodos.isVisible = false
        } else {
            binding.emptyTodos.isVisible = false
            binding.rvTodos.isVisible = true
        }

    }

    fun setSelectedSubMenu(menu: Menu, parentMenuId: Int, menuSelected: Int) {
        val getSubMenu = menu.findItem(parentMenuId).subMenu!!
        getSubMenu.forEach { subMenuItem ->
            if (subMenuItem.itemId == menuSelected) {
                val selectedMenuNow = menu.findItem(menuSelected)
                selectedMenuNow.title =
                    Utils.createSpanString(
                        selectedMenuNow.title.toString(),
                        ContextCompat.getColor(requireContext(), R.color.joust_blue)
                    )
            } else {
                val notSelectedMenu = menu.findItem(subMenuItem.itemId)
                notSelectedMenu.title =
                    Utils.createSpanString(
                        notSelectedMenu.title.toString(),
                        ContextCompat.getColor(requireContext(), R.color.dark_gunmetal)
                    )
            }
        }
    }

    fun setSortBy(menuItemId: Int) {
        when (menuItemId) {
            R.id.date_asc -> homeViewModel.setSortBy(SortBy.DATE_ASC)
            R.id.date_desc -> homeViewModel.setSortBy(SortBy.DATE_DESC)
        }
    }

    fun setFilterBy(menuItemId: Int) {
        when (menuItemId) {
            R.id.low_priority -> homeViewModel.setFilterBy(FilterBy.LOW_PRIORITY)
            R.id.medium_priority -> homeViewModel.setFilterBy(FilterBy.MEDIUM_PRIORITY)
            R.id.high_priority -> homeViewModel.setFilterBy(FilterBy.HIGH_PRIORITY)
            R.id.all_priority -> homeViewModel.setFilterBy(FilterBy.ALL)
        }
    }

    private fun setSortMenuItemId(sortBy: SortBy): Int {
        return when (sortBy) {
            SortBy.DATE_ASC -> R.id.date_asc
            SortBy.DATE_DESC -> R.id.date_desc
        }
    }

    private fun setFilterMenuItemId(filter: FilterBy): Int {
        return when (filter) {
            FilterBy.HIGH_PRIORITY -> R.id.high_priority
            FilterBy.MEDIUM_PRIORITY -> R.id.medium_priority
            FilterBy.LOW_PRIORITY -> R.id.low_priority
            FilterBy.ALL -> R.id.all_priority
        }
    }

}