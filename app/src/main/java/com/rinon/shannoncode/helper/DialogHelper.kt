package com.rinon.shannoncode.helper

import android.content.DialogInterface
import com.rinon.shannoncode.fragment.ConfirmDialogFragment


internal object DialogHelper {

    fun createSimpleErrorDialog(message: String,
                                onOkClickListener: DialogInterface.OnClickListener? = null,
                                title: String = "Error",
                                positiveText: String = "OK"): ConfirmDialogFragment {
        val dialog = ConfirmDialogFragment.newInstance(title, message, positiveText)
        dialog.onOkClickListener = onOkClickListener
        return dialog
    }

    fun createSimpleDialog(title: String,
                           message: String,
                           onOkClickListener: DialogInterface.OnClickListener? = null,
                           positiveText: String = "OK"): ConfirmDialogFragment {
        val dialog = ConfirmDialogFragment.newInstance(title, message, positiveText)
        dialog.onOkClickListener = onOkClickListener
        return dialog
    }
}