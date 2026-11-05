package com.dentex.troller.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Typeface
import com.dentex.troller.chart.ChartStyle

/**
 * Icons of the seamark layer, drawn at runtime so the app needs no sprite sheet.
 *
 * Hazards and lights follow paper chart symbols: a magenta flare for a light, an asterisk for a
 * rock, the wreck symbol and a dotted circle for an obstruction. Harbour facilities are lettered
 * badges.
 */
object SeamarkIcons {

    private const val INK = 0xFF1D2B36.toInt()
    private const val HALO = 0xFFFFFFFF.toInt()
    private const val MAGENTA = 0xFFA8166E.toInt()

    private val BADGES = mapOf(
        "harbour" to ('H' to 0xFF2B5D8A.toInt()),
        "marina" to ('M' to 0xFF2B5D8A.toInt()),
        "slipway" to ('S' to 0xFF1F7A6E.toInt()),
        "fuel" to ('F' to 0xFFC4661F.toInt()),
        "anchorage" to ('A' to 0xFF7A3FA0.toInt()),
    )

    /** Every icon by image name, see [ChartStyle.seamarkImage]. */
    fun all(context: Context): Map<String, Bitmap> {
        val density = context.resources.displayMetrics.density
        return ChartStyle.SEAMARK_ICONS.associate { kind ->
            val bitmap = when (kind) {
                "light" -> light(density)
                "rock" -> rock(density)
                "wreck" -> wreck(density)
                "obstruction" -> obstruction(density)
                else -> {
                    val (letter, color) = BADGES.getValue(kind)
                    badge(density, letter, color)
                }
            }
            bitmap.density = context.resources.displayMetrics.densityDpi
            ChartStyle.seamarkImage(kind) to bitmap
        }
    }

    private fun canvas(density: Float, sizeDp: Float): Pair<Bitmap, Canvas> {
        val size = (sizeDp * density).toInt()
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        return bitmap to Canvas(bitmap)
    }

