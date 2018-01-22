package com.rinon.shannoncode.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rinon.shannoncode.R
import com.rinon.shannoncode.model.AbstractCode
import kotlinx.android.synthetic.main.fragment_encode.*

interface EncodeFragmentListener {
    fun encodeListener(event: EncodeFragment.Companion.Event)
}

class EncodeFragment : Fragment() {

    companion object {
        fun newInstance(codeList: Array<AbstractCode>): EncodeFragment {
            val instance = EncodeFragment()
            val bundle = Bundle()
            bundle.putSerializable(KEY_CONTENT_LIST, codeList)
            instance.arguments = bundle

            return instance
        }

        enum class Event {
            EncodeError,

            None
        }

        private const val KEY_CONTENT_LIST = "content_list"
        private var listener: EncodeFragmentListener? = null
        private var codeList: Array<AbstractCode>? = null
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is EncodeFragmentListener) {
            listener = context
        }
        else {
            throw ClassCastException("context does not implement listener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_encode, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        codeList = arguments?.getSerializable(KEY_CONTENT_LIST) as Array<AbstractCode>

        // 一覧の説明文作成
        var codewordListStr = ""
        codeList?.forEachIndexed {
            index, content -> codewordListStr += content.symbol + ":" + content.codeword +
                if(index + 1 % 5 == 0) "\n" else if(index + 1 == codeList?.size) "" else ", "
        }
        description_text.text = "($codewordListStr)"

        encode_button.setOnClickListener {
            val encodeText = encode(codeList ?: throw NullPointerException("codeList is null"),
                    source_text.text.toString())

            result_text.text = encodeText
        }
    }

    private fun encode(result: Array<AbstractCode>, sourceText: String): String {
        var ret = ""

        // 1文字ずつ変換
        for (char in sourceText) {
            try {
                val match: AbstractCode = result.find {
                    it.symbol == char
                } ?: throw Exception("not found")
                ret += match.codeword

            } catch (e: Exception) {
                // エラー処理
                listener?.encodeListener(Event.EncodeError)
                return ""
            }
        }
        return ret
    }

}
