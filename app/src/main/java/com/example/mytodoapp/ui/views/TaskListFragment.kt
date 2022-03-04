package com.example.mytodoapp.ui.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.mytodoapp.databinding.FragmentTaskListBinding
import com.example.mytodoapp.ui.TaskAdapter
import com.example.mytodoapp.ui.viewmodels.TaskListViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Fragment to show All Task list
 * Home Destination on the navigation graph
 */
class TaskListFragment : Fragment() {

    // Binding object instance corresponding to the fragment_task_list.xml
    private lateinit var binding: FragmentTaskListBinding
    private val viewModel: TaskListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTaskListBinding.inflate(inflater, container, false)
        // Set PagingDataAdapter to the RecyclerView
        val adapter = TaskAdapter(viewModel) {
            // Tap Task to move to the Detail passing Task ID
            val action =
                TaskListFragmentDirections.actionTaskListFragmentToTaskDetailFragment(it.id)
            findNavController().navigate(action)
        }
        lifecycleScope.launch {
            viewModel.liveAllTasks.collectLatest {
                adapter.submitData(it)
            }
        }
        binding.recyclerView.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Tap FAB to move to Task Detail Fragment
        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(TaskListFragmentDirections.actionTaskListFragmentToTaskDetailFragment())
        }
    }

}