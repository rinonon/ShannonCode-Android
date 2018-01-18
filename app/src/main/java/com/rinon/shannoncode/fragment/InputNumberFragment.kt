package com.rinon.shannoncode.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rinon.shannoncode.R
import kotlinx.android.synthetic.main.fragment_input_number.*

interface InputNumberFragmentListener {
    fun inputNumberListener(errorType: InputNumberFragment.Companion.ErrorType,
                            num: Int?)
}

class InputNumberFragment : Fragment() {

    companion object {
        fun newInstance(): InputNumberFragment {
            return InputNumberFragment()
        }

        enum class ErrorType {
            Input,
            Zero,
            Max,

            None
        }

        const val MAX_NUM = 10
        private var listener: InputNumberFragmentListener? = null
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is InputNumberFragmentListener) {
            listener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_input_number, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        text_max_num.text = resources.getString(R.string.max_number).format(MAX_NUM)

        next_button.setOnClickListener {
            val num = char_num.text.toString().toIntOrNull()
            val errorType = when {
                num == null -> ErrorType.Input
                num > MAX_NUM -> ErrorType.Max
                else -> ErrorType.None
            }
            listener?.inputNumberListener(errorType, num)
        }
    }
}