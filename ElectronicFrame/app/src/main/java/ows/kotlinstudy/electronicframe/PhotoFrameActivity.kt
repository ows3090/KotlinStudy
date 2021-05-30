package ows.kotlinstudy.electronicframe

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import java.util.*
import kotlin.concurrent.timer

class PhotoFrameActivity : AppCompatActivity() {

    private val photoList = mutableListOf<Uri>()

    private var currentPositioin = 0

    private var timer: Timer? = null

    private val photoImageView: ImageView by lazy{
        findViewById<ImageView>(R.id.photoImageView)
    }

    private val backgoundPhotoImageView : ImageView by lazy{
        findViewById<ImageView>(R.id.backgroundPhotoImageView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_frame)
        getPhotoUriFromIntent()
    }

    private fun getPhotoUriFromIntent(){
        val size = intent.getIntExtra("photoListSize", 0)
        for(i in 0..size){
            intent.getStringExtra("photo$i")?.let {
                photoList.add((Uri.parse(it)));
            }
        }
    }

    private fun startTimer(){
        // Not Main Thread
        timer = timer(period = 5*1000){
            runOnUiThread {
                val current = currentPositioin
                val next = if(photoList.size <= currentPositioin +1 ) 0 else currentPositioin+1

                backgoundPhotoImageView.setImageURI(photoList[current])

                photoImageView.alpha = 0f
                photoImageView.setImageURI(photoList[next])
                photoImageView.animate()
                    .alpha(1.0f)
                    .setDuration(2000)
                    .start()

                currentPositioin = next

            }
        }
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
        timer?.cancel()
    }

    override fun onStart() {
        super.onStart()
        startTimer()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
}