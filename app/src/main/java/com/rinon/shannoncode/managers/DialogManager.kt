package com.rinon.shannoncode.managers

import android.content.DialogInterface
import com.rinon.shannoncode.fragments.ErrorDialogFragment

/**
 * Created by rinon on 2017/11/17.
 */

internal object DialogManager {

    fun createSimpleErrorDialog(message: String,
                                onOkClickListener: DialogInterface.OnClickListener? = null,
                                title: String = "Error",
                                positiveText: String = "OK"): ErrorDialogFragment {
        val dialog = ErrorDialogFragment.getInstance()
        dialog.title = title
        dialog.message = message
        dialog.positiveText = positiveText
        dialog.onOkClickListener = onOkClickListener
        return dialog
    }

    fun createSimpleDialog(title: String,
                           message: String,
                           onOkClickListener: DialogInterface.OnClickListener? = null,
                           positiveText: String = "OK"): ErrorDialogFragment {
        val dialog = ErrorDialogFragment.getInstance()
        dialog.title = title
        dialog.message = message
        dialog.positiveText = positiveText
        dialog.onOkClickListener = onOkClickListener
        return dialog
    }
}
