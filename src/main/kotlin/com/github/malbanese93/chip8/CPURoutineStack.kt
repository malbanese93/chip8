package com.github.malbanese93.chip8

import com.github.malbanese93.utils.OutOfRAMException
import com.github.malbanese93.utils.StackOverflowException

class CPURoutineStack {
    companion object {
        const val STACK_SIZE = 16
    }

    private val _stack : IntArray = IntArray(STACK_SIZE)

    operator fun get(stackIndex: Int) : Int {
        if(stackIndex !in 0..STACK_SIZE) throw StackOverflowException(stackIndex)

        val result = _stack[stackIndex]
        return if (result !in 0..Memory.SIZE_IN_BYTES) result else throw OutOfRAMException(
            result
        )
    }

    operator fun set(stackIndex: Int, value: Int) {
        if(stackIndex !in 0..STACK_SIZE) throw StackOverflowException(stackIndex)
        if (value !in 0..Memory.SIZE_IN_BYTES) throw OutOfRAMException(value)

        _stack[stackIndex] = value
    }
}