package com.rinon.shannoncode.models

/**
 * Created by rinon on 2017/11/17.
 */

abstract  class Content {
    abstract val char: Char
    abstract val probability: Int
    abstract val codeword: String
}