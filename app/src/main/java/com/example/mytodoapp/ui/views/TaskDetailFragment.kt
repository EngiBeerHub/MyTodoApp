package com.example.mytodoapp.ui.views

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mytodoapp.R
import com.example.mytodoapp.databinding.FragmentTaskDetailBinding
import com.example.mytodoapp.ui.viewmodels.TaskDetailViewModel
import com.google.android.material.snackbar.Snackbar

class TaskDetailFragment : Fragment(), DeleteTaskDialogFragment.DeleteTaskDialogListener {

    // Binding object instance corresponding to the fragment_task_list.xml
    private lateinit var binding: FragmentTaskDetailBinding

    // ViewModel corresponding to this Fragment
    private val viewModel: TaskDetailViewModel by viewModels()

    // arguments from Task List Fragment
    private val navigationArgs: TaskDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTaskDetailBinding.inflate(inflater, container, false)
        // bind viewModel and view
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /**
         * Subscribe TaskDetailViewModel's events
         * and handle changing View
         */
        viewModel.mode.observe(viewLifecycleOwner) { mode ->
            when (mode) {
                TaskDetailViewModel.Mode.CREATE -> {
                    // Hide the delete button when CREATE mode.
                    binding.buttonDelete.visibility = View.GONE
                }
                TaskDetailViewModel.Mode.ERROR_VALIDATION -> {
                    // Show Snack bar
                    Snackbar.make(
                        view, "Please input title.",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                TaskDetailViewModel.Mode.SUCCESS_CREATE -> {
                    hideKeyBoard()
                    // Show Snack bar
                    Snackbar.make(
                        view,
                        "The new task is created successfully.",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    // go back to the Task List Fragment
                    findNavController().popBackStack()
                }
                TaskDetailViewModel.Mode.SUCCESS_UPDATE -> {
                    hideKeyBoard()
                    // Show Snack bar
                    Snackbar.make(
                        view,
                        "The new task is created successfully.",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    // go back to the Task List Fragment
                    findNavController().popBackStack()
                }
                TaskDetailViewModel.Mode.CONFIRM_DELETE -> {
                    showDeleteDialog()
                }
                TaskDetailViewModel.Mode.SUCCESS_DELETE -> {
                    hideKeyBoard()
                    // Show Snack bar
                    Snackbar.make(
                        view,
                        "The new task is created successfully.",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    // go back to the Task List Fragment
                    findNavController().popBackStack()
                }
                else -> {
                }
            }
        }
        // Passed by Task List by tapping each Task
        val taskId = navigationArgs.taskId
        // Bind the Task to View whether it is new or existing.
        viewModel.bindTask(taskId)

        // TODO: Implement Notification sample
        createNotificationChannel()
        val builder = NotificationCompat.Builder(requireContext(), "CHANNEL_ID")
        val notification = builder.apply {
            setSmallIcon(R.drawable.ic_baseline_check_circle_outline_24)
            setContentTitle("Todo App")
            setContentText("This is a sample notification.")
            priority = NotificationCompat.PRIORITY_DEFAULT
        }.build()
        with(NotificationManagerCompat.from(requireContext())) {
            notify(1, notification)
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "CHANNEL_NAME"
            val descriptionText = "CHANNEL_DESCRIPTION"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("CHANNEL_ID", name, importance)
            channel.description = descriptionText
            // Register the channel with the system
            val notificationManager: NotificationManager =
                requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun hideKeyBoard() {
        // hide keyboard
        val manager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
    }

    /**
     * Methods handling DeleteTaskDialog
     */
    private fun showDeleteDialog() {
        val deleteDialog = DeleteTaskDialogFragment()
        // Add DeleteDialogFragment as a child fragment
        deleteDialog.show(childFragmentManager, "DeleteDialogFragment")
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        viewModel.deleteCurrentTask()
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        dialog.dismiss()
    }
}