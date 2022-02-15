package com.example.mytodoapp.ui.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.mytodoapp.R
import com.example.mytodoapp.databinding.FragmentTaskListBinding
import com.example.mytodoapp.ui.TaskAdapter
import com.example.mytodoapp.ui.viewmodels.TaskListViewModel

class TaskListFragment : Fragment() {

    // Binding object instance corresponding to the fragment_task_list.xml
    private lateinit var binding: FragmentTaskListBinding

    // hold TaskListViewModel
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
        // instantiate task list recycler view
        val taskRecyclerView = binding.taskRecyclerView
        // load Task list from TaskListViewModel and observe to attach Tasks to adapter
        taskListViewModel.tasks.observe(viewLifecycleOwner) {
            // instantiate TaskAdapter
            val taskAdapter = TaskAdapter(it)
            taskRecyclerView.adapter = taskAdapter
        }
        return binding.root
    }

}