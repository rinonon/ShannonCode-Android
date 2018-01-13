package com.rinon.shannoncode.model

import java.io.Serializable

object ShannonFano {

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
               override var codeword: String = ""): Serializable, com.rinon.shannoncode.model.AbstractCode()

    fun calc(codeList: MutableList<Code>): List<Code> {
        // 1.確率順に並び替える
        codeList.sortByDescending { it -> it.probability }

        // 2.確率の合計値を計算していく
        addBit(codeList, true)

        return codeList.toList()
    }

    private fun addBit(codeList: MutableList<Code>, up: Boolean) {
        val bit = if(up) '0' else '1'

        for(code in codeList) {
            code.codeword += bit
        }

        if(codeList.size >= 2) {
            val separator = getSeparatorIndex(codeList.toTypedArray())

            val upList = codeList.subList(0, separator)
            val downList = codeList.subList(separator, codeList.size)

            addBit(upList, true)
            addBit(downList, false)
        }
    }

    private fun getSeparatorIndex(codeList: Array<Code>): Int {
        return 1
    }
}