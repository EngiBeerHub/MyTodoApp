package com.example.mytodoapp.ui.views

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePickerDialogFragment(private val onCancelClicked: DialogInterface.OnClickListener) :
    DialogFragment() {

    // Instantiated in onAttach with class cast validation
    private lateinit var listener: DatePickerDialog.OnDateSetListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(
            requireContext(),
            // parentFragment must implement OnDataSetListener
            listener,
            year,
            month,
            day
        ).apply { setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", onCancelClicked) }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = parentFragment as DatePickerDialog.OnDateSetListener
        } catch (e: ClassCastException) {
            throw ClassCastException(("$context must implement OnDateSetListener"))
        }
    }
}