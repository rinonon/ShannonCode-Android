package com.rinon.shannoncode.models

import android.util.Log

/**
 * Created by rinon on 2017/10/13.
 */
class ShannonCode(var contentList: ArrayList<Content>) {

    class Content(val char: Char,
                  val probability: Int,
                  var preProbability: Int = 0,
                  var binaryText: String = "",
                  var bitNum: Int = 0,
                  var codeword: String = "")

    fun calc() {
        // 1.確率順に並び替える
        contentList.sortByDescending { it -> it.probability }

        // 2.確率の合計値を計算していく
        contentList.get(0).preProbability = 0

        var counter = 0
        while(counter < contentList.size) {

            if(counter <= 0) {
                // 初回はとばす
                counter++
                continue
            }

            contentList.get(counter).preProbability =
                    contentList.get(counter - 1).probability + contentList.get(counter - 1).preProbability

            counter++
        }

        // 3.2進数にして符号を決める
        for(content in contentList) {

            //-log2piを求める (切り上げ)
            content.bitNum = (- Math.log(content.probability / 100.0) / Math.log(2.0)).toInt() + 1
            content.binaryText = generateBinaryText(content.preProbability / 100.0)

            // 最初の0.を抜いたbitNumの数
            content.codeword = content.binaryText.substring(2, content.bitNum + 2)

            Log.d("codeword", "char: " + content.char + '\n' +
                              "probability: " + content.probability.toString() + '\n' +
                              "preprobability: " + content.preProbability.toString() + '\n' +
                              "binaryText: " + content.binaryText + '\n' +
                              "bitNum: " + content.bitNum.toString() + '\n' +
                              "codeword: " + content.codeword + '\n')
        }
    }

    private fun generateBinaryText(probability: Double): String {

        // 10桁までしか作成しない
        val maxCount = 10
        var count = 1
        var binary = ""
        var num = probability

        while(count < maxCount) {
            binary += (num.toInt()).toString().get(0)

            if(num == 1.0) {
                break
            }
            else if(num > 1.0) {
                num -= 1.0
            }

            num *= 2.0
            count++
        }

        // 小数点をつける
        binary = binary.substring(0, 1) + "." + binary.substring(1)
        return binary
    }
}