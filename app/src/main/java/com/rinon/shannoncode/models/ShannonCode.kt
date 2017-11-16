package com.rinon.shannoncode.models

import android.util.Log
import java.io.Serializable

/**
 * Created by rinon on 2017/10/13.
 */
class ShannonCode (var contentList: ArrayList<Content>) : Serializable {
    // データ格納用の内部クラス
    class Content(val char: Char,
                  val probability: Int,
                  var preProbability: Int = 0,
                  var binaryText: String = "",
                  var length: Int = 0,
                  var codeword: String = "") : Serializable

    init {
        calc()
    }

    private fun calc() {
        // 1.確率順に並び替える
        contentList.sortByDescending { it -> it.probability }

        // 2.確率の合計値を計算していく
        contentList[0].preProbability = 0

        (0 until contentList.size)
                .asSequence()
                .filter { it > 0 }  // 初回はとばす
                .forEach {
                    contentList[it].preProbability = contentList[it - 1].probability + contentList[it - 1].preProbability
                }

        // 3.2進数にして符号を決める
        for(content in contentList) {
            //-log2piを求める (切り上げ)
            content.length = (- Math.log(content.probability / 100.0) / Math.log(2.0)).toInt() + 1
            content.binaryText = generateBinaryText(content.preProbability / 100.0)

            // 最初の0.を抜いたbitNumの数
            content.codeword = content.binaryText.substring(2, content.length + 2)

            Log.d("result", "char: " + content.char + '\n' +
                              "probability: " + content.probability.toString() + '\n' +
                              "preprobability: " + content.preProbability.toString() + '\n' +
                              "binaryText: " + content.binaryText + '\n' +
                              "length: " + content.length.toString() + '\n' +
                              "codeword: " + content.codeword + '\n')
        }
    }

    private fun generateBinaryText(probability: Double): String {
        // 10桁までしか作成しない
        val maxCount = 10
        var binary = ""
        var num = probability

        for(count in 0 until maxCount) {
            binary += (num.toInt()).toString()[0]

            if(num == 1.0) {
                for(i in count + 1 until maxCount) {
                    binary += "0"
                }
                break
            }
            else if(num > 1.0) {
                num -= 1.0
            }
            num *= 2.0
        }

        // 小数点をつける
        binary = binary.substring(0, 1) + "." + binary.substring(1)
        return binary
    }
}