package ows.kotlinstudy.tinder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    /**
     * Activity, Fragment의 onActivityResult 메소드로부터 Facebook SDK에 콜백 호출을 위한 Manager
     * Pass the activity result back to the Facebook SDK
     */
    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth
        callbackManager = CallbackManager.Factory.create()

        initLoginButton()
        initSignUpButton()
        initEmailAndPassword()
        initFacebookLoginButton()
    }

    private fun initLoginButton() {
        val loginButton = findViewById<Button>(R.id.loginButton)
        loginButton.setOnClickListener {
            val email = getInputEmail()
            val password = getInputPassword()

            /**
             * Email, Password를 사용하여 로그인
             */
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    handleSuccessLogin()
                } else {
                    Toast.makeText(this, "로그인에 실패했습니다. 이메일 또는 비밀번호를 확인해주세요.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun initSignUpButton() {
        Log.d("msg","initSignUpButton")
        val signUpButton = findViewById<Button>(R.id.signUpButton)
        signUpButton.setOnClickListener {
            val email = getInputEmail()
            val password = getInputPassword()

            /**
             * Email과 Password의 유효성을 검사 후 사용자 생성
             * createUserWithEmailAndPassword -> Task<AutoResult> 반환, AutoResult에 사용자에 대한 정보 존재
             * addOnCompleListener : Task가 끝났을 때 호출되는 리스너, 비동기로 수행
             */
            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->
                if(task.isSuccessful){
                    Toast.makeText(this, "회원가입에 성공했습니다. 로그인 버튼을 눌러 로그인해주세요.", Toast.LENGTH_LONG).show()
                } else{
                    Toast.makeText(this, "회원가입에 실패했습니다.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun initEmailAndPassword(){
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val signUpButton = findViewById<Button>(R.id.signUpButton)

        emailEditText.addTextChangedListener {
            val enable = emailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()
            loginButton.isEnabled = enable
            signUpButton.isEnabled = enable
        }

        passwordEditText.addTextChangedListener {
            val enable = emailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()
            loginButton.isEnabled = enable
            signUpButton.isEnabled = enable
        }
    }

    private fun initFacebookLoginButton(){
        val facebookLoginButton = findViewById<LoginButton>(R.id.facebookLoginButton)

        /**
         * LoginButton : LoginManager에서 사용할 수 있는 기능에 대한 Wrapper UI 요소
         */
        facebookLoginButton.setPermissions("email","public_profile")    // email, public_profile 권한 부여

        /**
         * CallbackManager에 callback 등
         */
        facebookLoginButton.registerCallback(callbackManager, object: FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                // 로그인 성공, result에 AccessToken 포함

                val credential = FacebookAuthProvider.getCredential(result.accessToken.token)
                auth.signInWithCredential(credential).addOnCompleteListener(this@LoginActivity) {task ->
                    if(task.isSuccessful){
                        handleSuccessLogin()
                    } else{
                        Toast.makeText(this@LoginActivity, "페이스북 로그인이 실패했습니다.", Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onCancel() {}

            override fun onError(error: FacebookException?) {
                Toast.makeText(this@LoginActivity, "페이스북 로그인이 실패했습니다.",Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun getInputEmail(): String {
        return findViewById<EditText>(R.id.emailEditText).text.toString()
    }

    private fun getInputPassword(): String {
        return findViewById<EditText>(R.id.passwordEditText).text.toString()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleSuccessLogin(){
        if(auth.currentUser == null){
            Toast.makeText(this, "로그인에 실패했습니다.",Toast.LENGTH_LONG).show()
            return
        }

        /**
         * 데이터 업데이트 : setValue() or updateChildren
         * setValue : 모든 데이터를 덮어 씌움
         * updateChildren : 특정 DB에 동시에 업데이트 필요시 사용
         */
        val userId = auth.currentUser?.uid.orEmpty()
        val currentUserDB = Firebase.database.reference.child("Users").child(userId)
        val user = mutableMapOf<String, Any>()
        user["userId"] = userId
        currentUserDB.updateChildren(user)

        finish()
    }
}