package ows.kotlinstudy.recorder

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatImageButton

class RecordButton(context: Context, attrs: AttributeSet) : AppCompatImageButton(context, attrs){

    init {
        setBackgroundResource(R.drawable.shape_over_button)
    }

    fun updateIconWithState(state: State){
        when(state){
            State.BEFORE_RECORDING -> {
                setImageResource(R.drawable.ic_baseline_fiber_manual_record_24)
            }
            State.ON_RECORDING -> {
                setImageResource(R.drawable.ic_baseline_stop_24)
            }
            State.AFTER_RECORDING -> {
                setImageResource(R.drawable.ic_baseline_play_arrow_24)
            }
            State.ON_PLAYING -> {
                setImageResource(R.drawable.ic_baseline_stop_24)
            }
        }
    }
}