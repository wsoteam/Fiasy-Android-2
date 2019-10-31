package com.wsoteam.diet.views

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.Shader.TileMode.CLAMP
import android.graphics.Shader.TileMode.MIRROR
import android.graphics.drawable.Drawable

class SplashBackground : Drawable() {
  private val circles = arrayOf(
      Circle(0f, 0f, 20f, intArrayOf(0xb3b8f3f7.toInt(), 0xf06dced4.toInt(), 0xf02cadb5.toInt())),
      Circle(85f, 10f, 5f, intArrayOf(0xf0fff4bb.toInt(), 0xc2ffb800.toInt(), 0xef7d02)),
      Circle(75f, 50f, 10f, intArrayOf(0xf0fff4bb.toInt(), 0xc2ffb800.toInt(), 0xef7d02)),
      Circle(20f, 65f, 6.5f, intArrayOf(0xb3b8f3f7.toInt(), 0xf06dced4.toInt(), 0xf02cadb5.toInt())),
      Circle(87f, 83f, 4f, intArrayOf(0xf0fff4bb.toInt(), 0xc2ffb800.toInt(), 0xef7d02))
  )

  private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

  override fun draw(canvas: Canvas) {
    canvas.drawColor(Color.WHITE)

    val w = bounds.width()
    val h = bounds.height()

    circles.forEach {
      val x = it.x / 100 * w
      val y = it.y / 100 * h
      val r = it.radius / 100 * w.coerceAtLeast(h)

      paint.alpha = 35
      paint.shader = LinearGradient(x - r, y - r, x + r, y + r,
          it.gradient, null, CLAMP)

      canvas.drawCircle(x, y, r, paint)
    }
  }

  override fun setAlpha(alpha: Int) {

  }

  override fun setColorFilter(colorFilter: ColorFilter?) {

  }

  override fun getOpacity(): Int {
    return PixelFormat.TRANSLUCENT
  }

  data class Circle(
    val x: Float,
    val y: Float,
    val radius: Float,
    val gradient: IntArray)
}
