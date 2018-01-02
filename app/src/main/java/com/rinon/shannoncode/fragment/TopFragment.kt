package com.rinon.shannoncode.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rinon.shannoncode.R
import kotlinx.android.synthetic.main.fragment_top.*

interface TopFragmentListener {
    fun topListener(event: TopFragment.Companion.Event, quizFlag: Boolean)
}

class TopFragment : Fragment() {

    companion object {
        fun newInstance(): TopFragment {
            return TopFragment()
        }

        enum class Event {
            Start,

            None
        }

        private var listener: TopFragmentListener? = null
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



        button_start.setOnClickListener {
            listener?.topListener(Event.Start, quiz_switch.isChecked)
        }
    }
}
