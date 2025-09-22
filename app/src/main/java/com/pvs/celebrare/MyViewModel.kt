package com.pvs.celebrare

import android.graphics.Typeface
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Stack

class MyViewModel : ViewModel() {



    private val _textProperties : MutableLiveData<TextProperties?> = MutableLiveData(null)
    val textProperties: LiveData<TextProperties?> = _textProperties

    private val _undo = Stack<TextProperties>()
    private val _redo = Stack<TextProperties>()

    private fun addToUndo() {

        // do not save undo action if text is not present on the canvas yet
        if (_textProperties.value == null) return

        _undo.push(_textProperties.value?.copy())

    }

    fun addToUndo(ts: TextProperties){
        _undo.push(ts)
    }

    fun doUndo() {

        if (_undo.isEmpty()) return

        _redo.push(_undo.pop())

        if (_undo.isNotEmpty()) {
            _textProperties.value = _undo.peek().copy()
        }

    }

    fun doRedo() {

        if (_redo.isEmpty()) return

        val top = _redo.pop()

        _undo.push(top)

        _textProperties.value = top.copy()

    }

    fun setFontFamily(font: String) {

        val fontFamily = when (font.lowercase()) {
            "sans-serif" -> Typeface.SANS_SERIF
            "serif" -> Typeface.SERIF
            "monospace" -> Typeface.MONOSPACE
            "default" -> Typeface.DEFAULT
            else -> Typeface.DEFAULT
        }

        _textProperties.value = _textProperties.value?.copy(fontFamily = fontFamily)

        addToUndo()

    }

    fun toggleBold() {
        _textProperties.value = _textProperties.value?.copy(isBold = !_textProperties.value?.isBold!!)
        addToUndo()
    }

    fun toggleUnderline() {
        _textProperties.value = _textProperties.value?.copy(isUnderlined = !_textProperties.value?.isUnderlined!!)
        addToUndo()
    }

    fun toggleItalic() {
        _textProperties.value = _textProperties.value?.copy(isItalic = !_textProperties.value?.isItalic!!)
        addToUndo()
    }

    fun increaseFontSize() {
        _textProperties.value = _textProperties.value?.copy(fontSize = _textProperties.value?.fontSize!! + 8)
        addToUndo()
    }

    fun decreaseFontSize() {
        val crntFontSize = _textProperties.value?.fontSize!!
        if (crntFontSize == 8.0f) return // do not decrease the font size below 8
        _textProperties.value = _textProperties.value?.copy(fontSize = crntFontSize - 8)
        addToUndo()
    }

    fun addTextToCanvas(s: String) {

        _textProperties.value = TextProperties(
            s,
            400f,
            400f,
            false,
            false,
            false,
            64.0f,
            Typeface.DEFAULT
        )

        addToUndo() // saves the initial text style
    }


}