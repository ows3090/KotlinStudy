## CountDownTimer
카운트 다운하기 위한 Timer
```kotlin
        object : CountDownTimer(initialMillis, 1000L) {
            override fun onFinish() {
                completeCountDown()
            }

            override fun onTick(millisUntilFinished: Long) {
                updateRemainTimes(millisUntilFinished)
                updateSeekBar(millisUntilFinished)
            }
        }
```
<br><br>

## SoundPool
SoundPool는 오디오 리소스를 실행하고 관리하는 클래스

