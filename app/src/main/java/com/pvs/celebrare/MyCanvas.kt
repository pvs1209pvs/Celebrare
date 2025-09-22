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

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        textSize = 64f
        typeface = Typeface.SERIF
    }

    private var userText: TextStyle? = null
    private var selectedText: TextStyle? = null


    fun attachViewModel(viewModel: MyViewModel, lifecycleOwner: LifecycleOwner) {

        viewModel.textStyle.observe(lifecycleOwner) {

            if (it == null) return@observe

            userText = it

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

        if (userText == null) return

        canvas.drawText(userText?.text.toString(), userText?.x!!, userText?.y!!, paint)

    }

    var onCanvasEvent: ((textStyle: TextStyle) -> Unit)? = null


    override fun onTouchEvent(event: MotionEvent): Boolean {

        if (userText == null) return true

        when (event.action) {

            MotionEvent.ACTION_DOWN -> {

                val textWidth = paint.measureText(userText?.text)
                val textBottom = paint.fontMetrics.descent
                val textTop = paint.fontMetrics.ascent

                // calculate if the user is clicking on the text present on the canvas
                if (event.x >= userText?.x!! && event.x <= userText?.x!! + textWidth &&
                    event.y <= userText?.y!! + textBottom && event.y >= userText?.y!! + textTop
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
                if (userText != null) {
                    onCanvasEvent?.invoke(userText!!.copy())
                }
                selectedText = null
            }

        }

        return true

    }

}