package com.github.malbanese93.opcode

import com.github.malbanese93.bit.highByte
import com.github.malbanese93.bit.highNibble
import com.github.malbanese93.bit.lowByte
import com.github.malbanese93.bit.lowNibble
import com.github.malbanese93.hardware.CPU
import com.github.malbanese93.utils.OPCODE_BYTES

fun skipIfVxEqNN(
    opcode : Int,
    cpu : CPU
) {
    val vx = cpu.regs.V[opcode.highByte.lowNibble]
    val nn = opcode.lowByte

    skipOnCondition(cpu, vx, Int::equals, nn)
}

fun skipIfVxNotEqNN(
    opcode : Int,
    cpu : CPU
) {
    val vx = cpu.regs.V[opcode.highByte.lowNibble]
    val nn = opcode.lowByte

    skipOnCondition(cpu, vx, Int::equals, nn)
}

fun skipIfVxEqVy(
    opcode : Int,
    cpu : CPU
) {
    val vx = cpu.regs.V[opcode.highByte.lowNibble]
    val vy = cpu.regs.V[opcode.lowByte.highNibble]

    skipOnCondition(cpu, vx, Int::equals, vy)
}

private fun skipOnCondition(
    cpu: CPU,
    operand1: Int,
    condition: (Int, Int) -> Boolean,
    operand2: Int
) {
    if (condition(operand1, operand2))
        cpu.regs.PC += OPCODE_BYTES
}

fun setVxToNN(
    opcode : Int,
    cpu : CPU
) = doArithmeticOperation(
        cpu = cpu,
        x = opcode.highByte.lowNibble,
        operation = { _, o2 -> o2},
        operand = opcode.lowByte
    )

fun addNNToVx(
    opcode : Int,
    cpu : CPU
) = doArithmeticOperation(
    cpu = cpu,
    x = opcode.highByte.lowNibble,
    operation = { o1, o2 -> o1 + o2},
    operand = opcode.lowByte
)

fun setVxToVy(
    opcode : Int,
    cpu : CPU
) = doArithmeticOperation(
    cpu = cpu,
    x = opcode.highByte.lowNibble,
    operation = { _, o2 -> o2},
    operand = cpu.regs.V[opcode.lowByte.highNibble]
)

fun setVxToVxOrVy(
    opcode : Int,
    cpu : CPU
) = doArithmeticOperation(
    cpu = cpu,
    x = opcode.highByte.lowNibble,
    operation = { o1, o2 -> o1.or(o2)},
    operand = cpu.regs.V[opcode.lowByte.highNibble]
)

fun setVxToVxAndVy(
    opcode : Int,
    cpu : CPU
) = doArithmeticOperation(
    cpu = cpu,
    x = opcode.highByte.lowNibble,
    operation = { o1, o2 -> o1.and(o2)},
    operand = cpu.regs.V[opcode.lowByte.highNibble]
)

fun setVxToVxXorVy(
    opcode : Int,
    cpu : CPU
) = doArithmeticOperation(
    cpu = cpu,
    x = opcode.highByte.lowNibble,
    operation = { o1, o2 -> o1.xor(o2)},
    operand = cpu.regs.V[opcode.lowByte.highNibble]
)

private fun doArithmeticOperation(
    cpu: CPU,
    x: Int,
    operation: (Int, Int) -> Int,
    operand: Int
) {
    cpu.regs.V[x] = operation(cpu.regs.V[x], operand)
}

fun notImplementedOperation(
    opcode : Int,
    cpu : CPU
): Nothing = TODO("Opcode to be implemented!")