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
import kotlinx.android.synthetic.main.fragment_decode.*

interface DecodeFragmentListener {
    fun decodeListener(event: DecodeFragment.Companion.Event)
}

class DecodeFragment : Fragment() {

    companion object {
        fun newInstance(codeList: Array<AbstractCode>): DecodeFragment {
            val instance = DecodeFragment()
            val bundle = Bundle()
            bundle.putSerializable(KEY_CONTENT_LIST, codeList)
            instance.arguments = bundle

            return instance
        }

        enum class Event {
            DecodeError,

            None
        }

        private const val KEY_CONTENT_LIST = "content_list"
        private var listener: DecodeFragmentListener? = null
        private var codeList: Array<AbstractCode>? = null
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is DecodeFragmentListener) {
            listener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_decode, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        codeList = arguments?.getSerializable(KEY_CONTENT_LIST) as Array<AbstractCode>

        // 一覧の説明文作成
        var codewordListStr = ""
        codeList?.forEachIndexed {
            index, content -> codewordListStr += content.char + ":" + content.codeword +
                if(index + 1 % 5 == 0) "\n" else if(index + 1 == codeList?.size) "" else ", "
        }
        description_text.text = "($codewordListStr)"

        decode_button.setOnClickListener {
            val decodeText = decode(codeList ?: throw NullPointerException("codeList is null"),
                                    codeword.text.toString())

            result_text.text = decodeText
        }
    }

    private fun decode(result: Array<AbstractCode>, sourceText: String): String {
        var ret = ""
        var currentIdx = 0

        while (currentIdx < sourceText.length) {
            try {
                val match: AbstractCode = result.find {
                    it.codeword == sourceText.substring(currentIdx, it.codeword.length + currentIdx)
                } ?: throw Exception("not found")

                ret += match.char
                currentIdx += match.codeword.length
            } catch (e: Exception) {
                // エラー処理
                listener?.decodeListener(Event.DecodeError)
                return ""
            }
        }
        return ret
    }
}
