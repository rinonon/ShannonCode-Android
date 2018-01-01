package com.rinon.shannoncode.fragment

import android.content.DialogInterface
import android.os.Bundle

import android.app.Dialog
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog

class ConfirmDialogFragment : DialogFragment() {

    companion object {
        fun newInstance(title: String,
                        message: String,
                        positiveText: String): ConfirmDialogFragment {
            val instance = ConfirmDialogFragment()
            val bundle = Bundle()
            bundle.putString(KEY_TITLE, title)
            bundle.putString(KEY_MESSAGE, message)
            bundle.putString(KEY_POSITIVE_TEXT, positiveText)
            instance.arguments = bundle

            return instance
        }

        private val KEY_TITLE = "title"
        private val KEY_MESSAGE = "message"
        private val KEY_POSITIVE_TEXT = "positive_text"
        // private val KEY_ON_CLICK_LISTENER = "on_click_listener"
    }

    var onOkClickListener : DialogInterface.OnClickListener? = null // TODO: どうにかする

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the Builder class for convenient dialog construction
        val builder = AlertDialog.Builder(context?: throw NullPointerException("Context is null"))
        builder.setTitle(arguments?.getString(KEY_TITLE))
                .setMessage(arguments?.getString(KEY_MESSAGE))
                .setPositiveButton(arguments?.getString(KEY_POSITIVE_TEXT), onOkClickListener)
        // Create the AlertDialog object and return it
        return builder.create()
    }
}
