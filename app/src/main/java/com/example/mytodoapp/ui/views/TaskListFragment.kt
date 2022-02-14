package com.example.mytodoapp.ui.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.mytodoapp.R
import com.example.mytodoapp.ui.TaskAdapter
import com.example.mytodoapp.ui.viewmodels.TaskListViewModel

class TaskListFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_task_list, container, false)
        // instantiate TaskListViewModel
        val taskListViewModel = ViewModelProvider(this).get(TaskListViewModel::class.java)
        // instantiate task list recycler view
        val taskRecyclerView = view.findViewById<RecyclerView>(R.id.task_recycler_view)
        // load Task list from TaskListViewModel and observe to attach Tasks to adapter
        taskListViewModel.getTasks().observe(viewLifecycleOwner) {
            // instantiate TaskAdapter
            val taskAdapter = TaskAdapter(it)
            taskRecyclerView.adapter = taskAdapter
        }
        return view
    }

}