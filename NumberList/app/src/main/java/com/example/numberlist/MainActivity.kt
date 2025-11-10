package com.example.numberlist

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {

    private lateinit var editTextNumber: EditText
    private lateinit var radioGroupType: RadioGroup
    private lateinit var listViewNumbers: ListView
    private lateinit var textViewMessage: TextView
    private lateinit var adapter: ArrayAdapter<Int>
    private val numberList = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Khởi tạo views
        editTextNumber = findViewById(R.id.editTextNumber)
        radioGroupType = findViewById(R.id.radioGroupType)
        listViewNumbers = findViewById(R.id.listViewNumbers)
        textViewMessage = findViewById(R.id.textViewMessage)

        // Khởi tạo adapter
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, numberList)
        listViewNumbers.adapter = adapter

        // Cập nhật danh sách ban đầu
        updateNumberList()

        // Lắng nghe sự thay đổi số nhập vào
        editTextNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateNumberList()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Lắng nghe sự thay đổi loại số
        radioGroupType.setOnCheckedChangeListener { _, _ ->
            updateNumberList()
        }
    }

    // Hàm cập nhật danh sách số
    private fun updateNumberList() {
        numberList.clear()

        val input = editTextNumber.text.toString().trim()
        if (input.isEmpty()) {
            showMessage()
            adapter.notifyDataSetChanged()
            return
        }

        val maxNumber = input.toIntOrNull()
        if (maxNumber == null || maxNumber <= 0) {
            showMessage()
            adapter.notifyDataSetChanged()
            return
        }

        when (radioGroupType.checkedRadioButtonId) {
            R.id.radioEven -> {
                // Số lẻ
                var i = 1
                while (i < maxNumber) {
                    numberList.add(i)
                    i += 2
                }
            }
            R.id.radioPrime -> {
                // Số nguyên tố
                for (i in 2 until maxNumber) {
                    if (isPrime(i)) numberList.add(i)
                }
            }
            R.id.radioPerfect -> {
                // Số hoàn hảo
                for (i in 1 until maxNumber) {
                    if (isPerfect(i)) numberList.add(i)
                }
            }
            R.id.radioOdd -> {
                // Số chẵn
                var i = 2
                while (i < maxNumber) {
                    numberList.add(i)
                    i += 2
                }
            }
            R.id.radioSquare -> {
                // Số chính phương
                var i = 0
                while (i * i < maxNumber) {
                    numberList.add(i * i)
                    i++
                }
            }
            R.id.radioFibonacci -> {
                // Số Fibonacci
                getFibonacci(maxNumber)
            }
        }

        Log.d("NumberList", "Max: $maxNumber, Type: ${radioGroupType.checkedRadioButtonId}, Count: ${numberList.size}")

        // Hiển thị kết quả
        if (numberList.isEmpty()) {
            showMessage()
        } else {
            hideMessage()
        }

        adapter.notifyDataSetChanged()
    }

    // Kiểm tra số nguyên tố
    private fun isPrime(num: Int): Boolean {
        if (num < 2) return false
        if (num == 2) return true
        if (num % 2 == 0) return false

        var i = 3
        while (i * i <= num) {
            if (num % i == 0) return false
            i += 2
        }
        return true
    }

    // Kiểm tra số hoàn hảo
    private fun isPerfect(num: Int): Boolean {
        if (num < 2) return false
        var sum = 1
        var i = 2
        while (i * i <= num) {
            if (num % i == 0) {
                sum += i
                if (i != num / i) {
                    sum += num / i
                }
            }
            i++
        }
        return sum == num
    }

    // Tạo danh sách số Fibonacci
    private fun getFibonacci(max: Int) {
        var a = 0
        var b = 1

        if (a < max) numberList.add(a)
        if (b < max) numberList.add(b)

        while (true) {
            val next = a + b
            if (next >= max) break
            numberList.add(next)
            a = b
            b = next
        }
    }

    // Hiển thị thông báo
    private fun showMessage() {
        textViewMessage.visibility = View.VISIBLE
        listViewNumbers.visibility = View.GONE
    }

    // Ẩn thông báo
    private fun hideMessage() {
        textViewMessage.visibility = View.GONE
        listViewNumbers.visibility = View.VISIBLE
    }
}