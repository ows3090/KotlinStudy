package ows.kotlinstudy.useddeal.home

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import ows.kotlinstudy.useddeal.DBKey
import ows.kotlinstudy.useddeal.DBKey.Companion.DB_ARTICLES
import ows.kotlinstudy.useddeal.R

class ArticleAddActivity : AppCompatActivity() {

    private var selectedUri: Uri? = null
    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }

    private val storage: FirebaseStorage by lazy {
        Firebase.storage
    }

    private val articleDB: DatabaseReference by lazy {
        Firebase.database.reference.child(DB_ARTICLES)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_add)

        findViewById<Button>(R.id.imageAddButton).setOnClickListener {
            when{
                ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    startContentProvider()
                }
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                    showPermissionContextPopup()
                }
                else -> {
                    requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1010)
                }
            }
        }

        findViewById<Button>(R.id.submitButton).setOnClickListener {
            val title = findViewById<EditText>(R.id.titleEditText).text.toString()
            val price = findViewById<EditText>(R.id.priceEditText).text.toString()
            val sellerId = auth.currentUser?.uid.orEmpty()

            showProgress()
            if(selectedUri != null){
                val photoUri = selectedUri ?: return@setOnClickListener
                uploadPhoto(photoUri,
                        successHandler = { uri ->
                            uploadArticle(sellerId, title, price, uri)
                        },
                        errorHandler = {
                            Toast.makeText(this, "사진 업로드에 실패했습니다", Toast.LENGTH_LONG).show()
                            hideProgress()
                        }
                    )
            } else{
                uploadArticle(sellerId, title, price, "")
            }
        }
    }
d
    private fun uploadPhoto(uri: Uri, successHandler: (String) -> Unit, errorHandler: () -> Unit){
        val fileName = "${System.currentTimeMillis()}.png"
        Log.d("msg","first uri : $uri")
        storage.reference.child("article/photo").child(fileName)
            .putFile(uri)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    storage.reference.child("article/photo").child(fileName)
                        .downloadUrl
                        .addOnSuccessListener {
                            successHandler(it.toString())
                        }.addOnFailureListener {
                            errorHandler()
                        }

                } else {
                    errorHandler()
                }
            }
    }

    private fun uploadArticle(sellerId: String, title: String, price: String, imageUri: String){
        Log.d("msg","second uri : $imageUri")
        val model = ArticleModel(sellerId, title,System.currentTimeMillis(), price, imageUri)
        articleDB.push().setValue(model)
        hideProgress()
        finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            1010 -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    startContentProvider()
                } else{
                    Toast.makeText(this, "권한을 거부하셨습니다.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    /**
     * SAF로 DocumentProvider에 쉽게 접근 가능
     */
    private fun startContentProvider(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, 2020)
    }

    private fun showProgress() {
        findViewById<ProgressBar>(R.id.progressbar).isVisible = true
    }

    private fun hideProgress() {
        findViewById<ProgressBar>(R.id.progressbar).isVisible = false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != RESULT_OK){
            return
        }

        when(requestCode){
            2020 -> {
                val uri = data?.data    // content:// 로 시작
                if(uri != null){
                    findViewById<ImageView>(R.id.photoImageView).setImageURI(uri)
                    selectedUri = uri
                } else{
                    Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_LONG).show()
                }
            }
            else -> {
                Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showPermissionContextPopup(){
        AlertDialog.Builder(this)
            .setTitle("권한이 필요합니다")
            .setMessage("사진을 가져오기 위해 필요합니다.")
            .setPositiveButton("동의") { _, _ ->
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1010)
            }
            .create()
            .show()
    }
}