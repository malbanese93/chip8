package com.github.malbanese93.exceptions

import com.github.malbanese93.extensions.toHexString
import java.lang.IllegalArgumentException

class UnknownOpcodeException(opcode : Int) : IllegalArgumentException("OpcodeMnemonic <${opcode.toHexString}> is not a valid opcode for Chip8")