package com.example.mytodoapp.ui.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.mytodoapp.databinding.FragmentTaskDetailBinding
import com.example.mytodoapp.ui.viewmodels.TaskListViewModel

class TaskDetailFragment : Fragment() {

    // Binding object instance corresponding to the fragment_task_list.xml
    private lateinit var binding: FragmentTaskDetailBinding

    // Shared ViewModel which this fragment depends on
    private val viewModel: TaskListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTaskDetailBinding.inflate(inflater, container, false)
        // bind viewModel between view and
        binding.fragment = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    // validation check for adding new Task
    private fun isEntryValid(): Boolean {
        return binding.taskTitle.text.toString().isNotBlank()
    }

    fun addNewTask() {
        if (isEntryValid()) {
            viewModel.addNewTask(
                title = binding.taskTitle.text.toString(),
                content = binding.taskContent.text.toString()
            )
        } else {
            Toast.makeText(context, "Please input title.", Toast.LENGTH_SHORT).show()
        }
    }
}