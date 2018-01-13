package com.rinon.shannoncode.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TableRow
import com.rinon.shannoncode.R
import kotlinx.android.synthetic.main.fragment_input_character.*

interface InputCharacterFragmentListener {
    fun inputCharacterListener(errorType: InputCharacterFragment.Companion.ErrorType,
                               pairList: Array<Pair<String, String>>)
}

class InputCharacterFragment : Fragment() {

    companion object {
        fun newInstance(num: Int): InputCharacterFragment {
            val instance = InputCharacterFragment()
            val bundle = Bundle()
            bundle.putSerializable(KEY_CHARACTER_NUM, num)
            instance.arguments = bundle

            return instance
        }

        enum class Order(val value: Int) {
            Character(0),
            Probability(1),

            Max(2)
        }

        enum class ErrorType {
            InputAll,
            CorrectProbability,
            Unique,

            None
        }

        private val KEY_CHARACTER_NUM = "character_num"
        private var listener: InputCharacterFragmentListener? = null
        private var pairList: Array<Pair<EditText, EditText>>? = null
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is InputCharacterFragmentListener) {
            listener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_input_character, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pairList = generateInputRows(arguments?.getInt(KEY_CHARACTER_NUM)?: throw IllegalArgumentException("argument is null"))

        calc_button.setOnClickListener {
            val strPairList = convertToString(pairList?: throw NullPointerException("pair list is null"))
            val errorType = when {
                !isInputAll(strPairList) -> ErrorType.InputAll
                !isCorrectProbability(strPairList) -> ErrorType.CorrectProbability
                !isCorrectCharacter(strPairList) -> ErrorType.Unique
                else -> ErrorType.None
            }
            listener?.inputCharacterListener(errorType, strPairList)
        }
    }

    private fun generateInputRows(num: Int):  Array<Pair<EditText, EditText>> {
        val ret = ArrayList<Pair<EditText, EditText>>()        // first:char second:probability

        for(counter in 0 until num) {
            val row = layoutInflater.inflate(R.layout.container_input_character, scroll_view_content, false) as TableRow
            val char = row.getChildAt(Order.Character.value) as EditText
            val probability = row.getChildAt(Order.Probability.value) as EditText
            scroll_view_content.addView(row)

            ret.add(Pair(char, probability))
        }
        return ret.toTypedArray()
    }

    private fun isInputAll(pairList: Array<Pair<String, String>>): Boolean {
        return pairList.none { it.first == "" || it.second == "" }
    }

    private fun isCorrectProbability(pairList: Array<Pair<String, String>>): Boolean {
        val sum = pairList.sumBy { it.second.toInt() }
        return (sum == 100)
    }

    private fun isCorrectCharacter(pairList: Array<Pair<String, String>>): Boolean {
        return pairList.size == pairList.distinctBy { it.first[0] }.size
    }

    private fun convertToString(pairList: Array<Pair<EditText, EditText>>): Array<Pair<String, String>> {
        val ret = mutableListOf<Pair<String, String>>()

        pairList.mapTo(ret) {
            Pair(it.first.text.toString(), it.second.text.toString())
        }

        return ret.toTypedArray()
    }
}