package io.awesdroid.asrdemokt.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.google.common.primitives.Bytes

import java.util.ArrayList
import java.util.stream.IntStream

/**
 * Created by awesdroid.
 */

class VoiceSpectrum : View {

    private val lines = ArrayList<Byte>()
    private val lineNums = 400
    private val forePaint = Paint()
    private val points = FloatArray(4)

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        forePaint.strokeWidth = 1f
        forePaint.isAntiAlias = true
        forePaint.color = Color.YELLOW
    }

    fun setVisualizerColor(color: Int) {
        forePaint.color = color
    }


    fun updateVisualizer(fft: ByteArray) {
        val list = Bytes.asList(*fft)
        val extra = lines.size + fft.size - lineNums
        val linesSize = lines.size
        if (extra > 0) {
            IntStream.range(0, extra).forEach { i -> lines.removeAt(linesSize - i - 1) }
        }
        lines.addAll(0, list)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width
        val height = height

        val padding = width.toFloat() / lineNums
        val len = lines.size
        var i = 0
        while (i <= lineNums && i < len) {
            points[0] = width - padding * i
            points[1] = (height / 2 + 3 * lines[i] + bias).toFloat()
            points[2] = width - padding * i
            points[3] = (height / 2 - 3 * lines[i] - bias).toFloat()
            canvas.drawLines(points, forePaint)
            i++
        }
    }

    companion object {

        private val TAG = VoiceSpectrum::class.java.simpleName
        private val bias = 1 // To prevent drawing zero distance line
    }

}