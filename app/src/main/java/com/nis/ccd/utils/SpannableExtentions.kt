package com.nis.ccd.utils

import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.core.text.inSpans

fun View.getCompatColor(@ColorRes colorRes: Int): Int =
    ContextCompat.getColor(this.context, colorRes)

fun TextView.applySpanPo(stringPrefix: String, stringSuffix: String, color: Int) {
    text = buildSpannedString {
        inSpans {
            bold { append(stringPrefix) }
        }.inSpans(ForegroundColorSpan(getCompatColor(color)), RelativeSizeSpan(0.7f)) {
            append(stringSuffix)
        }
    }
}