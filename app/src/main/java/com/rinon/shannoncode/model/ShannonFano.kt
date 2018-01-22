package com.rinon.shannoncode.model

import java.io.Serializable

object ShannonFano {

    enum class Order(val value: Int) {
        Character(0),
        Probability(1),
        Codeword(2),

        Max(3)
    }

    // データ格納用の内部クラス
    class Code(override val symbol: Char,
               override val probability: Int,
               override var codeword: String = ""): Serializable, com.rinon.shannoncode.model.AbstractCode()

    fun calc(codeList: MutableList<Code>): List<Code> {
        // 1.確率順に並び替える
        codeList.sortByDescending { it -> it.probability }

        // 2.確率の合計値を計算していく
        if(codeList.size >= 2) {
            val separator = getSeparatorIndex(codeList.toTypedArray())

            val upList = codeList.subList(0, separator)
            val downList = codeList.subList(separator, codeList.size)

            addBit(upList, true)
            addBit(downList, false)
        } else {
            codeList[0].codeword += '0'
        }

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
        var index = 1
        val border = (codeList.sumBy { it.probability }) / 2

        while(Math.abs(border - codeList.slice(IntRange(0, index - 1)).sumBy { it.probability }) >
                Math.abs(border - codeList.slice(IntRange(0, index)).sumBy { it.probability })) {
            index++
        }
        return index
    }
}