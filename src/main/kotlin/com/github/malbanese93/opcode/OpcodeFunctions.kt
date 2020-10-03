package com.github.malbanese93.opcode

import com.github.malbanese93.bit.*
import com.github.malbanese93.hardware.CPU
import com.github.malbanese93.utils.OPCODE_BYTES
import java.util.*

fun jumpToNNN(
    opcode : Int,
    cpu : CPU
) {
    cpu.regs.PC = opcode.highByte.lowNibble.combineWithByte(opcode.lowByte)
}

fun jumpToV0PlusNNN(
    opcode : Int,
    cpu : CPU
) {
    cpu.regs.PC = cpu.regs.V[0] + opcode.highByte.lowNibble.combineWithByte(opcode.lowByte)
}


fun skipIfVxEqNN(
    opcode : Int,
    cpu : CPU
) {
    val vx = cpu.regs.V[opcode.highByte.lowNibble]
    val nn = opcode.lowByte

    skipOnCondition(cpu, vx, Int::equals, nn)
    cpu.regs.PC += OPCODE_BYTES
}

fun skipIfVxNotEqNN(
    opcode : Int,
    cpu : CPU
) {
    val vx = cpu.regs.V[opcode.highByte.lowNibble]
    val nn = opcode.lowByte

    skipOnCondition(cpu, vx, {o1 : Int, o2 : Int -> o1 != o2}, nn)
    cpu.regs.PC += OPCODE_BYTES
}

fun skipIfVxEqVy(
    opcode : Int,
    cpu : CPU
) {
    val vx = cpu.regs.V[opcode.highByte.lowNibble]
    val vy = cpu.regs.V[opcode.lowByte.highNibble]

    skipOnCondition(cpu, vx, Int::equals, vy)
    cpu.regs.PC += OPCODE_BYTES
}

fun skipIfVxNotEqVy(
    opcode : Int,
    cpu : CPU
) {
    val vx = cpu.regs.V[opcode.highByte.lowNibble]
    val vy = cpu.regs.V[opcode.lowByte.highNibble]

    skipOnCondition(cpu, vx, {o1 : Int, o2 : Int -> o1 != o2}, vy)
    cpu.regs.PC += OPCODE_BYTES
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
) {
    val x = opcode.highByte.lowNibble
    val nn = opcode.lowByte
    cpu.regs.V[x] = nn

    cpu.regs.PC += OPCODE_BYTES
}

fun setVxToVxPlusNN(
    opcode : Int,
    cpu : CPU
)  {
    val x = opcode.highByte.lowNibble
    val nn = opcode.lowByte
    cpu.regs.V[x] += nn

    cpu.regs.PC += OPCODE_BYTES
}

fun setVxToVy(
    opcode : Int,
    cpu : CPU
) {
    val x = opcode.highByte.lowNibble
    val y = opcode.lowByte.highNibble
    cpu.regs.V[x] = cpu.regs.V[y]

    cpu.regs.PC += OPCODE_BYTES
}

fun setVxToVxOrVy(
    opcode : Int,
    cpu : CPU
) {
    val x = opcode.highByte.lowNibble
    val y = opcode.lowByte.highNibble
    cpu.regs.V[x] = cpu.regs.V[x].or(cpu.regs.V[y])

    cpu.regs.PC += OPCODE_BYTES
}

fun setVxToVxAndVy(
    opcode : Int,
    cpu : CPU
) {
    val x = opcode.highByte.lowNibble
    val y = opcode.lowByte.highNibble
    cpu.regs.V[x] = cpu.regs.V[x].and(cpu.regs.V[y])

    cpu.regs.PC += OPCODE_BYTES
}

fun setVxToVxXorVy(
    opcode : Int,
    cpu : CPU
) {
    val x = opcode.highByte.lowNibble
    val y = opcode.lowByte.highNibble
    cpu.regs.V[x] = cpu.regs.V[x].xor(cpu.regs.V[y])

    cpu.regs.PC += OPCODE_BYTES
}

fun setVxToVxPlusVy(
    opcode : Int,
    cpu : CPU
) {
    val x = opcode.highByte.lowNibble
    val oldVx = cpu.regs.V[x]

    val y = opcode.lowByte.highNibble
    val vy = cpu.regs.V[y]

    cpu.regs.V[x] = (oldVx + vy).and(0xFF)
    cpu.regs.V[0xF] = if(oldVx + vy > 0xFF) 1 else 0

    cpu.regs.PC += OPCODE_BYTES
}

fun setVxToVxMinusVy(
    opcode : Int,
    cpu : CPU
) {
    val x = opcode.highByte.lowNibble
    val oldVx = cpu.regs.V[x]

    val y = opcode.lowByte.highNibble
    val vy = cpu.regs.V[y]

    cpu.regs.V[x] = (oldVx - vy).and(0xFF)
    cpu.regs.V[0xF] = if(oldVx - vy < 0) 0 else 1

    cpu.regs.PC += OPCODE_BYTES
}

fun setVxToVxShr1(
    opcode : Int,
    cpu : CPU
) {
    val x = opcode.highByte.lowNibble
    val oldVx = cpu.regs.V[x]

    cpu.regs.V[x] = oldVx.shr(1)
    cpu.regs.V[0xF] = oldVx.lowBit

    cpu.regs.PC += OPCODE_BYTES
}

fun setVxToVyMinusVx(
    opcode : Int,
    cpu : CPU
) {
    val x = opcode.highByte.lowNibble
    val oldVx = cpu.regs.V[x]

    val y = opcode.lowByte.highNibble
    val vy = cpu.regs.V[y]

    cpu.regs.V[x] = (vy - oldVx).and(0xFF)
    cpu.regs.V[0xF] = if(-oldVx + vy < 0) 0 else 1

    cpu.regs.PC += OPCODE_BYTES
}

fun setVxToVxShl1(
    opcode : Int,
    cpu : CPU
) {
    val x = opcode.highByte.lowNibble
    val oldVx = cpu.regs.V[x]

    cpu.regs.V[x] = oldVx.shl(1)
    cpu.regs.V[0xF] = oldVx.highBitInByte

    cpu.regs.PC += OPCODE_BYTES
}

fun setVxToRandAndNN(
    opcode : Int,
    cpu : CPU
) {
    val x = opcode.highByte.lowNibble
    val nn = opcode.lowByte
    cpu.regs.V[x] = Random().nextInt(256).and(nn)

    cpu.regs.PC += OPCODE_BYTES
}

fun setIToNNN(
    opcode : Int,
    cpu : CPU
) {
    cpu.regs.I = opcode.highByte.lowNibble.combineWithByte(opcode.lowByte)

    cpu.regs.PC += OPCODE_BYTES
}

fun notImplementedOperation(
    opcode : Int,
    cpu : CPU
): Nothing = TODO("Opcode to be implemented!")