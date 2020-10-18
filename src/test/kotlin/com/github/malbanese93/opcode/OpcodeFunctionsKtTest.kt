package com.github.malbanese93.opcode

import com.github.malbanese93.chip8.*
import com.github.malbanese93.chip8.Memory.Companion.FONT_SIZE_IN_BYTES
import com.github.malbanese93.exceptions.OutOfRoutineStackException
import com.github.malbanese93.exceptions.ValueExceedingNibbleException
import com.github.malbanese93.extensions.highByte
import com.github.malbanese93.extensions.lowByte
import com.github.malbanese93.utils.START_PC
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class OpcodeFunctionsKtTest {

    private lateinit var memory : Memory
    private lateinit var soundGenerator : SoundGenerator
    private lateinit var stack : CPURoutineStack
    private lateinit var videoBuffer : VideoBuffer

    private lateinit var cpu : CPU

    private var startPC : Int = START_PC

    @BeforeEach
    fun setUp() {
        val memory = Memory()
        val soundGenerator = SoundGenerator()
        val stack = CPURoutineStack()
        val videoBuffer = VideoBuffer()

        cpu = CPU(
            regs = CPURegisters(),
            soundGenerator = soundGenerator,
            memory = memory,
            stack = stack,
            videoBuffer = videoBuffer
        )

        startPC = cpu.regs.PC
    }

    @AfterEach
    fun tearDown() {
        println("== AFTER TEST ==")
        cpu.regs.logValues()
        cpu.memory.dumpMemory()
    }

    @Test
    fun jumpToNNN() { // 1NNN
        setNextInstruction(0x1BEA)

        cpu.update()
        assertEquals(0x0BEA, cpu.regs.PC)
    }

    @Test
    fun jumpToV0PlusNNN() { // BNNN
        setNextInstruction(0xBBEA)
        cpu.regs.V[0] = 2

        cpu.update()
        assertEquals(0x0BEC, cpu.regs.PC)
    }

    @Test
    fun skipIfVxEqNN() {    // 3XNN
        setNextInstruction(0x3ABA)
        cpu.regs.V[0xA] = 0xBA

        cpu.update()
        assertEquals(startPC + 4, cpu.regs.PC) // skip one instruction
    }

    @Test
    fun skipIfVxEqNNComparisonFails() {    // 3XNN
        setNextInstruction(0x3ABA)
        cpu.regs.V[0xA] = 0xAB

        cpu.update()
        assertEquals(startPC + 2, cpu.regs.PC) // DO NOT skip one instruction
    }

    @Test
    fun skipIfVxNotEqNN() { // 4XNN
        setNextInstruction(0x4CCD)
        cpu.regs.V[0xC] = 0xBA

        cpu.update()
        assertEquals(startPC + 4, cpu.regs.PC) // skip one instruction
    }

    @Test
    fun skipIfVxNotEqNNComparisonFails() { // 4XNN
        setNextInstruction(0x4CCD)
        cpu.regs.V[0xC] = 0xCD

        cpu.update()
        assertEquals(startPC + 2, cpu.regs.PC) // DO NOT skip one instruction
    }

    @Test
    fun skipIfVxEqVy() {            // 5XY0
        setNextInstruction(0x5AB0)
        cpu.regs.V[0xA] = 0x1
        cpu.regs.V[0xB] = 0x1

        cpu.update()
        assertEquals(startPC + 4, cpu.regs.PC) // skip one instruction
    }

    @Test
    fun skipIfVxEqVyComparisonFails() {            // 5XY0
        setNextInstruction(0x5AB0)
        cpu.regs.V[0xA] = 0x2
        cpu.regs.V[0xB] = 0x1

        cpu.update()
        assertEquals(startPC + 2, cpu.regs.PC) // skip one instruction
    }

    @Test
    fun skipIfVxNotEqVy() {                         // 9XY0
        setNextInstruction(0x9AB0)
        cpu.regs.V[0xA] = 0x1
        cpu.regs.V[0xB] = 0x2

        cpu.update()
        assertEquals(startPC + 4, cpu.regs.PC) // skip one instruction
    }

    @Test
    fun skipIfVxNotEqVyComparisonFails() {          // 9XY0
        setNextInstruction(0x9AB0)
        cpu.regs.V[0xA] = 0x2
        cpu.regs.V[0xB] = 0x2

        cpu.update()
        assertEquals(startPC + 2, cpu.regs.PC) // skip one instruction
    }

    @Test
    fun setVxToNN() {               // 6XNN
        setNextInstruction(0x6ABC)

        cpu.update()
        assertEquals(0xBC, cpu.regs.V[0xA])
        assertEquals(startPC + 2, cpu.regs.PC)
    }

    @Test
    fun setVxToVxPlusNN() {         // 7XNN
        setNextInstruction(0x7AEE)
        cpu.regs.V[0xA] = 0x20

        cpu.update()
        assertEquals(0x0E, cpu.regs.V[0xA])
        assertEquals(0x00, cpu.regs.V[0xF]) // unchanged even with carry!
        assertEquals(startPC + 2, cpu.regs.PC)
    }

    @Test
    fun setVxToVy() {               // 8XY0
        setNextInstruction(0x8AB0)
        cpu.regs.V[0xB] = 0xCD

        cpu.update()
        assertEquals(0xCD, cpu.regs.V[0xA])
        assertEquals(startPC + 2, cpu.regs.PC)
    }

    @Test
    fun setVxToVxOrVy() {           // 8XY1
        setNextInstruction(0x8AB1)
        cpu.regs.V[0xA] = 0b11001010
        cpu.regs.V[0xB] = 0b01011001

        cpu.update()
        assertEquals(0b11011011, cpu.regs.V[0xA])
        assertEquals(startPC + 2, cpu.regs.PC)
    }

    @Test
    fun setVxToVxAndVy() {          // 8XY2
        setNextInstruction(0x8AB2)
        cpu.regs.V[0xA] = 0b11001010
        cpu.regs.V[0xB] = 0b01011001

        cpu.update()
        assertEquals(0b01001000, cpu.regs.V[0xA])
        assertEquals(startPC + 2, cpu.regs.PC)
    }

    @Test
    fun setVxToVxXorVy() {          // 8XY3
        setNextInstruction(0x8AB3)
        cpu.regs.V[0xA] = 0b11001010
        cpu.regs.V[0xB] = 0b01011001

        cpu.update()
        assertEquals(0b10010011, cpu.regs.V[0xA])
        assertEquals(startPC + 2, cpu.regs.PC)
    }

    @Test
    fun setVxToVxPlusVy() {         // 8XY4
        setNextInstruction(0x8AB4)
        cpu.regs.V[0xA] = 0x23
        cpu.regs.V[0xB] = 0x01

        cpu.update()
        assertEquals(0x24, cpu.regs.V[0xA])
        assertEquals(0x00, cpu.regs.V[0xF])
        assertEquals(startPC + 2, cpu.regs.PC)
    }

    @Test
    fun setVxToVxPlusVyWithCarry() {         // 8XY4
        setNextInstruction(0x8AB4)
        cpu.regs.V[0xA] = 0xFE
        cpu.regs.V[0xB] = 0x03

        cpu.update()
        assertEquals(0x01, cpu.regs.V[0xA])
        assertEquals(0x01, cpu.regs.V[0xF])
        assertEquals(startPC + 2, cpu.regs.PC)
    }

    @Test
    fun setVxToVxMinusVy() {                // 8XY5
        setNextInstruction(0x8AB5)
        cpu.regs.V[0xA] = 0x23
        cpu.regs.V[0xB] = 0x01

        cpu.update()
        assertEquals(0x22, cpu.regs.V[0xA])
        assertEquals(0x00, cpu.regs.V[0xF])
        assertEquals(startPC + 2, cpu.regs.PC)
    }

    @Test
    fun setVxToVxMinusVyWithBorrow() {                // 8XY5
        setNextInstruction(0x8AB5)
        cpu.regs.V[0xA] = 0x23
        cpu.regs.V[0xB] = 0x25

        cpu.update()
        assertEquals(0xFE, cpu.regs.V[0xA])
        assertEquals(0x01, cpu.regs.V[0xF])
        assertEquals(startPC + 2, cpu.regs.PC)
    }

    @Test
    fun setVxToVxShr1() {           // 8XY6
        setNextInstruction(0x8AB6)
        cpu.regs.V[0xA] = 0b1001_1001

        cpu.update()
        assertEquals(0b0100_1100, cpu.regs.V[0xA])
        assertEquals(0x01, cpu.regs.V[0xF])
        assertEquals(startPC + 2, cpu.regs.PC)
    }

    @Test
    fun setVxToVxShr1WithLsb0() {           // 8XY6
        setNextInstruction(0x8AB6)
        cpu.regs.V[0xA] = 0b1001_1000

        cpu.update()
        assertEquals(0b0100_1100, cpu.regs.V[0xA])
        assertEquals(0x00, cpu.regs.V[0xF])
        assertEquals(startPC + 2, cpu.regs.PC)
    }

    @Test
    fun setVxToVyMinusVx() {                // 8XY7
        setNextInstruction(0x8AB7)
        cpu.regs.V[0xA] = 0x23
        cpu.regs.V[0xB] = 0x25

        cpu.update()
        assertEquals(0x02, cpu.regs.V[0xA])
        assertEquals(0x00, cpu.regs.V[0xF])
        assertEquals(startPC + 2, cpu.regs.PC)
    }

    @Test
    fun setVxToVyMinusVxWithBorrow() {             // 8XY7
        setNextInstruction(0x8AB7)
        cpu.regs.V[0xA] = 0x03
        cpu.regs.V[0xB] = 0x02

        cpu.update()
        assertEquals(0xFF, cpu.regs.V[0xA])
        assertEquals(0x01, cpu.regs.V[0xF])
        assertEquals(startPC + 2, cpu.regs.PC)
    }

    @Test
    fun setVxToVxShl1() {           // 8XYE
        setNextInstruction(0x8ABE)
        cpu.regs.V[0xA] = 0b1001_1001

        cpu.update()
        assertEquals(0b0011_0010, cpu.regs.V[0xA])
        assertEquals(0x01, cpu.regs.V[0xF])
        assertEquals(startPC + 2, cpu.regs.PC)
    }

    @Test
    fun setVxToVxShl1WithMsb0() {           // 8XYE
        setNextInstruction(0x8ABE)
        cpu.regs.V[0xA] = 0b0001_1001

        cpu.update()
        assertEquals(0b0011_0010, cpu.regs.V[0xA])
        assertEquals(0x00, cpu.regs.V[0xF])
        assertEquals(startPC + 2, cpu.regs.PC)
    }

    @Test
    fun setVxToDelayTimer() {           // FX07
        setNextInstruction(0xFA07)
        cpu.regs.DT = 0x23

        cpu.update()

        assertEquals(0x23, cpu.regs.V[0xA])
        assertEquals(startPC + 2, cpu.regs.PC)
    }

    @Test
    fun setDelayTimerToVx() {           // FX15
        setNextInstruction(0xFA15)
        cpu.regs.V[0xA] = 0x23

        cpu.update()
        assertEquals(0x23, cpu.regs.DT)
        assertEquals(startPC + 2, cpu.regs.PC)
    }

    @Test
    fun setSoundTimerToVx() {           // FX18
        setNextInstruction(0xFA18)
        cpu.regs.V[0xA] = 0x23

        cpu.update()
        assertEquals(0x23, cpu.regs.ST)
        assertEquals(startPC + 2, cpu.regs.PC)
    }

    @Test
    fun setIToNNN() {               // ANNN
        setNextInstruction(0xACBA)

        cpu.update()
        assertEquals(0xCBA, cpu.regs.I)
        assertEquals(startPC + 2, cpu.regs.PC)
    }

    @Test
    fun setIToIPlusVx() {           // FX1E
        setNextInstruction(0xFA1E)
        cpu.regs.V[0xA] = 0x0A
        cpu.regs.I = 0xFFFE

        cpu.update()
        assertEquals(0x8, cpu.regs.I)
        assertEquals(0x00, cpu.regs.V[0xF]) // unchanged even with carry!
        assertEquals(startPC + 2, cpu.regs.PC)
    }

    @Test
    fun setIToSpriteLocation() {        // FX29
        setNextInstruction(0xFA29)
        cpu.regs.V[0xA] = 0xB

        cpu.update()
        assertEquals(FONT_SIZE_IN_BYTES * 0xB, cpu.regs.I)
        assertEquals(startPC + 2, cpu.regs.PC)
    }

    @Test
    fun setIToSpriteLocationFailsWithOutOfRangeValue() {        // FX29
        setNextInstruction(0xFA29)
        cpu.regs.V[0xA] = 0x10

        Assertions.assertThrows(ValueExceedingNibbleException::class.java) { cpu.update() }
    }

    @Test
    fun bcd() {             // FX33
        setNextInstruction(0xFA33)
        cpu.regs.V[0xA] = 234

        cpu.update()
        assertEquals(2, cpu.memory[cpu.regs.I])
        assertEquals(3, cpu.memory[cpu.regs.I+1])
        assertEquals(4, cpu.memory[cpu.regs.I+2])
        assertEquals(startPC + 2, cpu.regs.PC)
    }

    @Test
    fun dumpVxRegisters() {         // FX55
        setNextInstruction(0xF155)

        cpu.regs.I = 0x0420
        cpu.regs.V[0x0] = 0x1F
        cpu.regs.V[0x1] = 0xAB

        cpu.update()
        assertEquals(0x1F, cpu.memory[cpu.regs.I])
        assertEquals(0xAB, cpu.memory[cpu.regs.I+1])

        for (idx in 0x2..0xF)
            assertEquals(0x00, cpu.memory[cpu.regs.I+idx])

        assertEquals(0x0420, cpu.regs.I)
        assertEquals(startPC + 2, cpu.regs.PC)
    }

    @Test
    fun loadVxRegisters() {         // FX65
        setNextInstruction(0xF165)

        cpu.regs.I = 0x0420
        cpu.memory[cpu.regs.I] = 0x1F
        cpu.memory[cpu.regs.I+1] = 0xAB

        cpu.update()
        assertEquals(0x1F, cpu.regs.V[0x00])
        assertEquals(0xAB, cpu.regs.V[0x01])

        for (idx in 0x2..0xF)
            assertEquals(0x00, cpu.regs.V[idx])

        assertEquals(0x0420, cpu.regs.I)
        assertEquals(startPC + 2, cpu.regs.PC)
    }

    @Test
    fun callSubroutine() {          // 2NNN
        setNextInstruction(0x2ABC)

        cpu.update()
        assertEquals(0x1, cpu.regs.SP)
        assertEquals(startPC, cpu.stack[0])
        assertEquals(0xABC, cpu.regs.PC)
    }

    @Test
    fun callSubroutineThrowsStackOverflow() {       // 2NNN
        setNextInstruction(0x2ABC)
        cpu.regs.SP = CPURoutineStack.STACK_SIZE

        Assertions.assertThrows(OutOfRoutineStackException::class.java) { cpu.update() }
    }

    @Test
    fun returnFromSubroutine() {    // 00EE
        setNextInstruction(0x00EE)
        cpu.regs.SP = 0x3
        cpu.stack[2] = 0x12

        cpu.update()
        assertEquals(0x2, cpu.regs.SP)
        assertEquals(0x12, cpu.regs.PC)
    }

    @Test
    fun returnFromSubroutineThrowsEmptyStack() {    // 00EE
        setNextInstruction(0x00EE)

        Assertions.assertThrows(OutOfRoutineStackException::class.java) { cpu.update() }
    }

    @Test
    fun draw() {
    }

    @Test
    fun clearDisplay() {                // 00E0
        setNextInstruction(0x00E0)
        cpu.videoBuffer[0, 0] = true

        cpu.update()

        for (x in 0 until VideoBuffer.ROW_PIXELS)
            for (y in 0 until VideoBuffer.COL_PIXELS)
                assertEquals(false, cpu.videoBuffer[x,y])

        assertEquals(startPC + 2, cpu.regs.PC)
    }

    private fun setNextInstruction(opcode: Int) {
        cpu.memory[cpu.regs.PC] = opcode.highByte
        cpu.memory[cpu.regs.PC+1] = opcode.lowByte
    }
}