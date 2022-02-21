package com.example.mytodoapp.ui.views

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mytodoapp.databinding.FragmentTaskDetailBinding
import com.example.mytodoapp.ui.viewmodels.TaskDetailViewModel

class TaskDetailFragment : Fragment() {

    // Binding object instance corresponding to the fragment_task_list.xml
    private lateinit var binding: FragmentTaskDetailBinding

    // ViewModel corresponding to this Fragment
    private val viewModel: TaskDetailViewModel by viewModels()

    // arguments from Task List Fragment
    private val navigationArgs: TaskDetailFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // When the input validation check is error, Show a Toast.
        viewModel.isEntryValid.observe(this) {
            if (it == false) {
                Toast.makeText(context, "Please input title.", Toast.LENGTH_SHORT).show()
            }
        }
        // When the Task Detail Fragment is done, go back to the Task List Fragment
        viewModel.isBackToList.observe(this) {
            if (it == true) {
                // hide keyboard
                val manager =
                    activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
                // show Toast
                Toast.makeText(context, "Task is registered successfully.", Toast.LENGTH_SHORT)
                    .show()
                // go back to the Task List Fragment
                findNavController().popBackStack()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTaskDetailBinding.inflate(inflater, container, false)
        // bind viewModel and view
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        // TODO: set initial data from Room when updating the existing Task
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val taskId = navigationArgs.taskId
        // If taskId is passed, bind the Task to View.
        viewModel.bindTask(taskId)
    }
}