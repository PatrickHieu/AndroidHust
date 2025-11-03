package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast // Thêm import Toast
import com.example.calculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Biến lưu trạng thái
    private var operand1: Int? = null
    private var currentOperator: String? = null
    private var isNewInput = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        // Gắn sự kiện cho các nút số
        val numberButtons = listOf(
            binding.btn0, binding.btn1, binding.btn2, binding.btn3, binding.btn4,
            binding.btn5, binding.btn6, binding.btn7, binding.btn8, binding.btn9
        )
        numberButtons.forEach { button ->
            button.setOnClickListener { onNumberClick(it) }
        }

        // Gắn sự kiện cho các nút phép toán
        val operatorButtons = listOf(
            binding.btnPlus, binding.btnSubtract, binding.btnMultiply, binding.btnDivide
        )
        operatorButtons.forEach { button ->
            button.setOnClickListener { onOperatorClick(it) }
        }

        // Gắn sự kiện cho các nút đặc biệt
        // *** LƯU Ý: ĐÃ CẬP NHẬT binding.btnC và binding.btnCE ***
        binding.btnCE.setOnClickListener { onClearEntryClick() } // CE
        binding.btnC.setOnClickListener { onClearClick() }       // C
        binding.btnBS.setOnClickListener { onBackspaceClick() }
        binding.btnEquals.setOnClickListener { onEqualsClick() }

        // Các nút mới - chưa thực hiện logic vì đề bài là SỐ NGUYÊN
        binding.btnPlusMinus.setOnClickListener {
            Toast.makeText(this, "Chức năng này chưa được cài đặt", Toast.LENGTH_SHORT).show()
        }
        binding.btnDot.setOnClickListener {
            Toast.makeText(this, "Chỉ tính toán số nguyên", Toast.LENGTH_SHORT).show()
        }
    }

    // Xử lý khi nhấn nút số
    private fun onNumberClick(view: View) {
        val button = view as Button
        val number = button.text.toString()
        val currentText = binding.tvResult.text.toString()

        // Giới hạn độ dài để tránh tràn màn hình
        if (currentText.length >= 9 && !isNewInput) {
            return
        }

        if (isNewInput || currentText == "0") {
            binding.tvResult.text = number
            isNewInput = false
        } else {
            binding.tvResult.text = currentText + number
        }
    }

    // Xử lý khi nhấn nút phép toán (+, -, *, /)
    private fun onOperatorClick(view: View) {
        val button = view as Button
        // Lấy text của nút, nhưng nút 'x' cần chuyển thành '*'
        val operator = if (button.text.toString() == "x") "*" else button.text.toString()


        // Lấy toán hạng hiện tại trên màn hình
        val currentOperand = binding.tvResult.text.toString().toIntOrNull() ?: 0

        if (operand1 == null) {
            // Đây là lần nhấn phép toán đầu tiên
            operand1 = currentOperand
        } else if (!isNewInput) { // Chỉ tính khi vừa nhập xong số thứ 2
            // Đây là phép toán nối tiếp (ví dụ: 5 + 3 *)
            try {
                val result = performCalculation(operand1!!, currentOperand, currentOperator!!)
                binding.tvResult.text = result.toString()
                operand1 = result
            } catch (e: Exception) {
                binding.tvResult.text = "Error"
                onClearClick() // Reset nếu có lỗi (ví dụ: chia cho 0)
                return
            }
        }

        // Luôn lưu phép toán mới và đánh dấu chờ nhập số mới
        currentOperator = operator
        isNewInput = true
    }

    // Xử lý khi nhấn nút =
    private fun onEqualsClick() {
        // Cần phải có cả toán hạng 1 và phép toán
        if (operand1 == null || currentOperator == null || isNewInput) {
            // Nếu đang chờ nhập số mới (ví dụ: 5 + =) thì không làm gì
            return
        }

        val operand2 = binding.tvResult.text.toString().toIntOrNull() ?: 0
        try {
            val result = performCalculation(operand1!!, operand2, currentOperator!!)
            binding.tvResult.text = result.toString()
        } catch (e: Exception) {
            binding.tvResult.text = "Error"
        }

        // Reset toàn bộ phép toán sau khi nhấn =
        resetOperation()
    }

    // Xử lý nút C (Clear)
    private fun onClearClick() {
        binding.tvResult.text = "0"
        resetOperation()
    }

    // Xử lý nút CE (Clear Entry)
    private fun onClearEntryClick() {
        // Chỉ xóa toán hạng hiện tại về 0
        binding.tvResult.text = "0"
        isNewInput = true // Sẵn sàng nhập số mới cho toán hạng này
    }

    // Xử lý nút BS (Backspace)
    private fun onBackspaceClick() {
        // Chỉ xóa khi đang nhập số (không xóa kết quả)
        if (!isNewInput) {
            val currentText = binding.tvResult.text.toString()
            if (currentText.length > 1) {
                binding.tvResult.text = currentText.substring(0, currentText.length - 1)
            } else {
                // Nếu chỉ còn 1 chữ số, set về 0
                binding.tvResult.text = "0"
                isNewInput = true
            }
        }
    }

    // Hàm thực hiện phép tính
    private fun performCalculation(op1: Int, op2: Int, operator: String): Int {
        return when (operator) {
            "+" -> op1 + op2
            "-" -> op1 - op2
            "*" -> op1 * op2 // Đã đổi từ 'x' thành '*'
            "/" -> {
                if (op2 == 0) {
                    throw ArithmeticException("Division by zero")
                }
                op1 / op2 // Vì là số nguyên nên đây là phép chia lấy phần nguyên
            }
            else -> throw IllegalArgumentException("Invalid operator")
        }
    }

    // Hàm reset trạng thái phép toán
    private fun resetOperation() {
        operand1 = null
        currentOperator = null
        isNewInput = true
    }
}