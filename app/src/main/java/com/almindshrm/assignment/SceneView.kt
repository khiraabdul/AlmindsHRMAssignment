package com.almindshrm.assignment

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import java.util.ArrayList
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class SceneView : View {

    var viewMatrix = Matrix()
    var invertMatrix = Matrix()
    var paint = Paint()
    var rectangles = ArrayList<RectF>()
    var points = ArrayList<Point>()
    var moving: RectF? = null
    var isDraw = false

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        event.transform(invertMatrix)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                moving = null
                for (f in rectangles) {
                    if (f.contains(event.x, event.y)) {
                        moving = f
                        return true
                    }
                }
                invertMatrix = Matrix(viewMatrix)
                invertMatrix.invert(invertMatrix)
            }
            MotionEvent.ACTION_MOVE -> if (moving != null) {
                moving!![event.x - 10, event.y - 10, event.x + 10] = event.y + 10
            }
            MotionEvent.ACTION_UP -> if (moving == null) {
                val x = event.x.toInt()
                val y = event.y.toInt()
                points.add(Point(x, y))
                rectangles.add(RectF(event.x - 10, event.y - 10, event.x + 10, event.y + 10))
            }
        }
        invalidate()
        return true
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        if (!isDraw) {
            canvas.concat(viewMatrix)
            for (f in rectangles) {
                canvas.drawRect(f, paint)
            }
            if (rectangles.size == 3) {
                Log.e("tag", rectangles.size.toString() + " = = = ")
                isDraw = true
                val r1 = points[0]
                val r2 = points[1]
                val r3 = points[2]
                val paint = Paint()
                paint.isAntiAlias = true
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = 2f
                canvas.drawPath(
                    drawCurvedArrow(
                        r1.x.toFloat(),
                        r1.y.toFloat(),
                        r2.x.toFloat(),
                        r2.y.toFloat(),
                        360.0
                    ), paint
                )
                canvas.drawPath(
                    drawCurvedArrow(
                        r2.x.toFloat(),
                        r2.y.toFloat(),
                        r3.x.toFloat(),
                        r3.y.toFloat(),
                        360.0
                    ), paint
                )
                canvas.drawPath(
                    drawCurvedArrow(
                        r3.x.toFloat(),
                        r3.y.toFloat(),
                        r1.x.toFloat(),
                        r1.y.toFloat(),
                        360.0
                    ), paint
                )

            }
        } else {
            isDraw = false
            points.clear()
            rectangles.clear()
        }
    }

    private fun drawCurvedArrow(x1: Float, y1: Float, x2: Float, y2: Float, curveRadius: Double): Path {
        val path = Path()
        val midX = x1 + (x2 - x1) / 2
        val midY = y1 + (y2 - y1) / 2
        val xDiff = midX - x1
        val yDiff = midY - y1
        val angle = atan2(yDiff.toDouble(), xDiff.toDouble()) * (180 / Math.PI) - 90
        val angleRadians = Math.toRadians(angle)
        val pointX = (midX + curveRadius * cos(angleRadians)).toFloat()
        val pointY = (midY + curveRadius * sin(angleRadians)).toFloat()
        path.moveTo(x1, y1)
        path.cubicTo(x1, y1, pointX, pointY, x2, y2)
        return path
    }

    companion object {
        fun angleOf(p1: Point, p2: Point): Double {
            val deltaY = (p1.y - p2.y).toDouble()
            val deltaX = (p2.x - p1.x).toDouble()
            val result = Math.toDegrees(atan2(deltaY, deltaX))
            return if (result < 0) 360.0 + result else result
        }
    }
}