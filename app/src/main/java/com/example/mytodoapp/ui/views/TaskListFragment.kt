package com.example.mytodoapp.ui.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.mytodoapp.databinding.FragmentTaskListBinding
import com.example.mytodoapp.ui.TaskPagedListAdapter
import com.example.mytodoapp.ui.viewmodels.TaskListViewModel

/**
 * Fragment to show All Task list
 * Home Destination on the navigation graph
 */
class TaskListFragment : Fragment() {

    // Binding object instance corresponding to the fragment_task_list.xml
    private lateinit var binding: FragmentTaskListBinding
    // ViewModel which this fragment depends on
    private val taskListViewModel: TaskListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTaskListBinding.inflate(inflater, container, false)

        // attach Task list LiveData to RecyclerView not using Paging
//        taskListViewModel.tasks.observe(this.viewLifecycleOwner) {
//            val taskAdapter = TaskAdapter(it)
//            taskRecyclerView.adapter = taskAdapter
//        }

        // set PagedListAdapter to the RecyclerView
        val adapter = TaskPagedListAdapter()
        taskListViewModel.liveAllTasks.observe(this.viewLifecycleOwner) {
            adapter.submitList(it)
        }
        binding.taskRecyclerView.adapter = adapter

        return binding.root
    }

}