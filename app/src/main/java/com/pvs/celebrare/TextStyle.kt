package com.pvs.celebrare

import android.graphics.Typeface

data class TextStyle(
    var text:String?,
    var x:Float,
    var y:Float,
    var isBold : Boolean,
    var isItalic : Boolean,
    var isUnderlined : Boolean,
    var fontSize : Float,
    var fontFamily : Typeface
)
