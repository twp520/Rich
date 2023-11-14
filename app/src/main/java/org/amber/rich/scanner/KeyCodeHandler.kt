package org.amber.rich.scanner

import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.nativeKeyCode
import org.amber.rich.logD

/**
 * create by colin
 * 2023/5/7
 */
class KeyCodeHandler(val inputCallback: (code: String) -> Unit) {


    private val sBuilder = StringBuilder()
    private var isCaps = false

    fun handleKeyEvent(keyEvent: KeyEvent): Boolean {

        checkLetter(keyEvent)

        val inputCode = getInputCode(keyEvent)
        if (inputCode != null) {
            sBuilder.append(inputCode)
            logD("sBuilder = $sBuilder")
        }

        if (keyEvent.key.nativeKeyCode == android.view.KeyEvent.KEYCODE_ENTER
            && keyEvent.nativeKeyEvent.action == android.view.KeyEvent.ACTION_DOWN
        ) {
            logD("callback --->>> $sBuilder")
            val input = sBuilder.toString().trim()
            if (input.isNotEmpty() && input.isNotBlank()) {
                inputCallback.invoke(sBuilder.toString())
            }
            sBuilder.clear()
        }

        return true
    }

    private fun getInputCode(keyEvent: KeyEvent): Char? {
        var charCode: Char? = null
        if (keyEvent.nativeKeyEvent.action == android.view.KeyEvent.ACTION_DOWN) {
            when (keyEvent.key.nativeKeyCode) {
                in android.view.KeyEvent.KEYCODE_0..android.view.KeyEvent.KEYCODE_9 -> {
                    charCode = '0' + keyEvent.key.nativeKeyCode - android.view.KeyEvent.KEYCODE_0
                }

                in android.view.KeyEvent.KEYCODE_A..android.view.KeyEvent.KEYCODE_Z -> {
                    charCode =
                        (if (isCaps) 'A' else 'a') + keyEvent.key.nativeKeyCode - android.view.KeyEvent.KEYCODE_A
                }

                android.view.KeyEvent.KEYCODE_SPACE -> {
                    charCode = ' '
                }

                android.view.KeyEvent.KEYCODE_PERIOD -> {
                    charCode = '.'
                }

                android.view.KeyEvent.KEYCODE_MINUS -> {
                    charCode = if (isCaps) '_' else '-'
                }

                android.view.KeyEvent.KEYCODE_SLASH -> {
                    charCode = '/'
                }

                android.view.KeyEvent.KEYCODE_BACKSLASH -> {
                    charCode = if (isCaps) '|' else '\\'
                }

            }

        }
        return charCode
    }

    private fun checkLetter(event: KeyEvent) {
        val code = event.nativeKeyEvent.keyCode
        if (code == android.view.KeyEvent.KEYCODE_SHIFT_LEFT ||
            code == android.view.KeyEvent.KEYCODE_SHIFT_RIGHT
        ) {
            isCaps = event.nativeKeyEvent.action == android.view.KeyEvent.ACTION_DOWN
        }
    }

}