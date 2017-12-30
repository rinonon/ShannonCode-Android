package com.rinon.shannoncode.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import com.rinon.shannoncode.R
import com.rinon.shannoncode.fragment.InputCharacterFragmentListener
import com.rinon.shannoncode.fragment.InputNumberFragment
import com.rinon.shannoncode.fragment.InputNumberFragmentListener
import com.rinon.shannoncode.fragment.ResultShannonFragmentListener
import com.rinon.shannoncode.fragment.InputCharacterFragment
import com.rinon.shannoncode.fragment.ResultShannonFragment
import com.rinon.shannoncode.managers.DialogManager
import com.rinon.shannoncode.model.ShannonCode
import kotlinx.android.synthetic.main.activity_shannon_coding.*

class ShannonCodingActivity : AppCompatActivity(), InputNumberFragmentListener
                                                 , InputCharacterFragmentListener
                                                 , ResultShannonFragmentListener {

    companion object {
        var result: ArrayList<ShannonCode.Content>? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shannon_coding)

        // ツールバーの設定
        toolbar.title = resources.getString(R.string.shannon_coding)
        setSupportActionBar(toolbar)

        if (savedInstanceState == null) {
            val inputNumberFragment = InputNumberFragment.newInstance()

            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, inputNumberFragment, inputNumberFragment.tag)
                    .commit()
        }
    }

    override fun inputNumberListener(errorType: InputNumberFragment.Companion.ErrorType,
                                     num: Int?) {
        when (errorType){
            InputNumberFragment.Companion.ErrorType.Input -> {
                // 数字が入力されていない
                val dialog = DialogManager.createSimpleErrorDialog(resources.getString(R.string.error_input_check))
                dialog.show(supportFragmentManager, null)
            }
            InputNumberFragment.Companion.ErrorType.Max -> {
                // 数が最大値よりも大きい
                val dialog = DialogManager.createSimpleErrorDialog(resources.getString(R.string.error_num_check) + InputNumberFragment.MAX_NUM)
                dialog.show(supportFragmentManager, null)
            }
            else -> {
                // fragment変更
                val inputCharacterFragment = InputCharacterFragment.newInstance(num?: throw NullPointerException("num is null"))

                supportFragmentManager.beginTransaction()
                                      .setCustomAnimations(R.anim.slide_in_right,
                                                           R.anim.slide_out_left,
                                                           R.anim.slide_in_left,
                                                           R.anim.slide_out_right)
                                      .replace(R.id.container, inputCharacterFragment, inputCharacterFragment.tag)
                                      .addToBackStack(inputCharacterFragment.tag)
                                      .commit()
            }
        }
    }

    override fun inputCharacterListener(errorType: InputCharacterFragment.Companion.ErrorType,
                                        pairList: ArrayList<Pair<EditText, EditText>>?) {
        when (errorType) {
            InputCharacterFragment.Companion.ErrorType.InputAll -> {
                val dialog = DialogManager.createSimpleErrorDialog(resources.getString(R.string.error_input_check))
                dialog.show(supportFragmentManager, null)
            }

            InputCharacterFragment.Companion.ErrorType.CorrectProbability -> {
                val dialog = DialogManager.createSimpleErrorDialog(resources.getString(R.string.error_probability_check))
                dialog.show(supportFragmentManager, null)
            }

            InputCharacterFragment.Companion.ErrorType.Unique -> {
                val dialog = DialogManager.createSimpleErrorDialog(resources.getString(R.string.error_unique_check))
                dialog.show(supportFragmentManager, null)
            }

            InputCharacterFragment.Companion.ErrorType.None -> {
                if(pairList != null) {
                    result = convertToShannonCode(pairList)
                    val resultFragment = ResultShannonFragment.newInstance(result?: throw NullPointerException("result is null"),
                                                                            false)
                    supportFragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_right,
                                    R.anim.slide_out_left,
                                    R.anim.slide_in_left,
                                    R.anim.slide_out_right)
                            .replace(R.id.container, resultFragment)
                            .addToBackStack(resultFragment.tag)
                            .commit()
                } else {
                    throw IllegalArgumentException("pairList is null")
                }
            }
        }
    }

    override fun resultShannonListener(status: ResultShannonFragment.Companion.Event, hintText: String?) {
       when(status) {
           ResultShannonFragment.Companion.Event.Wrong -> {
               val dialog = DialogManager.createSimpleDialog("Wrong", resources.getString(R.string.wrong_answer))
               dialog.show(supportFragmentManager, null)
           }

           ResultShannonFragment.Companion.Event.Complete -> {
               // ダイアログを出す
               val dialog = DialogManager.createSimpleDialog(resources.getString(R.string.congratulation),
                                                             resources.getString(R.string.all_correct))
               dialog.show(supportFragmentManager, null)
           }

           ResultShannonFragment.Companion.Event.Hint -> {
               val dialog = DialogManager.createSimpleDialog("Hint", hintText?: throw NullPointerException("hint text is null"))
               dialog.show(supportFragmentManager, null)
           }

           ResultShannonFragment.Companion.Event.Encode -> {
               val intent = Intent(this, EncodeDecodeActivity::class.java)
               intent.putExtra(EncodeDecodeActivity.CONTENT, result?: throw NullPointerException("result is null"))
               intent.putExtra(EncodeDecodeActivity.STATUS, EncodeDecodeActivity.Companion.Status.Encode)
               startActivity(intent)
           }

           ResultShannonFragment.Companion.Event.Decode -> {
               val intent = Intent(this, EncodeDecodeActivity::class.java)
               intent.putExtra(EncodeDecodeActivity.CONTENT, result?: throw NullPointerException("result is null"))
               intent.putExtra(EncodeDecodeActivity.STATUS, EncodeDecodeActivity.Companion.Status.Decode)
               startActivity(intent)
           }
       }
    }

    private fun convertToShannonCode(pairList: ArrayList<Pair<EditText, EditText>>): ArrayList<ShannonCode.Content> {
        val contentList = ArrayList<ShannonCode.Content>()

        // 変換作業
        pairList.mapTo(contentList) {
            ShannonCode.Content(it.first.text.toString()[0],
                    it.second.text.toString().toInt())
        }
        return ShannonCode.calc(contentList)
    }
}