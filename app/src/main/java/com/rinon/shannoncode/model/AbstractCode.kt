package com.rinon.shannoncode.model

abstract class AbstractCode {
    abstract val symbol: Char
    abstract val probability: Int
    abstract val codeword: String
}