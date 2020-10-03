package com.github.malbanese93.opcode

import com.github.malbanese93.hardware.CPU

enum class OpcodeMnemonic(
    val doOperation : (
        opcode : Int,
        cpu : CPU
    ) -> Unit
) {
    CALL_MACHINE_CODE(::notImplementedOperation),
    CLEAR_DISPLAY(::notImplementedOperation),
    RETURN_FROM_SUBROUTINE(::notImplementedOperation),
    JUMP(::notImplementedOperation),
    CALL_SUBROUTINE(::notImplementedOperation),
    SKIP_IF_VX_EQ_NN(::skipIfVxEqNN),
    SKIP_IF_VX_NOT_EQ_NN(::skipIfVxNotEqNN),
    SKIP_IF_VX_EQ_VY(::skipIfVxEqVy),
    SET_VX_TO_NN(::setVxToNN),
    ADD_NN_TO_VX(::addNNToVx),
    SET_VX_TO_VY(::setVxToVy),
    SET_VX_TO_VX_OR_VY(::setVxToVxOrVy),
    SET_VX_TO_VX_AND_VY(::setVxToVxAndVy),
    SET_VX_TO_VX_XOR_VY(::setVxToVxXorVy),
    SET_VX_TO_VX_PLUS_VY(::notImplementedOperation),
    SET_VX_TO_VX_MINUS_VY(::notImplementedOperation),
    SET_VX_TO_VX_SHR_1(::notImplementedOperation),
    SET_VX_TO_VY_MINUS_VX(::notImplementedOperation),
    SET_VX_TO_VX_SHL_1(::notImplementedOperation),
    SKIP_IF_VX_NOT_EQ_VY(::notImplementedOperation),
    SET_I_TO_NNN(::notImplementedOperation),
    JUMP_TO_V0_PLUS_NNN(::notImplementedOperation),
    SET_VX_TO_RAND_AND_NN(::notImplementedOperation),
    DRAW(::notImplementedOperation),
    SKIP_IF_KEY_EQ_VX(::notImplementedOperation),
    SKIP_IF_KEY_NOT_EQ_VX(::notImplementedOperation),
    GET_DELAY(::notImplementedOperation),
    GET_KEY(::notImplementedOperation),
    SET_DELAY_TIMER(::notImplementedOperation),
    SET_SOUND_TIMER(::notImplementedOperation),
    SET_I_TO_I_PLUS_VX(::notImplementedOperation),
    SET_I_TO_SPRITE_LOCATION(::notImplementedOperation),
    BCD(::notImplementedOperation),
    REGISTER_DUMP(::notImplementedOperation),
    REGISTER_LOAD(::notImplementedOperation)
}