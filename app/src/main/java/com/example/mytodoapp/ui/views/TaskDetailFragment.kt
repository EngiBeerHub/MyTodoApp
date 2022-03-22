package com.example.mytodoapp.ui.views

import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mytodoapp.R
import com.example.mytodoapp.databinding.FragmentTaskDetailBinding
import com.example.mytodoapp.ui.viewmodels.TaskDetailViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class TaskDetailFragment : Fragment(), DeleteTaskDialogFragment.DeleteTaskDialogListener,
    DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener,
    DialogInterface.OnClickListener {

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
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    when (it.mode) {
                        Mode.ERROR_VALIDATION -> {
                            // Show Snack bar
                            Snackbar.make(
                                view, getString(R.string.snackbar_message_error_validation),
                                Snackbar.LENGTH_SHORT
                            ).show()
                            viewModel.onEventCompleted()
                        }
                        Mode.UPDATE_DEADLINE_DATE -> showDatePickerDialog()
                        Mode.UPDATE_DEADLINE_TIME -> showTimePickerDialog()
                        Mode.SUCCESS_CREATE -> {
                            hideKeyBoard()
                            // Show Snack bar
                            Snackbar.make(
                                view,
                                getString(R.string.snackbar_message_success_create),
                                Snackbar.LENGTH_SHORT
                            ).show()
                            // go back to the Task List Fragment
                            findNavController().popBackStack()
                            viewModel.onEventCompleted()
                        }
                        Mode.SUCCESS_UPDATE -> {
                            hideKeyBoard()
                            // Show Snack bar
                            Snackbar.make(
                                view,
                                getString(R.string.snackbar_message_success_update),
                                Snackbar.LENGTH_SHORT
                            ).show()
                            // go back to the Task List Fragment
                            findNavController().popBackStack()
                            viewModel.onEventCompleted()
                        }
                        Mode.CONFIRM_DELETE -> showDeleteDialog()
                        Mode.SUCCESS_DELETE -> {
                            hideKeyBoard()
                            // Show Snack bar
                            Snackbar.make(
                                view,
                                getString(R.string.snackbar_message_success_delete),
                                Snackbar.LENGTH_SHORT
                            ).show()
                            // go back to the Task List Fragment
                            findNavController().popBackStack()
                            viewModel.onEventCompleted()
                        }
                        else -> {}
                    }
                }
            }
        }
        // Passed by Task List by tapping each Task
        val taskId = navigationArgs.taskId
        // Bind the Task to View whether it is new or existing.
        viewModel.bindTask(taskId)

        // FIXME: This is just a sample of Notification
        val intent = Intent(requireContext(), MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(
                requireContext(),
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )

        val notification = NotificationCompat.Builder(
            requireContext(),
            getString(R.string.notification_channel_id)
        ).apply {
            setSmallIcon(R.drawable.ic_baseline_check_circle_outline_24)
            setContentTitle("Todo App")
            setContentText("This is a sample notification.")
            priority = NotificationCompat.PRIORITY_DEFAULT
            setContentIntent(pendingIntent)
            setAutoCancel(true)
        }.build()
        with(NotificationManagerCompat.from(requireContext())) {
            notify(1, notification)
        }
    }

    private fun hideKeyBoard() {
        // hide keyboard
        val manager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
    }

    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialogFragment(this)
        // Add DatePickerDialogFragment as a child fragment
        datePickerDialog.show(childFragmentManager, "DatePickerDialogFragment")
    }

    private fun showTimePickerDialog() {
        val timePickerDialog = TimePickerDialogFragment(this)
        // Add TimePickerDialogFragment as a child fragment
        timePickerDialog.show(childFragmentManager, "TimePickerDialogFragment")
    }

    /**
     * Methods handling DeleteTaskDialog
     */
    private fun showDeleteDialog() {
        val deleteDialog = DeleteTaskDialogFragment()
        // Add DeleteDialogFragment as a child fragment
        deleteDialog.show(childFragmentManager, "DeleteDialogFragment")
    }

    // Called when the positive button clicked in the delete confirm dialog
    override fun onDialogPositiveClick(dialog: DialogFragment) {
        viewModel.deleteCurrentTask()
        viewModel.onEventCompleted()
    }

    // Called when the negative button clicked in the delete confirm dialog
    override fun onDialogNegativeClick(dialog: DialogFragment) {
        dialog.dismiss()
        viewModel.onEventCompleted()
    }

    // Called when DatePickerDialog saved
    override fun onDateSet(datePicker: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        viewModel.onDeadLineDatePicked(year, month + 1, dayOfMonth)
    }

    // Called when TimePickerDialog saved
    override fun onTimeSet(timePicker: TimePicker?, hourOfDay: Int, minute: Int) {
        viewModel.onDeadLineTimePicked(hourOfDay, minute)
    }

    override fun onClick(dialog: DialogInterface?, buttonId: Int) {
        if (buttonId == DialogInterface.BUTTON_NEGATIVE) {
            viewModel.onSetDeadLineCanceled()
        }
    }
}