package com.pvs.celebrare

import android.graphics.Typeface
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Stack

class MyViewModel : ViewModel() {

    private val _textStyle = MutableLiveData(
        TextStyle(
            false,
            false,
            false,
            64.0f,
            Typeface.DEFAULT
        )
    )
    val textStyle = _textStyle

    private val _undo = Stack<TextStyle>()
    private val _redo = Stack<TextStyle>()

    private fun addToUndo() {
        _undo.push(_textStyle.value?.copy())
    }

    fun doUndo() {

        if (_undo.isEmpty()) return

        _redo.push(_undo.pop())

        _textStyle.value =
            if (_undo.isEmpty()) TextStyle(false, false, false, 64.0f, Typeface.DEFAULT)
            else _undo.peek().copy()

    }

    fun doRedo() {

        if (_redo.isEmpty()) return

        val top = _redo.pop()

        _undo.push(top)

        _textStyle.value = top.copy()

    }


    fun setFontFamily(font: String) {

        val fontFamily = when (font.lowercase()) {
            "sans-serif" -> Typeface.SANS_SERIF
            "serif" -> Typeface.SERIF
            "monospace" -> Typeface.MONOSPACE
            "default" -> Typeface.DEFAULT
            else -> Typeface.DEFAULT
        }

        textStyle.value = textStyle.value?.copy(fontFamily = fontFamily)

        addToUndo()


    }

    fun toggleBold() {
        textStyle.value = textStyle.value?.copy(isBold = !_textStyle.value?.isBold!!)
        addToUndo()
    }

    fun toggleUnderline() {
        textStyle.value = textStyle.value?.copy(isUnderlined = !_textStyle.value?.isUnderlined!!)
        addToUndo()
    }

    fun toggleItalic() {
        textStyle.value = textStyle.value?.copy(isItalic = !_textStyle.value?.isItalic!!)
        addToUndo()
    }

    fun increaseFontSize() {
        textStyle.value = textStyle.value?.copy(fontSize = _textStyle.value?.fontSize!! + 8)
        addToUndo()
    }

    fun decreaseFontSize() {
        textStyle.value = textStyle.value?.copy(fontSize = _textStyle.value?.fontSize!! - 8)
        addToUndo()
    }


}