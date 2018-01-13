package com.rinon.shannoncode.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rinon.shannoncode.R
import com.rinon.shannoncode.activity.TopActivity.Companion.Type as Type
import kotlinx.android.synthetic.main.fragment_top.*

interface TopFragmentListener {
    fun topListener(event: TopFragment.Companion.Event, quizFlag: Boolean)
}

class TopFragment : Fragment() {

    companion object {
        fun newInstance(type: Type): TopFragment {
            val instance = TopFragment()
            val bundle = Bundle()
            bundle.putSerializable(KEY_TYPE, type)
            instance.arguments = bundle

            return instance
        }

        enum class Event {
            Start,

            None
        }

        private val KEY_TYPE = "type"
        private var listener: TopFragmentListener? = null
        private var type = Type.None
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is TopFragmentListener) {
            listener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_top, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        type = arguments?.getSerializable(KEY_TYPE) as Type

        title.text = when(type) {
            Type.Shannon -> resources.getString(R.string.shannon_coding)
            Type.ShannonFano -> resources.getString(R.string.shannon_fano_coding)

            else -> ""
        }

        button_start.setOnClickListener {
            listener?.topListener(Event.Start, quiz_switch.isChecked)
        }
    }
}
