package com.github.malbanese93.opcode

import com.github.malbanese93.chip8.*
import com.github.malbanese93.exceptions.InvalidPixelHeightException
import com.github.malbanese93.extensions.highByte
import com.github.malbanese93.extensions.lowByte
import com.github.malbanese93.utils.START_PC
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class DrawOpcodeTest {

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
    fun drawOnEmptyBuffer() {        // DXYN
        /*
        * Draw the following pattern at the top left:
        * -█------
        * █-------
        * █-------
        * --█-----
        */
        cpu.regs.V[0xA] = 0
        cpu.regs.V[0xB] = 0
        cpu.regs.I = 0x300
        cpu.memory[cpu.regs.I] =   0b0100_0000
        cpu.memory[cpu.regs.I+1] = 0b1000_0000
        cpu.memory[cpu.regs.I+2] = 0b1000_0000
        cpu.memory[cpu.regs.I+3] = 0b0010_0000
        setNextInstruction(0xDAB4)

        cpu.update()
        assertEquals(true, cpu.videoBuffer[1,0])
        assertEquals(true, cpu.videoBuffer[0,1])
        assertEquals(true, cpu.videoBuffer[0,2])
        assertEquals(true, cpu.videoBuffer[2,3])
        assertEquals(0x00, cpu.regs.V[0xF])
    }

    @Test
    fun drawOnNonEmptyBuffer() {        // DXYN
        /*
        * Draw the following pattern at the top left:
        * -█------
        * █-------
        * █-------
        * --█-----
        * but with the buffer partially set:
        * ██------
        * █-------
        * --------
        * and so on
        *
        * This should be the result:
        * █-------
        * --------
        * █-------
        * --█-----
        */
        cpu.regs.V[0xA] = 0
        cpu.regs.V[0xB] = 0

        cpu.regs.I = 0x300
        cpu.memory[cpu.regs.I] =   0b0100_0000
        cpu.memory[cpu.regs.I+1] = 0b1000_0000
        cpu.memory[cpu.regs.I+2] = 0b1000_0000
        cpu.memory[cpu.regs.I+3] = 0b0010_0000

        cpu.videoBuffer[0,0] = true
        cpu.videoBuffer[1,0] = true
        cpu.videoBuffer[0,1] = true

        println("Starting buffer:")
        cpu.videoBuffer.dumpBuffer()

        setNextInstruction(0xDAB4)

        cpu.update()
        assertEquals(true, cpu.videoBuffer[0,0])
        assertEquals(false, cpu.videoBuffer[1,0])  // COLLISION
        assertEquals(false, cpu.videoBuffer[0,1])  // COLLISION
        assertEquals(true, cpu.videoBuffer[0,2])
        assertEquals(true, cpu.videoBuffer[2,3])
        assertEquals(0x01, cpu.regs.V[0xF])
    }

    @Test
    fun drawWrongNumberPixels() {
        setNextInstruction(0xDAB0)

        Assertions.assertThrows(InvalidPixelHeightException::class.java) { cpu.update() }
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