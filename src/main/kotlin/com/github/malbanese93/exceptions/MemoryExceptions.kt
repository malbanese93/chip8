package com.github.malbanese93.exceptions

import com.github.malbanese93.chip8.Memory
import com.github.malbanese93.extensions.toHexString
import java.lang.IllegalArgumentException

class OutOfRAMException(address : Int) : IllegalArgumentException("The address <${address.toHexString}> is outside the allowed range: 0x0 - ${Memory.SIZE_IN_BYTES.toHexString}")
class OutOfRoutineStackException(index : Int) : IllegalArgumentException("Routine stack overflow: requested index $index")