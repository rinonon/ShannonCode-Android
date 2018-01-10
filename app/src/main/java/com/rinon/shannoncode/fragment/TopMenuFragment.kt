package com.rinon.shannoncode.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rinon.shannoncode.R
import kotlinx.android.synthetic.main.fragment_top_menu.*

interface TopMenuFragmentListener {
    fun topMenuListener(event: TopMenuFragment.Companion.Event, view: View)
}

class TopMenuFragment : Fragment() {

    companion object {
        fun newInstance(): TopMenuFragment {
            return TopMenuFragment()
        }

        enum class Event {
            Shannon,
            ShannonFano,

            None
        }

        private var listener: TopMenuFragmentListener? = null
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is TopMenuFragmentListener) {
            listener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_top_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_shannon_coding.setOnClickListener {
            listener?.topMenuListener(Event.Shannon, button_shannon_coding)
        }

        button_shannon_fano.setOnClickListener {
            listener?.topMenuListener(Event.ShannonFano, button_shannon_fano)
        }
    }
}
