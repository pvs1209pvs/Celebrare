package com.pvs.celebrare

import android.graphics.Typeface

data class TextStyle(
    var isBold : Boolean,
    var isItalic : Boolean,
    var isUnderlined : Boolean,
    var fontSize : Float,
    var fontFamily : Typeface
)
