package com.rinon.shannoncode.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rinon.shannoncode.R
import com.rinon.shannoncode.activity.TopActivity
import com.rinon.shannoncode.model.AbstractContent
import com.rinon.shannoncode.model.ShannonCode
import kotlinx.android.synthetic.main.fragment_result.*

interface ResultFragmentListener {
    fun resultListener(result: ResultFragment.Companion.Event, hintText: String? = null)
}

class ResultFragment : Fragment() {

    companion object {
        fun newInstance(type: TopActivity.Companion.Type,
                        contentList: ArrayList<AbstractContent>,
                        quizFlag: Boolean): ResultFragment {

            val instance = ResultFragment()
            val bundle = Bundle()
            bundle.putSerializable(KEY_TYPE, type)
            bundle.putSerializable(KEY_CONTENT_LIST, contentList)
            bundle.putBoolean(KEY_QUIZ_FLAG, quizFlag)
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

        private val KEY_TYPE = "type"
        private val KEY_QUIZ_FLAG = "quiz_flag"
        private val KEY_CONTENT_LIST = "content_list"

        //private var contentList: ArrayList<AbstractContent>? = null
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

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val type = arguments.getSerializable(KEY_TYPE) as TopActivity.Companion.Type
        val quizFlag = arguments.getBoolean(KEY_QUIZ_FLAG)
        val contentList = arguments.getSerializable(KEY_CONTENT_LIST) as ArrayList<AbstractContent>

        val fragment = when(type) {
            TopActivity.Companion.Type.Shannon -> ResultShannonFragment.newInstance(contentList as ArrayList<ShannonCode.Content>, quizFlag)

            else -> throw NullPointerException("Type is illegal")
        }

        // 子フラグメント設定
        childFragmentManager.beginTransaction()
                            .replace(R.id.container, fragment)
                            .commit()


        if(!quizFlag) {
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

    fun eventListener(event: ResultShannonFragment.Companion.Event) {
        when(event) {
            ResultShannonFragment.Companion.Event.Complete -> {
                // エンコード/デコードボタンに切り替える
                button_switcher.showNext()
                listener?.resultListener(Event.Complete)
            }

            ResultShannonFragment.Companion.Event.Wrong -> {
                listener?.resultListener(Event.Wrong)
            }
        }
    }
}