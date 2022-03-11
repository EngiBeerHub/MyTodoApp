package com.example.mytodoapp.ui.views

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.mytodoapp.R

/**
 * A Dialog to confirm deleting a task to the user.
 */
class DeleteTaskDialogFragment : DialogFragment() {

    // Use this instance of the interface to deliver action events
    private lateinit var listener: DeleteTaskDialogListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setTitle(getString(R.string.dialog_delete_title))
                setMessage(getString(R.string.dialog_delete_message))
                setPositiveButton(getString(R.string.dialog_delete_label_positive)) { _, _ ->
                    listener.onDialogPositiveClick(this@DeleteTaskDialogFragment)
                }
                setNegativeButton(getString(R.string.dialog_delete_label_negative)) { _, _ ->
                    listener.onDialogNegativeClick(this@DeleteTaskDialogFragment)
                }
                // Create the AlertDialog object and return it
            }.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    // For instantiate the DeleteTaskDialogListener
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Verify that the host fragment implements the callback interface
        try {
            // Instantiate the DeleteTaskDialogListener so we can send events to the host
            listener = parentFragment as DeleteTaskDialogListener
        } catch (e: ClassCastException) {
            // The fragment doesn't implement the interface, throw exception
            throw ClassCastException(("$context must implement DeleteTaskDialogListener"))
        }
    }

    /**
     * The activity or fragment that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it.
     */
    interface DeleteTaskDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }
}