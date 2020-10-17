package com.github.malbanese93.opcode

import com.github.malbanese93.chip8.CPU

enum class OpcodeMnemonic(
    val doOperation : (
        opcode : Int,
        cpu : CPU
    ) -> Unit
) {
    CALL_MACHINE_CODE(::noOp),
    CLEAR_DISPLAY(::clearDisplay),
    RETURN_FROM_SUBROUTINE(::returnFromSubroutine),
    JUMP_TO_NNN(::jumpToNNN),
    CALL_SUBROUTINE(::callSubroutine),
    SKIP_IF_VX_EQ_NN(::skipIfVxEqNN),
    SKIP_IF_VX_NOT_EQ_NN(::skipIfVxNotEqNN),
    SKIP_IF_VX_EQ_VY(::skipIfVxEqVy),
    SET_VX_TO_NN(::setVxToNN),
    SET_VX_TO_VX_PLUS_NN(::setVxToVxPlusNN),
    SET_VX_TO_VY(::setVxToVy),
    SET_VX_TO_VX_OR_VY(::setVxToVxOrVy),
    SET_VX_TO_VX_AND_VY(::setVxToVxAndVy),
    SET_VX_TO_VX_XOR_VY(::setVxToVxXorVy),
    SET_VX_TO_VX_PLUS_VY(::setVxToVxPlusVy),
    SET_VX_TO_VX_MINUS_VY(::setVxToVxMinusVy),
    SET_VX_TO_VX_SHR_1(::setVxToVxShr1),
    SET_VX_TO_VY_MINUS_VX(::setVxToVyMinusVx),
    SET_VX_TO_VX_SHL_1(::setVxToVxShl1),
    SKIP_IF_VX_NOT_EQ_VY(::skipIfVxNotEqVy),
    SET_I_TO_NNN(::setIToNNN),
    JUMP_TO_V0_PLUS_NNN(::jumpToV0PlusNNN),
    SET_VX_TO_RAND_AND_NN(::setVxToRandAndNN),
    DRAW(::draw),
    SKIP_IF_KEY_EQ_VX(::notImplementedOperation),
    SKIP_IF_KEY_NOT_EQ_VX(::notImplementedOperation),
    SET_VX_TO_DELAY_TIMER(::setVxToDelayTimer),
    GET_KEY(::notImplementedOperation),
    SET_DELAY_TIMER_TO_VX(::setDelayTimerToVx),
    SET_SOUND_TIMER_TO_VX(::setSoundTimerToVx),
    SET_I_TO_I_PLUS_VX(::setIToIPlusVx),
    SET_I_TO_SPRITE_LOCATION(::setIToSpriteLocation),
    BCD(::bcd),
    REGISTER_DUMP(::dumpVxRegisters),
    REGISTER_LOAD(::loadVxRegisters)
}