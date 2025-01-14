package ows.kotlinstudy.recorder

import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    private val soundVisualizerView : SoundVisualizerView by lazy {
        findViewById<SoundVisualizerView>(R.id.soundVisualizerView)
    }

    private val recordTimeTextView : CountUpView by lazy{
        findViewById<CountUpView>(R.id.recordTimeTextView)
    }

    private val recordButton: RecordButton by lazy {
        findViewById<RecordButton>(R.id.recordButton)
    }

    private val resetButton: Button by lazy{
        findViewById<Button>(R.id.resetButton)
    }

    private val recordingFilePath : String by lazy {
        "${externalCacheDir?.absolutePath}/recording.3gp"
    }
    private var recorder : MediaRecorder? = null
    private var player: MediaPlayer? = null
    private val requiredPermission = arrayOf(android.Manifest.permission.RECORD_AUDIO)
    private var state = State.BEFORE_RECORDING
        set(value) {
            field = value
            resetButton.isEnabled = (value == State.AFTER_RECORDING || value == State.ON_PLAYING)
            recordButton.updateIconWithState(value)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestAudioPermission()
        initViews()
        bindViews()
        initVariables()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        val audioRecordPermissionGranted = requestCode == REQUEST_RECORD_AUDIO_PERMISSION &&
                grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED

        if(!audioRecordPermissionGranted){
            finish()
        }
    }

    private fun requestAudioPermission() {
        requestPermissions(requiredPermission, REQUEST_RECORD_AUDIO_PERMISSION)
    }

    private fun initViews() {
        recordButton.updateIconWithState(state)
    }

    private fun bindViews(){
        soundVisualizerView.onRequestCurrentAmplitude = {
            recorder?.maxAmplitude ?: 0
        }

        recordButton.setOnClickListener {
            when(state){
                State.BEFORE_RECORDING -> {
                    startRecording()
                }
                State.ON_RECORDING -> {
                    stopRecording()
                }
                State.AFTER_RECORDING -> {
                    startPlaying()
                }
                State.ON_PLAYING -> {
                    stopPlaying(false)
                }
            }
        }

        resetButton.setOnClickListener {
            stopPlaying(true)
            soundVisualizerView.clearVisualization()
            recordTimeTextView.clearCountTime()
            state = State.BEFORE_RECORDING
        }
    }

    private fun initVariables() {
        state = State.BEFORE_RECORDING
    }

    private fun startRecording(){
        /**
         * MediaRecorder 초기화
         */
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)       // 오디오 소스 설정, 대게 MIC(마이크)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)       // 출력 포맷 설정, Android 8.0(API 26)부터는 스트리밍에 유용한 MPEG2_TS 포맷 지원
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)      // 오디오 인코더 설정
            setOutputFile(recordingFilePath)        // 출력 파일 이름 설정
            prepare()       // prepare를 호춯하여야 초기화 완료
        }
        recorder?.start()

        soundVisualizerView.startVisualizing(false) // Record O, Play X
        recordTimeTextView.startCountUp(true)
        state = State.ON_RECORDING
    }

    private fun stopRecording(){
        recorder?.run{
            stop()
            release()
        }
        recorder = null
        soundVisualizerView.stopVisualizing(true)
        recordTimeTextView.stopCountUp(true)
        state = State.AFTER_RECORDING
    }

    private fun startPlaying(){
        if(player == null){
            /**
             * MediaPlayer 초기화
             */
            player = MediaPlayer().apply {
                setDataSource(recordingFilePath)
                prepare()
            }
            player?.start()
            soundVisualizerView.startVisualizing(true)
            recordTimeTextView.startCountUp(true)
        }else{
            player?.start()
            soundVisualizerView.startVisualizing(true)
            recordTimeTextView.startCountUp(false)
        }

        player?.setOnCompletionListener {
            stopPlaying(true)
            state = State.AFTER_RECORDING
        }

        state = State.ON_PLAYING
    }

    private fun stopPlaying(isfinish : Boolean){
        if(isfinish){
            player?.release()
            player = null
            soundVisualizerView.stopVisualizing(true)
            recordTimeTextView.stopCountUp(true)
        }else{
            player?.pause()
            soundVisualizerView.stopVisualizing(false)
            recordTimeTextView.stopCountUp(false)
        }
        state = State.AFTER_RECORDING
    }

    companion object {
        private const val REQUEST_RECORD_AUDIO_PERMISSION = 201
    }
}