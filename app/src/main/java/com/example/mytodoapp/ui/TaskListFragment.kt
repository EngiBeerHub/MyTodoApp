package com.example.mytodoapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mytodoapp.R
import com.example.mytodoapp.data.repository.TaskDataSource

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

        // set action to navigate to task detail fragment
//        val button = view.findViewById<Button>(R.id.button)
//        button.setOnClickListener {
//            findNavController().navigate(R.id.action_taskListFragment_to_taskDetailFragment)
//        }

        // instantiate task list recycler view
        val taskRecyclerView = view.findViewById<RecyclerView>(R.id.task_recycler_view)
        // instantiate TaskAdapter
        val taskAdapter = TaskAdapter(TaskDataSource().loadTasks())
        // set task adapter to task list recycler view
        taskRecyclerView.adapter = taskAdapter
        // set border line to each task views
        val linearLayoutManager = LinearLayoutManager(view.context)
        taskRecyclerView.addItemDecoration(
            DividerItemDecoration(
                view.context,
                linearLayoutManager.orientation
            )
        )
        return view
    }

}