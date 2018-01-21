package com.rinon.shannoncode.managers

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Html

class ImageGetterHelper(private val context: Context,
                        private val widthRate: Float = 1f,
                        private val heightRate: Float = 1f) : Html.ImageGetter {

    override fun getDrawable(source: String?): Drawable {
        val id = context.resources.getIdentifier(source, "drawable", context.packageName)
        val drawable = context.resources.getDrawable(id, null)
        drawable.setBounds(0, 0, (drawable.intrinsicWidth * widthRate).toInt(), (drawable.intrinsicHeight * heightRate).toInt())

        return drawable
    }
}
