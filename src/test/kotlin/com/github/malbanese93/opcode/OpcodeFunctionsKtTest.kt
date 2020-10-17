package com.github.malbanese93.opcode

import com.github.malbanese93.chip8.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class OpcodeFunctionsKtTest {

    private lateinit var memory : Memory
    private lateinit var soundGenerator : SoundGenerator
    private lateinit var stack : CPURoutineStack
    private lateinit var videoBuffer : VideoBuffer

    private lateinit var cpu : CPU

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
        val startPC = cpu.regs.PC
        val opcode = 0x3ABA
        cpu.regs.V[0xA] = 0xBA

        skipIfVxEqNN(opcode, cpu)
        assertEquals(startPC + 4, cpu.regs.PC) // skip one instruction
    }

    @Test
    fun skipIfVxEqNNComparisonFails() {    // 3XNN
        val startPC = cpu.regs.PC
        val opcode = 0x3ABA
        cpu.regs.V[0xA] = 0xAB

        skipIfVxEqNN(opcode, cpu)
        assertEquals(startPC + 2, cpu.regs.PC) // DO NOT skip one instruction
    }

    @Test
    fun skipIfVxNotEqNN() { // 4XNN
        val startPC = cpu.regs.PC
        val opcode = 0x4CCD
        cpu.regs.V[0xC] = 0xBA

        skipIfVxNotEqNN(opcode, cpu)
        assertEquals(startPC + 4, cpu.regs.PC) // skip one instruction
    }

    @Test
    fun skipIfVxNotEqNNComparisonFails() { // 4XNN
        val startPC = cpu.regs.PC
        val opcode = 0x4CCD
        cpu.regs.V[0xC] = 0xCD

        skipIfVxNotEqNN(opcode, cpu)
        assertEquals(startPC + 2, cpu.regs.PC) // DO NOT skip one instruction
    }

    @Test
    fun skipIfVxEqVy() {            // 5XY0
        val startPC = cpu.regs.PC
        val opcode = 0x5AB0
        cpu.regs.V[0xA] = 0x1
        cpu.regs.V[0xB] = 0x1

        skipIfVxEqVy(opcode, cpu)
        assertEquals(startPC + 4, cpu.regs.PC) // skip one instruction
    }

    @Test
    fun skipIfVxEqVyComparisonFails() {            // 5XY0
        val startPC = cpu.regs.PC
        val opcode = 0x5AB0
        cpu.regs.V[0xA] = 0x2
        cpu.regs.V[0xB] = 0x1

        skipIfVxEqVy(opcode, cpu)
        assertEquals(startPC + 2, cpu.regs.PC) // skip one instruction
    }

    @Test
    fun skipIfVxNotEqVy() {                         // 9XY0
        val startPC = cpu.regs.PC
        val opcode = 0x9AB0
        cpu.regs.V[0xA] = 0x1
        cpu.regs.V[0xB] = 0x2

        skipIfVxNotEqVy(opcode, cpu)
        assertEquals(startPC + 4, cpu.regs.PC) // skip one instruction
    }

    @Test
    fun skipIfVxNotEqVyComparisonFails() {          // 9XY0
        val startPC = cpu.regs.PC
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
    }

    @Test
    fun setVxToVxPlusNN() {
    }

    @Test
    fun setVxToVy() {               // 8XY0
        val opcode = 0x8AB0
        cpu.regs.V[0xB] = 0xCD
        setVxToVy(opcode, cpu)
        assertEquals(0xCD, cpu.regs.V[0xA])
    }

    @Test
    fun setVxToVxOrVy() {           // 8XY1
        val opcode = 0x8AB1
        cpu.regs.V[0xA] = 0b11001010
        cpu.regs.V[0xB] = 0b01011001
        setVxToVxOrVy(opcode, cpu)
        assertEquals(0b11011011, cpu.regs.V[0xA])
    }

    @Test
    fun setVxToVxAndVy() {          // 8XY2
        val opcode = 0x8AB2
        cpu.regs.V[0xA] = 0b11001010
        cpu.regs.V[0xB] = 0b01011001
        setVxToVxAndVy(opcode, cpu)
        assertEquals(0b01001000, cpu.regs.V[0xA])
    }

    @Test
    fun setVxToVxXorVy() {          // 8XY3
        val opcode = 0x8AB3
        cpu.regs.V[0xA] = 0b11001010
        cpu.regs.V[0xB] = 0b01011001
        setVxToVxXorVy(opcode, cpu)
        assertEquals(0b10010011, cpu.regs.V[0xA])
    }

    @Test
    fun setVxToVxPlusVy() {         // 8XY4
        val opcode = 0x8AB4
        cpu.regs.V[0xA] = 0x23
        cpu.regs.V[0xB] = 0x01
        setVxToVxPlusVy(opcode, cpu)
        assertEquals(0x24, cpu.regs.V[0xA])
        assertEquals(0x00, cpu.regs.V[0xF])
    }

    @Test
    fun setVxToVxPlusVyWithCarry() {         // 8XY4
        val opcode = 0x8AB4
        cpu.regs.V[0xA] = 0xFE
        cpu.regs.V[0xB] = 0x03
        setVxToVxPlusVy(opcode, cpu)
        assertEquals(0x01, cpu.regs.V[0xA])
        assertEquals(0x01, cpu.regs.V[0xF])
    }

    @Test
    fun setVxToVxMinusVy() {                // 8XY5
        val opcode = 0x8AB5
        cpu.regs.V[0xA] = 0x23
        cpu.regs.V[0xB] = 0x01
        setVxToVxMinusVy(opcode, cpu)
        assertEquals(0x22, cpu.regs.V[0xA])
        assertEquals(0x00, cpu.regs.V[0xF])
    }

    @Test
    fun setVxToVxMinusVyWithBorrow() {                // 8XY5
        val opcode = 0x8AB5
        cpu.regs.V[0xA] = 0x23
        cpu.regs.V[0xB] = 0x25
        setVxToVxMinusVy(opcode, cpu)
        assertEquals(0xFE, cpu.regs.V[0xA])
        assertEquals(0x01, cpu.regs.V[0xF])
    }

    @Test
    fun setVxToVxShr1() {           // 8XY6
        val opcode = 0x8AB6
        cpu.regs.V[0xA] = 0b1001_1001
        setVxToVxShr1(opcode, cpu)
        assertEquals(0b0100_1100, cpu.regs.V[0xA])
        assertEquals(0x01, cpu.regs.V[0xF])
    }

    @Test
    fun setVxToVxShr1WithLsb0() {           // 8XY6
        val opcode = 0x8AB6
        cpu.regs.V[0xA] = 0b1001_1000
        setVxToVxShr1(opcode, cpu)
        assertEquals(0b0100_1100, cpu.regs.V[0xA])
        assertEquals(0x00, cpu.regs.V[0xF])
    }

    @Test
    fun setVxToVyMinusVx() {                // 8XY7
        val opcode = 0x8AB7
        cpu.regs.V[0xA] = 0x23
        cpu.regs.V[0xB] = 0x25
        setVxToVyMinusVx(opcode, cpu)
        assertEquals(0x02, cpu.regs.V[0xA])
        assertEquals(0x00, cpu.regs.V[0xF])
    }

    @Test
    fun setVxToVyMinusVxWithBorrow() {             // 8XY7
        val opcode = 0x8AB7
        cpu.regs.V[0xA] = 0x03
        cpu.regs.V[0xB] = 0x02
        setVxToVyMinusVx(opcode, cpu)
        assertEquals(0xFF, cpu.regs.V[0xA])
        assertEquals(0x01, cpu.regs.V[0xF])
    }

    @Test
    fun setVxToVxShl1() {           // 8XYE
        val opcode = 0x8ABE
        cpu.regs.V[0xA] = 0b1001_1001
        setVxToVxShl1(opcode, cpu)
        assertEquals(0b0011_0010, cpu.regs.V[0xA])
        assertEquals(0x01, cpu.regs.V[0xF])
    }

    @Test
    fun setVxToVxShl1WithMsb0() {           // 8XYE
        val opcode = 0x8ABE
        cpu.regs.V[0xA] = 0b0001_1001
        setVxToVxShl1(opcode, cpu)
        assertEquals(0b0011_0010, cpu.regs.V[0xA])
        assertEquals(0x00, cpu.regs.V[0xF])
    }

    @Test
    fun setVxToRandAndNN() {
    }

    @Test
    fun setVxToDelayTimer() {
    }

    @Test
    fun setDelayTimerToVx() {
    }

    @Test
    fun setSoundTimerToVx() {
    }

    @Test
    fun setIToNNN() {
    }

    @Test
    fun setIToIPlusVx() {
    }

    @Test
    fun setIToSpriteLocation() {
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