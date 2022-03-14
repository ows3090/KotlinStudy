package ows.kotlinstudy.recorder

import android.annotation.SuppressLint
import android.content.Context
import android.os.SystemClock
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * 녹음 시간 Count Up 을 위한 TextView
 *
 * Java 시간 체크
 * 1. System.currentTimeMillis : 현재 시간을 UTC의 millisecond로 리턴, 디바이스에 설정된 현재 시각 기준 리턴하기에 네트워크, 위치에 따라 변할 수도 있음.
 * 2. SystemClock.elapsedRealtime : 부팅된 시점부터 현재까 시간 millsecond 리턴, Sleep 상태도 측정, 시간 간격 측정하는데 용이.
 * 3. SystemClock.uptimeMills : SystemClock.elapsedRealtime도 동일하지만 Sleep 상태 미측정
 */
class CountUpView(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs) {

    /**
     * 초기 시간
     */
    private var startTimeStamp: Long = 0L

    /**
     * 1초 마다 CountUpView 갱신
     */
    private val countUpAction: Runnable = object : Runnable{
        override fun run() {
            val currentTimeStamp = SystemClock.elapsedRealtime()
            val countTimeSeconds = ((currentTimeStamp - startTimeStamp) / 1000L).toInt()
            updateaCountTime(countTimeSeconds)

            handler?.postDelayed(this, 1000L)
        }
    }

    /**
     * CountUpView Start
     * isStart == true : stop -> start => startTimeStamp를 초기화 해야함
     * isStart == false : pause -> start
     */
    fun startCountUp(isStart: Boolean) {
        if(isStart) {
            startTimeStamp = SystemClock.elapsedRealtime()
        }

        handler?.post(countUpAction)
    }

    /**
     * CountView Stop
     * isfinish == true : start -> stop
     * isfinish == false : start -> pause
     */
    fun stopCountUp(isfinish : Boolean) {
        if(isfinish){
            updateaCountTime(0)
        }
        handler?.removeCallbacks(countUpAction)
    }

    fun clearCountTime() {
        updateaCountTime(0)
    }

    @SuppressLint("SetTextI18n")
    private fun updateaCountTime(countTimeSeconds : Int){
        val minutes = countTimeSeconds / 60
        val seconds = countTimeSeconds % 60

        text = "%02d:%02d".format(minutes,seconds)
    }
}