    private fun stroke(color: Int, width: Float) = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        this.color = color
        style = Paint.Style.STROKE
        strokeWidth = width
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }

    private fun fill(color: Int) = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        this.color = color
        style = Paint.Style.FILL
    }

    /** The light flare: a magenta teardrop leaning from the position dot. */
    private fun light(density: Float): Bitmap {
        val (bitmap, canvas) = canvas(density, 22f)
        val s = bitmap.width.toFloat()
        val flare = Path().apply {
            moveTo(s * 0.5f, s * 0.62f)
            cubicTo(s * 0.5f, s * 0.38f, s * 0.62f, s * 0.18f, s * 0.82f, s * 0.08f)
            cubicTo(s * 0.86f, s * 0.30f, s * 0.74f, s * 0.52f, s * 0.5f, s * 0.62f)
            close()
        }
        canvas.drawPath(flare, stroke(HALO, s * 0.08f))
        canvas.drawPath(flare, fill(MAGENTA))
        canvas.drawCircle(s * 0.5f, s * 0.62f, s * 0.09f, fill(HALO))
        canvas.drawCircle(s * 0.5f, s * 0.62f, s * 0.06f, fill(INK))
        return bitmap
    }

    /** A rock: an asterisk. */
    private fun rock(density: Float): Bitmap {
        val (bitmap, canvas) = canvas(density, 16f)
        val s = bitmap.width.toFloat()
        val c = s / 2
        val r = s * 0.36f
        val star = Path()
        for (i in 0 until 4) {
            val angle = Math.toRadians(i * 45.0)
            val dx = (Math.cos(angle) * r).toFloat()
            val dy = (Math.sin(angle) * r).toFloat()
            star.moveTo(c - dx, c - dy)
            star.lineTo(c + dx, c + dy)
        }
        canvas.drawPath(star, stroke(HALO, s * 0.2f))
        canvas.drawPath(star, stroke(INK, s * 0.09f))
        return bitmap
    }

    /** A wreck: a hull line with three masts. */
    private fun wreck(density: Float): Bitmap {
        val (bitmap, canvas) = canvas(density, 20f)
        val s = bitmap.width.toFloat()
        val shape = Path().apply {
            moveTo(s * 0.12f, s * 0.5f)
            lineTo(s * 0.88f, s * 0.5f)
            for (x in listOf(0.35f, 0.5f, 0.65f)) {
                moveTo(s * x, s * (if (x == 0.5f) 0.25f else 0.33f))
                lineTo(s * x, s * (if (x == 0.5f) 0.75f else 0.67f))
            }
        }
        canvas.drawPath(shape, stroke(HALO, s * 0.18f))
        canvas.drawPath(shape, stroke(INK, s * 0.08f))
        return bitmap
    }

    /** An obstruction: a dotted circle. */
    private fun obstruction(density: Float): Bitmap {
        val (bitmap, canvas) = canvas(density, 18f)
        val s = bitmap.width.toFloat()
        canvas.drawCircle(s / 2, s / 2, s * 0.36f, fill(0x55FFFFFF))
        canvas.drawCircle(s / 2, s / 2, s * 0.36f, stroke(INK, s * 0.08f).apply {
            pathEffect = DashPathEffect(floatArrayOf(s * 0.08f, s * 0.12f), 0f)
        })
        return bitmap
    }

    /** A round badge with a white letter. */
    private fun badge(density: Float, letter: Char, color: Int): Bitmap {
        val (bitmap, canvas) = canvas(density, 20f)
        val s = bitmap.width.toFloat()
        canvas.drawCircle(s / 2, s / 2, s * 0.46f, fill(HALO))
        canvas.drawCircle(s / 2, s / 2, s * 0.40f, fill(color))
        val text = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            this.color = HALO
            textSize = s * 0.5f
            textAlign = Paint.Align.CENTER
            typeface = Typeface.DEFAULT_BOLD
        }
        val baseline = s / 2 - (text.descent() + text.ascent()) / 2
        canvas.drawText(letter.toString(), s / 2, baseline, text)
        return bitmap
    }
}

/** The icon of a pin: an orange map pin whose tip marks the spot. */
fun pinIcon(context: Context, color: String): Bitmap {
    val metrics = context.resources.displayMetrics
    val w = (22 * metrics.density).toInt()
    val h = (30 * metrics.density).toInt()
    val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
    bitmap.density = metrics.densityDpi
    val canvas = Canvas(bitmap)
    val s = w.toFloat()
    val r = s * 0.42f
    val cx = s / 2
    val cy = r + s * 0.06f
    val stroke = s * 0.07f
    // One outline: the tip, a tangent line up to the head, the arc over the top, and the other
    // tangent back down. A circle plus a separate triangle would also stroke the triangle's top
    // edge as a white line across the head.
    val tipY = h - stroke
    val half = Math.toDegrees(kotlin.math.acos((r / (tipY - cy)).toDouble())).toFloat()
    val body = Path().apply {
        moveTo(cx, tipY)
        val right = Math.toRadians((90f - half).toDouble())
        lineTo(cx + r * kotlin.math.cos(right).toFloat(), cy + r * kotlin.math.sin(right).toFloat())
        arcTo(RectF(cx - r, cy - r, cx + r, cy + r), 90f - half, -(360f - 2 * half))
        close()
    }
    canvas.drawPath(body, Paint(Paint.ANTI_ALIAS_FLAG).apply {
        this.color = android.graphics.Color.parseColor(color)
        style = Paint.Style.FILL
    })
    canvas.drawPath(body, Paint(Paint.ANTI_ALIAS_FLAG).apply {
        this.color = android.graphics.Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = stroke
        strokeJoin = Paint.Join.ROUND
    })
    canvas.drawCircle(cx, cy, r * 0.38f, Paint(Paint.ANTI_ALIAS_FLAG).apply { this.color = android.graphics.Color.WHITE })
    return bitmap
}
