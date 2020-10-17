package com.github.malbanese93.opcode

import com.github.malbanese93.chip8.*
import com.github.malbanese93.chip8.Memory.Companion.FONT_SIZE_IN_BYTES
import com.github.malbanese93.utils.START_PC
import com.github.malbanese93.exceptions.ValueExceedingByteException
import com.github.malbanese93.exceptions.ValueExceedingNibbleException
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
        val opcode = 0x1BEA

        jumpToNNN(opcode, cpu)
        assertEquals(0x0BEA, cpu.regs.PC)
    }

    @Test
    fun jumpToV0PlusNNN() { // BNNN
        val opcode = 0xBBEA
        cpu.regs.V[0] = 2

        jumpToV0PlusNNN(opcode, cpu)
        assertEquals(0x0BEC, cpu.regs.PC)
    }

    @Test
    fun skipIfVxEqNN() {    // 3XNN
        val opcode = 0x3ABA
        cpu.regs.V[0xA] = 0xBA

        skipIfVxEqNN(opcode, cpu)
        assertEquals(startPC + 4, cpu.regs.PC) // skip one instruction
    }

    @Test
    fun skipIfVxEqNNComparisonFails() {    // 3XNN
        val opcode = 0x3ABA
        cpu.regs.V[0xA] = 0xAB

        skipIfVxEqNN(opcode, cpu)
        assertEquals(startPC + 2, cpu.regs.PC) // DO NOT skip one instruction
    }

    @Test
    fun skipIfVxNotEqNN() { // 4XNN
        val opcode = 0x4CCD
        cpu.regs.V[0xC] = 0xBA

        skipIfVxNotEqNN(opcode, cpu)
        assertEquals(startPC + 4, cpu.regs.PC) // skip one instruction
    }

    @Test
    fun skipIfVxNotEqNNComparisonFails() { // 4XNN
        val opcode = 0x4CCD
        cpu.regs.V[0xC] = 0xCD

        skipIfVxNotEqNN(opcode, cpu)
        assertEquals(startPC + 2, cpu.regs.PC) // DO NOT skip one instruction
    }

    @Test
    fun skipIfVxEqVy() {            // 5XY0
        val opcode = 0x5AB0
        cpu.regs.V[0xA] = 0x1
        cpu.regs.V[0xB] = 0x1

        skipIfVxEqVy(opcode, cpu)
        assertEquals(startPC + 4, cpu.regs.PC) // skip one instruction
    }

    @Test
    fun skipIfVxEqVyComparisonFails() {            // 5XY0
        val opcode = 0x5AB0
        cpu.regs.V[0xA] = 0x2
        cpu.regs.V[0xB] = 0x1

        skipIfVxEqVy(opcode, cpu)
        assertEquals(startPC + 2, cpu.regs.PC) // skip one instruction
    }

    @Test
    fun skipIfVxNotEqVy() {                         // 9XY0
        val opcode = 0x9AB0
        cpu.regs.V[0xA] = 0x1
        cpu.regs.V[0xB] = 0x2

        skipIfVxNotEqVy(opcode, cpu)
        assertEquals(startPC + 4, cpu.regs.PC) // skip one instruction
    }

    @Test
    fun skipIfVxNotEqVyComparisonFails() {          // 9XY0
        val opcode = 0x9AB0
        cpu.regs.V[0xA] = 0x2
        cpu.regs.V[0xB] = 0x2

        skipIfVxNotEqVy(opcode, cpu)
        assertEquals(startPC + 2, cpu.regs.PC) // skip one instruction
    }

    @Test
    fun setVxToNN() {               // 6XNN
        val opcode = 0x6ABC
        setVxToNN(opcode, cpu)
        assertEquals(0xBC, cpu.regs.V[0xA])
        assertEquals(startPC + 2, cpu.regs.PC)
    }

    @Test
    fun setVxToVxPlusNN() {         // 7XNN
        val opcode = 0x7AEE
        cpu.regs.V[0xA] = 0x20
        setVxToVxPlusNN(opcode, cpu)
        assertEquals(0x0E, cpu.regs.V[0xA])
        assertEquals(0x00, cpu.regs.V[0xF]) // unchanged even with carry!
        assertEquals(startPC + 2, cpu.regs.PC)
    }

    @Test
    fun setVxToVy() {               // 8XY0
        val opcode = 0x8AB0
        cpu.regs.V[0xB] = 0xCD
        setVxToVy(opcode, cpu)
        assertEquals(0xCD, cpu.regs.V[0xA])
        assertEquals(startPC + 2, cpu.regs.PC)
    }

    @Test
    fun setVxToVxOrVy() {           // 8XY1
        val opcode = 0x8AB1
        cpu.regs.V[0xA] = 0b11001010
        cpu.regs.V[0xB] = 0b01011001
        setVxToVxOrVy(opcode, cpu)
        assertEquals(0b11011011, cpu.regs.V[0xA])
        assertEquals(startPC + 2, cpu.regs.PC)
    }

    @Test
    fun setVxToVxAndVy() {          // 8XY2
        val opcode = 0x8AB2
        cpu.regs.V[0xA] = 0b11001010
        cpu.regs.V[0xB] = 0b01011001
        setVxToVxAndVy(opcode, cpu)
        assertEquals(0b01001000, cpu.regs.V[0xA])
        assertEquals(startPC + 2, cpu.regs.PC)
    }

    @Test
    fun setVxToVxXorVy() {          // 8XY3
        val opcode = 0x8AB3
        cpu.regs.V[0xA] = 0b11001010
        cpu.regs.V[0xB] = 0b01011001
        setVxToVxXorVy(opcode, cpu)
        assertEquals(0b10010011, cpu.regs.V[0xA])
        assertEquals(startPC + 2, cpu.regs.PC)
    }

    @Test
    fun setVxToVxPlusVy() {         // 8XY4
        val opcode = 0x8AB4
        cpu.regs.V[0xA] = 0x23
        cpu.regs.V[0xB] = 0x01
        setVxToVxPlusVy(opcode, cpu)
        assertEquals(0x24, cpu.regs.V[0xA])
        assertEquals(0x00, cpu.regs.V[0xF])
        assertEquals(startPC + 2, cpu.regs.PC)
    }

    @Test
    fun setVxToVxPlusVyWithCarry() {         // 8XY4
        val opcode = 0x8AB4
        cpu.regs.V[0xA] = 0xFE
        cpu.regs.V[0xB] = 0x03
        setVxToVxPlusVy(opcode, cpu)
        assertEquals(0x01, cpu.regs.V[0xA])
        assertEquals(0x01, cpu.regs.V[0xF])
        assertEquals(startPC + 2, cpu.regs.PC)
    }

    @Test
    fun setVxToVxMinusVy() {                // 8XY5
        val opcode = 0x8AB5
        cpu.regs.V[0xA] = 0x23
        cpu.regs.V[0xB] = 0x01
        setVxToVxMinusVy(opcode, cpu)
        assertEquals(0x22, cpu.regs.V[0xA])
        assertEquals(0x00, cpu.regs.V[0xF])
        assertEquals(startPC + 2, cpu.regs.PC)
    }

    @Test
    fun setVxToVxMinusVyWithBorrow() {                // 8XY5
        val opcode = 0x8AB5
        cpu.regs.V[0xA] = 0x23
        cpu.regs.V[0xB] = 0x25
        setVxToVxMinusVy(opcode, cpu)
        assertEquals(0xFE, cpu.regs.V[0xA])
        assertEquals(0x01, cpu.regs.V[0xF])
        assertEquals(startPC + 2, cpu.regs.PC)
    }

    @Test
    fun setVxToVxShr1() {           // 8XY6
        val opcode = 0x8AB6
        cpu.regs.V[0xA] = 0b1001_1001
        setVxToVxShr1(opcode, cpu)
        assertEquals(0b0100_1100, cpu.regs.V[0xA])
        assertEquals(0x01, cpu.regs.V[0xF])
        assertEquals(startPC + 2, cpu.regs.PC)
    }

    @Test
    fun setVxToVxShr1WithLsb0() {           // 8XY6
        val opcode = 0x8AB6
        cpu.regs.V[0xA] = 0b1001_1000
        setVxToVxShr1(opcode, cpu)
        assertEquals(0b0100_1100, cpu.regs.V[0xA])
        assertEquals(0x00, cpu.regs.V[0xF])
        assertEquals(startPC + 2, cpu.regs.PC)
    }

    @Test
    fun setVxToVyMinusVx() {                // 8XY7
        val opcode = 0x8AB7
        cpu.regs.V[0xA] = 0x23
        cpu.regs.V[0xB] = 0x25
        setVxToVyMinusVx(opcode, cpu)
        assertEquals(0x02, cpu.regs.V[0xA])
        assertEquals(0x00, cpu.regs.V[0xF])
        assertEquals(startPC + 2, cpu.regs.PC)
    }

    @Test
    fun setVxToVyMinusVxWithBorrow() {             // 8XY7
        val opcode = 0x8AB7
        cpu.regs.V[0xA] = 0x03
        cpu.regs.V[0xB] = 0x02
        setVxToVyMinusVx(opcode, cpu)
        assertEquals(0xFF, cpu.regs.V[0xA])
        assertEquals(0x01, cpu.regs.V[0xF])
        assertEquals(startPC + 2, cpu.regs.PC)
    }

    @Test
    fun setVxToVxShl1() {           // 8XYE
        val opcode = 0x8ABE
        cpu.regs.V[0xA] = 0b1001_1001
        setVxToVxShl1(opcode, cpu)
        assertEquals(0b0011_0010, cpu.regs.V[0xA])
        assertEquals(0x01, cpu.regs.V[0xF])
        assertEquals(startPC + 2, cpu.regs.PC)
    }

    @Test
    fun setVxToVxShl1WithMsb0() {           // 8XYE
        val opcode = 0x8ABE
        cpu.regs.V[0xA] = 0b0001_1001
        setVxToVxShl1(opcode, cpu)
        assertEquals(0b0011_0010, cpu.regs.V[0xA])
        assertEquals(0x00, cpu.regs.V[0xF])
        assertEquals(startPC + 2, cpu.regs.PC)
    }

    @Test
    fun setVxToDelayTimer() {           // FX07
        val opcode = 0xFA07
        cpu.regs.DT = 0x23
        setVxToDelayTimer(opcode, cpu)
        assertEquals(0x23, cpu.regs.V[0xA])
        assertEquals(startPC + 2, cpu.regs.PC)
    }

    @Test
    fun setDelayTimerToVx() {           // FX15
        val opcode = 0xFA15
        cpu.regs.V[0xA] = 0x23
        setDelayTimerToVx(opcode, cpu)
        assertEquals(0x23, cpu.regs.DT)
        assertEquals(startPC + 2, cpu.regs.PC)
    }

    @Test
    fun setSoundTimerToVx() {           // FX18
        val opcode = 0xFA18
        cpu.regs.V[0xA] = 0x23
        setSoundTimerToVx(opcode, cpu)
        assertEquals(0x23, cpu.regs.ST)
        assertEquals(startPC + 2, cpu.regs.PC)
    }

    @Test
    fun setIToNNN() {               // ANNN
        val opcode = 0xACBA
        setIToNNN(opcode, cpu)
        assertEquals(0xCBA, cpu.regs.I)
        assertEquals(startPC + 2, cpu.regs.PC)
    }

    @Test
    fun setIToIPlusVx() {           // FX1E
        val opcode = 0xFA1E
        cpu.regs.V[0xA] = 0x0A
        cpu.regs.I = 0xFFFE
        setIToIPlusVx(opcode, cpu)
        assertEquals(0x8, cpu.regs.I)
        assertEquals(startPC + 2, cpu.regs.PC)
        assertEquals(0x00, cpu.regs.V[0xF]) // unchanged even with carry!
    }

    @Test
    fun setIToSpriteLocation() {        // FX29
        val opcode = 0xFA29
        cpu.regs.V[0xA] = 0xB
        setIToSpriteLocation(opcode, cpu)
        assertEquals(FONT_SIZE_IN_BYTES * 0xB, cpu.regs.I)
        assertEquals(startPC + 2, cpu.regs.PC)
    }

    @Test
    fun setIToSpriteLocationFailsWithOutOfRangeValue() {        // FX29
        val opcode = 0xFA29
        cpu.regs.V[0xA] = 0x10
        Assertions.assertThrows(ValueExceedingNibbleException::class.java) { setIToSpriteLocation(opcode, cpu) }
    }

    @Test
    fun bcd() {
    }

    @Test
    fun dumpVxRegisters() {
    }

    @Test
    fun loadVxRegisters() {
    }

    @Test
    fun callSubroutine() {
    }

    @Test
    fun returnFromSubroutine() {
    }

    @Test
    fun draw() {
    }

    @Test
    fun clearDisplay() {
    }
}