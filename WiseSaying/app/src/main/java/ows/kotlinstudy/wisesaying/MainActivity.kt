package ows.kotlinstudy.wisesaying

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.absoluteValue

class MainActivity : AppCompatActivity() {

    private val viewPager: ViewPager2 by lazy {
        findViewById<ViewPager2>(R.id.viewPager)
    }

    private val progressBar : ProgressBar by lazy {
        findViewById<ProgressBar>(R.id.progressBar)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        initData()
    }

    private fun initViews(){
        /**
         * 포커스된 페이지에서 변형될 때 호출되는 메소드
         * -2 -1 0 1 2 : 0이 포커스된 페이지
         */
        viewPager.setPageTransformer { page, position ->
            when{
                position.absoluteValue >= 1F -> {
                    page.alpha = 1F
                }
                position == 0F -> {
                    page.alpha = 1F
                }
                else -> {
                    page.alpha = 1F - 2 * position.absoluteValue
                }
            }
        }
    }

    private fun initData() {
        val remoteConfig = Firebase.remoteConfig
        remoteConfig.setConfigSettingsAsync(
            remoteConfigSettings {
                minimumFetchIntervalInSeconds = 0       // 최소 Remote Config fecth 타밈, 상용은 기본값이 12시간 사용s
            }
        )
        remoteConfig.fetchAndActivate().addOnCompleteListener {
            if(it.isSuccessful){
                progressBar.visibility = View.GONE
                val quotes = parseQuotesJson(remoteConfig.getString("quotes"))
                val isNameReveled = remoteConfig.getBoolean("is_name_revealed")

                displayQuotesPager(quotes, isNameReveled)
            }
        }
    }

    private fun displayQuotesPager(quotes : List<Quote>, isNameReveled : Boolean){
        val adapter = QuotesPagerAdapter(
            quotes = quotes,
            isNameRevealed = isNameReveled
        )

        viewPager.adapter = adapter
        viewPager.setCurrentItem(adapter.itemCount/2, false)    // 현재 포커스 아이템 설정
    }

    private fun parseQuotesJson(json : String) : List<Quote> {
        val jsonArray = JSONArray(json)
        var jsonList = emptyList<JSONObject>()
        for(index in 0 until jsonArray.length()){
            val jsonObjet = jsonArray.getJSONObject(index)
            jsonObjet?.let{
                jsonList = jsonList + it
            }
        }

        return jsonList.map {
            Quote(
                quote = it.getString("quote"),
                name = it.getString("name")
            )
        }
    }
}