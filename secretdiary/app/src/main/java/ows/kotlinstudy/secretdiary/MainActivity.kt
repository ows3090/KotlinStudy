package ows.kotlinstudy.secretdiary

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.edit
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val numberPicker1: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numberPicker1)
            .apply {
                minValue = 0
                maxValue = 9
            }
    }

    private val numberPicker2: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numberPicker2)
            .apply {
                minValue = 0
                maxValue = 9
            }
    }

    private val numberPicker3: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numberPicker3)
            .apply {
                minValue = 0
                maxValue = 9
            }
    }

    private val openButton : Button by lazy {
        findViewById<Button>(R.id.openButton)
    }

    private val changePasswordButton : Button by lazy {
        findViewById<Button>(R.id.changePasswordButton)
    }

    private val textView: TextView by lazy{
        findViewById<TextView>(R.id.textView)
    }

    private var changePasswordMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView.postDelayed(Runnable {
            textView.setTextColor(Color.RED)
        },5000)

        openButton.setOnClickListener {
            // Preferences 라는 파일을 다른 외부 앱과 공유하도록 해주는 컴포넌트 , Mode 설정으로 현재 앱만 가능
            if(changePasswordMode){
                Toast.makeText(this, "비밀번호 변경 중입니다.",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE)

            val passwordFromUser = "${numberPicker1.value}${numberPicker2.value}${numberPicker3.value}"

            if(passwordPreferences.getString("password","000").equals(passwordFromUser)){
                // 패스워드 성공
                startActivity(Intent(this, DiaryActivity::class.java))
            }else{
                // 실패
                showErrorAlertDialog()
            }
        }

        changePasswordButton.setOnClickListener {
            val passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE)
            val passwordFromUser = "${numberPicker1.value}${numberPicker2.value}${numberPicker3.value}"

            if(changePasswordMode){
                // 번호를 저장하는 기능
                // commit : sync , apply : async
                passwordPreferences.edit(true) {
                    putString("password",passwordFromUser)
                }

                changePasswordMode = false
                changePasswordButton.setBackgroundColor(Color.BLACK)

            }else{
                // changePasswordMode 가 활성화 : 비밀번호가 맞는지를 체크
                if(passwordPreferences.getString("password","000").equals(passwordFromUser)){
                    // 패스워드 성공
                    changePasswordMode = true
                    Toast.makeText(this, "변경할 패스워드를 입력해주세요",Toast.LENGTH_LONG).show()
                    changePasswordButton.setBackgroundColor(Color.RED)
                }else{
                    // 실패
                    showErrorAlertDialog()
                }
            }
        }
    }

    private fun showErrorAlertDialog(){
        AlertDialog.Builder(this)
            .setTitle("실패!!")
            .setMessage("비밀번호가 잘못되었습니다.")
            .setPositiveButton("확인"){ _, _ -> }
            .create()
            .show()
    }
}