package ows.kotlinstudy.recorder

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View

class SoundVisualizerView(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    var onRequestCurrentAmplitude: (() -> Int)? = null

    /**
     * Draw 하기위한 Paint 도구
     * Canvas : 도화지, Paint : 붓
     * @color : 색깔
     * @strokeWidth : 굵기
     * @strokeCap : 선의 끝 부분 마감처리 (@BUTT, @ROUND, @SQUARE)
      */
    private val amplitudePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.getColor(R.color.purple_500)
        strokeWidth = LINE_WIDTH
        strokeCap = Paint.Cap.ROUND
    }

    private var drawingWidth: Int = 0
    private var drawingHeight: Int = 0
    private var drawingAmplitudes: List<Int> = emptyList()
    private var isReplaying: Boolean = false
    private var replayingPosition: Int = 0

    /**
     * isReplaying == true : replayingPosition에 해당되는 drawingAmplitude 크기만큼 선 그리기
     * isReplaying == false : drawingAmplitude에 currentAmplitude 추가
     */
    private val visualizeRepeatAction: Runnable = object : Runnable{
        override fun run() {
            if(!isReplaying){
                val currentAmplitude = onRequestCurrentAmplitude?.invoke() ?:0
                drawingAmplitudes = listOf(currentAmplitude) + drawingAmplitudes
            }else{
                replayingPosition++
            }
            invalidate()
            Log.d("msg","invalidate")

            // 20ms 반복
            handler?.postDelayed(this, ACTION_INTERVAL)
        }
    }

    /**
     * 해당 View의 사이즈가 변하거나, 초기 생성될 때 호출
     * w, h, oldw, oldh -> px 단위
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        drawingWidth = w
        drawingHeight = h
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?: return

        val centerY = drawingHeight / 2f
        var offsetX = drawingWidth.toFloat()

        drawingAmplitudes
            .let{ amplitudes ->
                if(isReplaying){
                    amplitudes.takeLast(replayingPosition)
                } else{
                    amplitudes
                }
            }
            .forEach {amplitude ->

            val lineLength = amplitude / MAX_AMPLITUE * drawingHeight * 0.8F

            offsetX -= LINE_SPACE
            if(offsetX < 0) return@forEach

            canvas.drawLine(
                offsetX,
                centerY - lineLength / 2F,
                offsetX,
                centerY + lineLength / 2F,
                amplitudePaint
            )
        }
    }

    fun startVisualizing(isReplaying: Boolean) {
        this.isReplaying = isReplaying
        handler?.post(visualizeRepeatAction)
    }

    fun stopVisualizing(isfinish : Boolean) {
        if(isfinish){
            replayingPosition = 0
        }
        handler?.removeCallbacks(visualizeRepeatAction)
    }

    fun clearVisualization(){
        drawingAmplitudes = emptyList()
        invalidate()
    }

    companion object {
        private const val LINE_WIDTH = 10F
        private const val LINE_SPACE = 15F
        private const val MAX_AMPLITUE = Short.MAX_VALUE.toFloat()
        private const val ACTION_INTERVAL = 20L
    }

}
