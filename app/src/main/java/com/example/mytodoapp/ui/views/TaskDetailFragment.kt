package com.example.mytodoapp.ui.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.mytodoapp.databinding.FragmentTaskDetailBinding
import com.example.mytodoapp.ui.viewmodels.TaskDetailViewModel

class TaskDetailFragment : Fragment() {

    // Binding object instance corresponding to the fragment_task_list.xml
    private lateinit var binding: FragmentTaskDetailBinding

    private val viewModel: TaskDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // When the input validation check is error, Show a Toast.
        viewModel.isEntryValid.observe(this) {
            if (it == false) {
                Toast.makeText(context, "Please input title.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTaskDetailBinding.inflate(inflater, container, false)
        // bind viewModel between view and
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        // set initial data from Room
        return binding.root
    }

}