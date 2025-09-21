package com.pvs.celebrare

import android.graphics.Typeface
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Stack

class MyViewModel : ViewModel() {

    private var _canvasText : MutableLiveData<String?> = MutableLiveData(null)
    val canvasText : LiveData<String?> = _canvasText

    private val _textStyle = MutableLiveData(
        TextStyle(
            false,
            false,
            false,
            64.0f,
            Typeface.DEFAULT
        )
    )
    val textStyle : LiveData<TextStyle> = _textStyle

    private val _undo = Stack<TextStyle>()
    private val _redo = Stack<TextStyle>()

    private fun addToUndo() {
        // do not save undo action if text is not present on the canvas yet
        if(_canvasText.value == null) return

        _undo.push(_textStyle.value?.copy())

        println("undo")
        println(_undo.joinToString("\n","",""))
    }

    fun doUndo() {

        if (_undo.isEmpty()) return

        _redo.push(_undo.pop())

        if(_undo.isNotEmpty()){
            _textStyle.value = _undo.peek().copy()
        }

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

        _textStyle.value = _textStyle.value?.copy(fontFamily = fontFamily)

        addToUndo()

    }

    fun toggleBold() {
        _textStyle.value = _textStyle.value?.copy(isBold = !_textStyle.value?.isBold!!)
        addToUndo()
    }

    fun toggleUnderline() {
        _textStyle.value = _textStyle.value?.copy(isUnderlined = !_textStyle.value?.isUnderlined!!)
        addToUndo()
    }

    fun toggleItalic() {
        _textStyle.value = _textStyle.value?.copy(isItalic = !_textStyle.value?.isItalic!!)
        addToUndo()
    }

    fun increaseFontSize() {
        _textStyle.value = _textStyle.value?.copy(fontSize = _textStyle.value?.fontSize!! + 8)
        addToUndo()
    }

    fun decreaseFontSize() {
        val crntFontSize = _textStyle.value?.fontSize!!
        if(crntFontSize == 8.0f) return // do not decrease the font size below 8
        _textStyle.value = _textStyle.value?.copy(fontSize = crntFontSize - 8)
        addToUndo()
    }

    fun addTextToCanvas(s: String) {
        _canvasText.value = s
        addToUndo() // saves the initial text style
    }


}