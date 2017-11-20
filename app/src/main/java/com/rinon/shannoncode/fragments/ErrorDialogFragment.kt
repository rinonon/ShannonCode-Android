package com.rinon.shannoncode.fragments

import android.content.DialogInterface
import android.os.Bundle

import android.app.Dialog
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog

class ErrorDialogFragment : DialogFragment() {

    companion object {
        fun getInstance(): ErrorDialogFragment {
            return ErrorDialogFragment()
        }
    }

    var title = "title"
    var message = "message"
    var positiveText = "OK"
    var onOkClickListener : DialogInterface.OnClickListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the Builder class for convenient dialog construction
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveText, onOkClickListener)
        // Create the AlertDialog object and return it
        return builder.create()
    }
}