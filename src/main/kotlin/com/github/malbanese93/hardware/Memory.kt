package com.github.malbanese93.hardware

import com.github.malbanese93.OutOfRAMException
import com.github.malbanese93.ValueExceedingByteException


class Memory {
    companion object {
        const val SIZE_IN_BYTES = 4096
    }

    private val _buffer : IntArray = IntArray(SIZE_IN_BYTES) {
            idx -> 0x35 // fill some random data
    }

    fun readValue(address : Int) : Int {
        if(address !in 0..SIZE_IN_BYTES) throw OutOfRAMException(address)

        val result = _buffer[address]
        return if (result in 0..0xFF) result else throw ValueExceedingByteException(result)
    }
}