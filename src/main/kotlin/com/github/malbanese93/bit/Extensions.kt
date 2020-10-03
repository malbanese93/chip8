package com.github.malbanese93.bit

val Int.highByte: Int
    get() = this.and(0xFF00).shr(8)

val Int.lowByte: Int
    get() = this.and(0xFF)

val Int.highNibble : Int
    get() = this.and(0xF0).shr(4)

val Int.lowNibble : Int
    get() = this.and(0x0F)

val Int.toHexString : String
    get() = "0x${this.toString(16).padStart(4,'0').toUpperCase()}"
