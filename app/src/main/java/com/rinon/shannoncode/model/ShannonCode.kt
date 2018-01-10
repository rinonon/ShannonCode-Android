package com.rinon.shannoncode.model

import android.util.Log
import java.io.Serializable

object ShannonCode {

    enum class Order(val value: Int) {
        Num(0),
        Character(1),
        Probability(2),
        PreProbability(3),
        Binary(4),
        Length(5),
        Codeword(6),

        Max(7)
    }

    // データ格納用の内部クラス
    class Code(override val char: Char,
               override val probability: Int,
               override var codeword: String = "",
               var preProbability: Int = 0,
               var binaryText: String = "",
               var length: Int = 0): Serializable, com.rinon.shannoncode.model.AbstractCode()

    fun calc(codeList: MutableList<Code>): Array<Code> {
        // 1.確率順に並び替える
        codeList.sortByDescending { it -> it.probability }

        // 2.確率の合計値を計算していく
        codeList[0].preProbability = 0

        (0 until codeList.size)
                .asSequence()
                .filter { it > 0 }  // 初回はとばす
                .forEach {
                    codeList[it].preProbability = codeList[it - 1].probability + codeList[it - 1].preProbability
                }

        // 3.2進数にして符号を決める
        for(code in codeList) {
            //-log2piを求める (切り上げ)
            code.length = (- Math.log(code.probability / 100.0) / Math.log(2.0)).toInt() + 1
            code.binaryText = generateBinaryText(code.preProbability / 100.0)

            // 最初の0.を抜いたbitNumの数
            code.codeword = code.binaryText.substring(2, code.length + 2)

            Log.d("result", "char: " + code.char + '\n' +
                              "probability: " + code.probability.toString() + '\n' +
                              "preProbability: " + code.preProbability.toString() + '\n' +
                              "binaryText: " + code.binaryText + '\n' +
                              "length: " + code.length.toString() + '\n' +
                              "codeword: " + code.codeword + '\n')
        }
        return codeList.toTypedArray()
    }

    private fun generateBinaryText(probability: Double): String {
        // 8桁までしか作成しない
        val maxCount = 8
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