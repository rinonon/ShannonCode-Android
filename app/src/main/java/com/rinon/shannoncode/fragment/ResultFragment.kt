package com.rinon.shannoncode.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rinon.shannoncode.R
import com.rinon.shannoncode.activity.TopActivity
import com.rinon.shannoncode.model.AbstractCode
import com.rinon.shannoncode.model.ShannonCode
import com.rinon.shannoncode.model.ShannonFano
import kotlinx.android.synthetic.main.fragment_result.*

interface ResultFragmentListener {
    fun resultListener(result: ResultFragment.Companion.Event, hintText: String? = null)
}

class ResultFragment : Fragment() {

    companion object {
        fun newInstance(type: TopActivity.Companion.Type,
                        codeList: Array<AbstractCode>,
                        quizType: QuizType): ResultFragment {

            val instance = ResultFragment()
            val bundle = Bundle()
            bundle.putSerializable(KEY_TYPE, type)
            bundle.putSerializable(KEY_CONTENT_LIST, codeList)
            bundle.putSerializable(KEY_QUIZ_TYPE, quizType)
            instance.arguments = bundle

            return instance
        }

        enum class Event {
            Complete,
            Wrong,
            Hint,
            Encode,
            Decode,

            None
        }

        enum class QuizType {
            Easy,
            Normal,
            Hard,

            None
        }

        private const val KEY_TYPE = "type"
        private const val KEY_QUIZ_TYPE = "quiz_type"
        private const val KEY_CONTENT_LIST = "content_list"

        private var listener: ResultFragmentListener? = null
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is ResultFragmentListener) {
            listener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val type = arguments?.getSerializable(KEY_TYPE) as TopActivity.Companion.Type
        val quizType = arguments?.getSerializable(KEY_QUIZ_TYPE) as QuizType
        val codeList = arguments?.getSerializable(KEY_CONTENT_LIST) as Array<AbstractCode>

        val fragment = when(type) {
            TopActivity.Companion.Type.Shannon -> ShannonResultFragment.newInstance(codeList as Array<ShannonCode.Code>, quizType)
            TopActivity.Companion.Type.ShannonFano -> ShannonFanoResultFragment.newInstance(codeList as Array<ShannonFano.Code>, quizType)

            else -> throw NullPointerException("Type is illegal")
        }

        // 子フラグメント設定
        childFragmentManager.beginTransaction()
                            .replace(R.id.container, fragment)
                            .commit()


        if(quizType == QuizType.None) {
            // エンコード/デコードボタンに切り替える
            button_switcher.showNext()
        }

        check_button.setOnClickListener {
            fragment.check()
        }

        hint_button.setOnClickListener {
            listener?.resultListener(Event.Hint, fragment.getHintText())
        }

        encode_button.setOnClickListener {
            listener?.resultListener(Event.Encode)
        }

        decode_button.setOnClickListener {
            listener?.resultListener(Event.Decode)
        }
    }

    fun eventListener(event: Event) {
        when(event) {
            Event.Complete -> {
                // エンコード/デコードボタンに切り替える
                button_switcher.showNext()
                listener?.resultListener(Event.Complete)
            }

            Event.Wrong -> {
                listener?.resultListener(Event.Wrong)
            }
        }
    }
}