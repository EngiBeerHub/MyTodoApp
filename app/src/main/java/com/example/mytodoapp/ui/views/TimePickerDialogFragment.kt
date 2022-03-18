package com.example.mytodoapp.ui.views

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.*

class TimePickerDialogFragment : DialogFragment() {

    // Instantiated in onAttach with class cast validation
    private lateinit var listener: TimePickerDialog.OnTimeSetListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        return TimePickerDialog(
            requireContext(),
            // parentFragment must implement OnTimeSetListener
            listener,
            hourOfDay,
            minute,
            true
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = parentFragment as TimePickerDialog.OnTimeSetListener
        } catch (e: ClassCastException) {
            throw ClassCastException(("$context must implement OnTimeSetListener"))
        }
    }
}