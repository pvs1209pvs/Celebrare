package com.pvs.celebrare

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.LifecycleOwner

class MyCanvas(context: Context, attrs: AttributeSet) : View(context, attrs) {


    data class TextData(
        val userText: String = "Paramvir",
        var x: Float = 400f,
        var y: Float = 400f
    )

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        textSize = 64f
        typeface = Typeface.SERIF
    }

    private val userText = TextData()
    private var selectedText: TextData? = null


    fun attachViewModel(viewModel: MyViewModel, lifecycleOwner: LifecycleOwner) {

        viewModel.textStyle.observe(lifecycleOwner) {

            val boldItalicStyle =
                if (it.isBold && it.isItalic) Typeface.BOLD_ITALIC
                else if (it.isBold) Typeface.BOLD
                else if (it.isItalic) Typeface.ITALIC
                else Typeface.NORMAL

            paint.setTypeface(Typeface.create(it.fontFamily, boldItalicStyle))
            paint.isUnderlineText = it.isUnderlined
            paint.textSize = it.fontSize

            invalidate()
        }

    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawText(userText.userText, userText.x, userText.y, paint)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {

        when (event.action) {

            MotionEvent.ACTION_DOWN -> {

                val textWidth = paint.measureText(userText.userText)
                val textBottom = paint.fontMetrics.descent
                val textTop = paint.fontMetrics.ascent

                if (event.x >= userText.x && event.x <= userText.x + textWidth &&
                    event.y <= userText.y + textBottom && event.y >= userText.y + textTop
                ) {
                    selectedText = userText
                }

            }

            MotionEvent.ACTION_MOVE -> {
                selectedText?.let {
                    it.x = event.x
                    it.y = event.y
                    invalidate()
                }
            }

            MotionEvent.ACTION_UP -> {
                selectedText = null
            }

        }

        return true
    }

}