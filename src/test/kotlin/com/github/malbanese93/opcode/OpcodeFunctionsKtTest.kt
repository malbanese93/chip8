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
    fun skipIfVxNotEqNN() {
    }

    @Test
    fun skipIfVxEqVy() {
    }

    @Test
    fun skipIfVxNotEqVy() {
    }

    @Test
    fun setVxToNN() {
    }

    @Test
    fun setVxToVxPlusNN() {
    }

    @Test
    fun setVxToVy() {
    }

    @Test
    fun setVxToVxOrVy() {
    }

    @Test
    fun setVxToVxAndVy() {
    }

    @Test
    fun setVxToVxXorVy() {
    }

    @Test
    fun setVxToVxPlusVy() {
    }

    @Test
    fun setVxToVxMinusVy() {
    }

    @Test
    fun setVxToVxShr1() {
    }

    @Test
    fun setVxToVyMinusVx() {
    }

    @Test
    fun setVxToVxShl1() {
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