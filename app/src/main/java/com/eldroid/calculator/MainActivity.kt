package com.eldroid.calculator

import android.graphics.Color
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random
import android.graphics.drawable.GradientDrawable

class MainActivity : AppCompatActivity() {
    private lateinit var display: TextView
    private var operand1: Double? = null
    private var operand2: Double? = null
    private var operator: String? = null
    private var isNewOperation: Boolean = true
    private lateinit var soundPool: SoundPool
    private var soundId: Int = 0
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        display = findViewById(R.id.display)

        if (savedInstanceState != null) {
            display.text = savedInstanceState.getString("display_text")
            operand1 = savedInstanceState.getDouble("operand1")
            operator = savedInstanceState.getString("operator")
            isNewOperation = savedInstanceState.getBoolean("isNewOperation")
        }

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(6)
            .setAudioAttributes(audioAttributes)
            .build()

        soundId = soundPool.load(this, R.raw.button_click, 1)

        val buttons = listOf(
            R.id.button0, R.id.button1, R.id.button2,
            R.id.button3, R.id.button4, R.id.button5,
            R.id.button6, R.id.button7, R.id.button8,
            R.id.button9
        )

        buttons.forEachIndexed { index, buttonId ->
            findViewById<Button>(buttonId).setOnClickListener {
                playSound()
                changeButtonColorTemporarily(it as Button)
                appendToDisplay(index.toString())
            }
        }

        val operators = mapOf(
            R.id.buttonAdd to "+",
            R.id.buttonSubtract to "-",
            R.id.buttonMultiply to "×",
            R.id.buttonDivide to "÷"
        )

        operators.forEach { (buttonId, op) ->
            findViewById<Button>(buttonId).setOnClickListener {
                playSound()
                changeButtonColorTemporarily(it as Button)
                handleOperatorInput(op)
            }
        }

        findViewById<Button>(R.id.buttonEquals).setOnClickListener {
            playSound()
            changeButtonColorTemporarily(it as Button)
            calculateResult()
        }

        findViewById<Button>(R.id.buttonClear).setOnClickListener {
            playSound()
            changeButtonColorTemporarily(it as Button)
            clearDisplay()
        }
    }

    private fun appendToDisplay(value: String) {
        if (isNewOperation) {
            display.text = value
            isNewOperation = false
        } else {
            display.append(value)
        }
    }

    private fun handleOperatorInput(op: String) {
        // If there's already an operator, calculate the result
        if (operator != null) {
            calculateResult()
        }
        operand1 = display.text.toString().toDoubleOrNull()
        operator = op
        appendToDisplay(" $op ") // Add operator to the display with spaces for readability
        isNewOperation = false
    }

    private fun calculateResult() {
        val inputText = display.text.toString().trim()

        // Split the input text by the operator to extract operands
        val splitInput = inputText.split(" ")
        if (splitInput.size == 3) {
            val operand1 = splitInput[0].toDoubleOrNull()
            val operator = splitInput[1]
            val operand2 = splitInput[2].toDoubleOrNull()

            if (operand1 != null && operand2 != null) {
                val result = when (operator) {
                    "+" -> operand1 + operand2
                    "-" -> operand1 - operand2
                    "×" -> operand1 * operand2
                    "÷" -> if (operand2 != 0.0) operand1 / operand2 else Double.NaN
                    else -> null
                }

                result?.let {
                    // Format the result to 2 decimal places
                    display.text = String.format("%.2f", it)
                }
            }
        }

        // Reset the operation
        operand1 = null
        operand2 = null
        operator = null
        isNewOperation = true
    }


    private fun clearDisplay() {
        display.text = ""
        operand1 = null
        operand2 = null
        operator = null
        isNewOperation = true
    }

    private fun playSound() {
        soundPool.play(soundId, 1f, 1f, 0, 0, 1f)
    }

    private fun changeButtonColorTemporarily(button: Button) {
        // Save the original background
        val originalBackground = button.background

        // Create a new GradientDrawable with the desired color and corner radius
        val randomColor = Color.rgb(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
        val drawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            setColor(randomColor) // Set the random color
            cornerRadius = 10f * resources.displayMetrics.density // 10dp radius
        }

        // Set the button's background to the new drawable
        button.background = drawable

        // Revert back to the original background after 400ms
        handler.postDelayed({
            button.background = originalBackground
        }, 400)
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()
    }
}

