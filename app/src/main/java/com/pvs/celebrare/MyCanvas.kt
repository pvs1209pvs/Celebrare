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

    private var userTextProp: TextProperties? = null
    private var selectedTextProp: TextProperties? = null


    fun attachViewModel(viewModel: MyViewModel, lifecycleOwner: LifecycleOwner) {

        viewModel.textProperties.observe(lifecycleOwner) {

            if (it == null) return@observe

            userTextProp = it

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

        if (userTextProp == null) return

        canvas.drawText(userTextProp?.text.toString(), userTextProp?.x!!, userTextProp?.y!!, paint)

    }

    var onCanvasEvent: ((textProperties: TextProperties) -> Unit)? = null


    override fun onTouchEvent(event: MotionEvent): Boolean {

        if (userTextProp == null) return true

        when (event.action) {

            MotionEvent.ACTION_DOWN -> {

                val textWidth = paint.measureText(userTextProp?.text)
                val textBottom = paint.fontMetrics.descent
                val textTop = paint.fontMetrics.ascent

                // calculate if the user is clicking on the text present on the canvas
                if (event.x >= userTextProp?.x!! && event.x <= userTextProp?.x!! + textWidth &&
                    event.y <= userTextProp?.y!! + textBottom && event.y >= userTextProp?.y!! + textTop
                ) {
                    selectedTextProp = userTextProp
                }

            }

            MotionEvent.ACTION_MOVE -> {
                selectedTextProp?.let {
                    it.x = event.x
                    it.y = event.y
                    invalidate()
                }
            }

            MotionEvent.ACTION_UP -> {
                if (userTextProp != null) {
                    onCanvasEvent?.invoke(userTextProp!!.copy())
                }
                selectedTextProp = null
            }

        }

        return true

    }

}