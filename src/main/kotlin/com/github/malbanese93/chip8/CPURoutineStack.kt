package com.github.malbanese93.chip8

import com.github.malbanese93.exceptions.OutOfRAMException
import com.github.malbanese93.exceptions.OutOfRoutineStackException

class CPURoutineStack {
    companion object {
        const val STACK_SIZE = 16
    }

    private val _stack : IntArray = IntArray(STACK_SIZE)

    init {
        reset()
    }

    fun reset() {
        for (idx in 0 until STACK_SIZE)
            _stack[idx] = 0
    }

    operator fun get(stackIndex: Int) : Int {
        if(stackIndex !in 0 until STACK_SIZE) throw OutOfRoutineStackException(
            stackIndex
        )

        val result = _stack[stackIndex]

        return if (result in 0 until Memory.SIZE_IN_BYTES) result else throw OutOfRAMException(
            result
        )
    }

    operator fun set(stackIndex: Int, value: Int) {
        if(stackIndex !in 0 until STACK_SIZE) throw OutOfRoutineStackException(
            stackIndex
        )
        if (value !in 0 until Memory.SIZE_IN_BYTES) throw OutOfRAMException(
            value
        )

        _stack[stackIndex] = value
    }
}