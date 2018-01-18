package com.rinon.shannoncode.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.rinon.shannoncode.R
import kotlinx.android.synthetic.main.fragment_top.*

import com.rinon.shannoncode.activity.TopActivity.Companion.Type as Type
import com.rinon.shannoncode.fragment.ResultFragment.Companion.QuizType as QuizType

interface TopFragmentListener {
    fun topListener(event: TopFragment.Companion.Event, quizType: QuizType)
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
            Info,

            None
        }

        private const val KEY_TYPE = "type"
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

        quiz_switch.setOnCheckedChangeListener { _, checked ->
            if(checked) {
                radio_group.visibility = View.VISIBLE
                radio_group.startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_down))
            } else {
                radio_group.visibility = View.INVISIBLE
                radio_group.startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_up))
            }
        }

        button_start.setOnClickListener {
            var quizType = when(radio_group.checkedRadioButtonId) {
                R.id.radio_easy -> QuizType.Easy
                R.id.radio_normal -> QuizType.Normal
                R.id.radio_hard -> QuizType.Hard
                else -> QuizType.None
            }

            if(!quiz_switch.isChecked) {
                // チェックされていなければNone
                quizType = QuizType.None
            }
            listener?.topListener(Event.Start, quizType)
        }

        button_info.setOnClickListener {
            listener?.topListener(Event.Info, QuizType.None)
        }
    }
}
