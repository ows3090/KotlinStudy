package ows.kotlinstudy.pushnotification

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

    private val resultTextView: TextView by lazy {
        findViewById<TextView>(R.id.resultTextView)
    }

    private val firebaseToken: TextView by lazy {
        findViewById<TextView>(R.id.firebaseTokenTextView)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initFirebase()
        updateResult()
    }

    /**
     * 테스크에 있는 Activity가 재사용될 경우 onCreate 대신 onNewIntent 호출
     * 해당 앱에서는 알림 클릭 시
     */
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d("msg","onNewIntent")

        setIntent(intent)
        updateResult(true)
    }

    /**
     * Firebase Cloud Message(FCM) 초기화
     *
     */
    private fun initFirebase() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("msg","${task.result}")
                    firebaseToken.text = task.result
                }
            }
    }

    private fun updateResult(isNewIntent: Boolean = false) {
        resultTextView.text = (intent.getStringExtra("notificationType") ?: "앱 런처") +
                if (isNewIntent) {
                    "(으)로 갱신했습니다"
                } else {
                    "(으)로 실행했습니다"
                }
    }
}