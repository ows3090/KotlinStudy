package ows.kotlinstudy.caculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.room.Room
import ows.kotlinstudy.caculator.model.History
import java.lang.NumberFormatException

class MainActivity : AppCompatActivity() {

    private val expressionTextView: TextView by lazy {
        findViewById<TextView>(R.id.expressionTextView);
    }

    private val resultTextView: TextView by lazy {
        findViewById<TextView>(R.id.resultTextView);
    }

    private val historyLayout : View by lazy {
        findViewById<View>(R.id.historyLayout)
    }

    private val historyLinearLayout : LinearLayout by lazy{
        findViewById<LinearLayout>(R.id.historyLinearLayout)
    }

    lateinit var db : AppDatabase

    private var isOperator = false;
    private var hasoperator = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = Room.databaseBuilder(
                this,
                AppDatabase::class.java,
                "historyDB"
        ).build()


    }

    fun buttonClicked(view: View) {
        when (view.id) {
            R.id.button0 -> numberButtonClicked("0")
            R.id.button1 -> numberButtonClicked("1")
            R.id.button2 -> numberButtonClicked("2")
            R.id.button3 -> numberButtonClicked("3")
            R.id.button4 -> numberButtonClicked("4")
            R.id.button5 -> numberButtonClicked("5")
            R.id.button6 -> numberButtonClicked("6")
            R.id.button7 -> numberButtonClicked("7")
            R.id.button8 -> numberButtonClicked("8")
            R.id.button9 -> numberButtonClicked("9")
            R.id.plusbutton -> operatorButtonClicked("+")
            R.id.minusbutton -> operatorButtonClicked("-")
            R.id.mulbutton -> operatorButtonClicked("*")
            R.id.divbutton -> operatorButtonClicked("/")
            R.id.modulerbutton -> operatorButtonClicked("%")
        }
    }

    private fun numberButtonClicked(number: String) {

        if (isOperator) {
            expressionTextView.append(" ")
        }
        isOperator = false

        val expressText = expressionTextView.text.split(" ")
        if (expressText.isNotEmpty() && expressText.last().length >= 15) {
            Toast.makeText(this, "15자리 까지만 입력할 수 있습니다", Toast.LENGTH_LONG).show()
            return
        } else if (expressText.last().isEmpty() && number == "0") {
            Toast.makeText(this, "0은 제일 앞에 올 수 없습니다", Toast.LENGTH_LONG).show()
            return
        }

        expressionTextView.append(number)

        // 실시간 계산
        resultTextView.text = caculateExpression()
    }

    private fun operatorButtonClicked(operator: String) {
        if (expressionTextView.text.isEmpty()) {
            return
        }

        when {
            isOperator -> {
                val text = expressionTextView.text.toString()
                expressionTextView.text = text.dropLast(1) + operator
            }

            hasoperator -> {
                Toast.makeText(this, "연산자는 한번만 사용할 수 있습니다", Toast.LENGTH_LONG).show()
                return
            }
            else -> {
                expressionTextView.append(" $operator")
            }
        }

        val ssb = SpannableStringBuilder(expressionTextView.text)
        ssb.setSpan(
                ForegroundColorSpan(getColor(R.color.green)),
                expressionTextView.text.length - 1,
                expressionTextView.text.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        expressionTextView.text = ssb

        isOperator = true
        hasoperator = true
    }

    private fun caculateExpression(): String {
        val expressiontTexts = expressionTextView.text.split(" ")

        if (hasoperator.not() || expressiontTexts.size != 3) {
            return ""
        } else if (expressiontTexts[0].isNumber().not() || expressiontTexts[2].isNumber().not()) {
            return ""
        }

        val exp1 = expressiontTexts[0].toBigInteger()
        val exp2 = expressiontTexts[2].toBigInteger()
        val op = expressiontTexts[1]

        return when (op) {
            "+" -> (exp1 + exp2).toString()
            "-" -> (exp1 - exp2).toString()
            "*" -> (exp1 * exp2).toString()
            "/" -> (exp1 / exp2).toString()
            "%" -> (exp1 % exp2).toString()
            else -> ""
        }
    }

    fun clearButtonClicked(view: View) {
        expressionTextView.text = ""
        resultTextView.text = ""
        isOperator = false
        hasoperator = false
    }

    fun resultButtonClicked(view: View) {
        val expressiontTexts = expressionTextView.text.split(" ")

        if(expressionTextView.text.isEmpty() || expressiontTexts.size == 1){
            return
        }

        if(expressiontTexts.size != 3 && hasoperator){
            Toast.makeText(this, "아직 완성되지 않은 수식입니다", Toast.LENGTH_LONG).show()
            return
        }

        if(expressiontTexts[0].isNumber().not() || expressiontTexts[2].isNumber().not()){
            Toast.makeText(this, "오류가 발생했습니다", Toast.LENGTH_LONG).show()
            return
        }

        val expressionText = expressionTextView.text.toString()
        val resultText = caculateExpression()

        // TODO DB 저장, DB에 저장하는 과정은 MainThread가 아닌 일반 Thread에서 진행

        Thread(Runnable {
            db.historyDao().insertHistory(History(null,expressionText, resultText))
        }).start()

        resultTextView.text = ""
        expressionTextView.text = resultText
        isOperator = false
        hasoperator = false
    }

    fun historyButtonClicked(view: View) {
        historyLayout.isVisible = true
        historyLinearLayout.removeAllViews()

        // TODO DB에서 데이터 가져오기
        Thread(Runnable {
            db.historyDao().getAll().reversed().forEach {
                runOnUiThread {
                    val historyView = LayoutInflater.from(this).inflate(R.layout.history_row, null, false)
                    historyView.findViewById<TextView>(R.id.expressionTextView).text = it.expression
                    historyView.findViewById<TextView>(R.id.resultTextView).text = "= ${it.result}"

                    historyLinearLayout.addView(historyView)
                }
            }
        }).start()
    }

    fun closeHistoryClicked(view: View) {
        historyLayout.isVisible = false
    }

    fun historyClearButton(view: View) {
        // DB 기록 삭제
        // View에서도 삭제
        historyLinearLayout.removeAllViews()

        Thread(Runnable {
            db.historyDao().deleteAll()
        }).start()
   }
}

fun String.isNumber(): Boolean {
    return try {
        this.toBigInteger()
        return true
    } catch (e: NumberFormatException) {
        return false
    }
}