package com.github.malbanese93.bit

val Int.highByte: Int
    get() = this.and(0xFF00).shr(8)

val Int.lowByte: Int
    get() = this.and(0xFF)

val Int.highNibble : Int
    get() = this.and(0xF0).shr(4)

val Int.lowNibble : Int
    get() = this.and(0x0F)

val Int.lowBit : Int
    get() = this.and(0x1)

val Int.highBitInByte : Int
    get() = this.and(0x80)

fun Int.combineWithByte(low : Int) : Int = this.shl(8).or(low)

val Int.toHexString : String
    get() = "0x${this.toString(16).padStart(4,'0').toUpperCase()}"

val Int.toByteHexString : String
    get() = this.toString(16).padStart(2,'0').toUpperCase()