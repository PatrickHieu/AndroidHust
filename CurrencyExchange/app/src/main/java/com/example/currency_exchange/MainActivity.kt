package com.example.currency_exchange

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    // 1. Tỷ giá cố định (Yêu cầu: ít nhất 10 loại tiền tệ)
    // Chúng ta sẽ dùng USD làm đơn vị cơ sở (base currency)
    private val rates = mapOf(
        "USD" to 1.0,            // Đô la Mỹ
        "VND" to 25450.0,        // Việt Nam Đồng
        "EUR" to 0.92,           // Euro
        "JPY" to 157.5,          // Yên Nhật
        "GBP" to 0.79,           // Bảng Anh
        "AUD" to 1.50,           // Đô la Úc
        "CAD" to 1.37,           // Đô la Canada
        "CHF" to 0.89,           // Franc Thụy Sĩ
        "CNY" to 7.25,           // Nhân dân tệ
        "KRW" to 1380.0,         // Won Hàn Quốc
        "SGD" to 1.35            // Đô la Singapore
    )

    // Danh sách các mã tiền tệ
    private val currencyCodes = rates.keys.toList()

    // Định dạng số cho đẹp (vd: 2 số sau dấu phẩy)
    private val decimalFormat = DecimalFormat("#,##0.##")

    // View components
    private lateinit var spinnerFrom: Spinner
    private lateinit var spinnerTo: Spinner
    private lateinit var editTextFrom: EditText
    private lateinit var editTextTo: EditText

    // 5. Biến cờ để ngăn vòng lặp vô hạn
    // Khi chúng ta cập nhật EditText B bằng code, nó sẽ kích hoạt TextWatcher của B.
    // TextWatcher của B lại cập nhật EditText A, kích hoạt TextWatcher của A.
    // Hai biến cờ này dùng để ngắt vòng lặp đó.
    private var isUpdatingFrom = false
    private var isUpdatingTo = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 2. Ánh xạ View từ layout XML
        spinnerFrom = findViewById(R.id.spinnerFrom)
        spinnerTo = findViewById(R.id.spinnerTo)
        editTextFrom = findViewById(R.id.editTextFrom)
        editTextTo = findViewById(R.id.editTextTo)

        // 3. Setup Spinners (Yêu cầu: Sử dụng Spinner)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencyCodes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerFrom.adapter = adapter
        spinnerTo.adapter = adapter

        // Set giá trị mặc định cho Spinner
        spinnerFrom.setSelection(currencyCodes.indexOf("VND"))
        spinnerTo.setSelection(currencyCodes.indexOf("USD"))

        // 6. Thêm Listener cho các View
        setupListeners()
    }

    // 4. Hàm tính toán chuyển đổi
    private fun convertCurrency(amount: Double, fromCurrency: String, toCurrency: String): Double {
        val rateFrom = rates[fromCurrency] ?: 1.0
        val rateTo = rates[toCurrency] ?: 1.0

        // Công thức:
        // 1. Chuyển số tiền `amount` từ `fromCurrency` về USD (base currency)
        // 2. Chuyển số tiền USD vừa có sang `toCurrency`
        val amountInUSD = amount / rateFrom
        return amountInUSD * rateTo
    }

    private fun setupListeners() {
        // Listener cho Spinner
        val spinnerListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Khi thay đổi Spinner, tự động cập nhật lại kết quả
                // Luôn lấy giá trị từ ô "From" để tính toán lại
                updateConversion(isUpdatingFromEditText = true)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spinnerFrom.onItemSelectedListener = spinnerListener
        spinnerTo.onItemSelectedListener = spinnerListener

        // Listener cho EditText "From" (Yêu cầu: tự động chuyển đổi khi thay đổi)
        editTextFrom.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                // Nếu `isUpdatingTo` là true, nghĩa là text này đang được cập nhật
                // bởi code từ `editTextTo`, thì ta không làm gì cả (tránh lặp)
                if (isUpdatingTo) return

                updateConversion(isUpdatingFromEditText = true)
            }
        })

        // Listener cho EditText "To" (Yêu cầu: thay đổi EditText này thì cái còn lại cập nhật)
        editTextTo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                // Tương tự, nếu `isUpdatingFrom` là true, thì bỏ qua
                if (isUpdatingFrom) return

                updateConversion(isUpdatingFromEditText = false)
            }
        })
    }

    /**
     * Hàm trung tâm xử lý việc cập nhật
     * @param isUpdatingFromEditText:
     * true = người dùng gõ vào ô "From", ta tính ra ô "To"
     * false = người dùng gõ vào ô "To", ta tính ngược lại ra ô "From"
     */
    private fun updateConversion(isUpdatingFromEditText: Boolean) {
        val fromCurrency = spinnerFrom.selectedItem.toString()
        val toCurrency = spinnerTo.selectedItem.toString()

        if (isUpdatingFromEditText) {
            // Đặt cờ `isUpdatingFrom` = true để báo cho TextWatcher của `editTextTo`
            // biết là "tôi đang cập nhật, đừng làm gì cả"
            isUpdatingFrom = true

            val amountFrom = editTextFrom.text.toString().toDoubleOrNull() ?: 0.0
            if (amountFrom == 0.0) {
                editTextTo.setText("") // Nếu nhập 0 hoặc rỗng thì xóa ô kia
            } else {
                val convertedAmount = convertCurrency(amountFrom, fromCurrency, toCurrency)
                editTextTo.setText(decimalFormat.format(convertedAmount))
            }

            // Xong việc, gỡ cờ
            isUpdatingFrom = false

        } else { // Tức là isUpdatingFromEditText == false
            // Người dùng gõ vào ô "To", tính ngược lại
            isUpdatingTo = true

            val amountTo = editTextTo.text.toString().toDoubleOrNull() ?: 0.0
            if (amountTo == 0.0) {
                editTextFrom.setText("")
            } else {
                // Tính ngược: chuyển `amountTo` từ `toCurrency` về `fromCurrency`
                val convertedAmount = convertCurrency(amountTo, toCurrency, fromCurrency)
                editTextFrom.setText(decimalFormat.format(convertedAmount))
            }

            isUpdatingTo = false
        }
    }

    // Helper function để parse số, xử lý dấu phẩy
    private fun String.toDoubleOrNull(): Double? {
        // Loại bỏ các dấu phẩy (dùng cho DecimalFormat)
        val cleanString = this.replace(",", "")
        return cleanString.toDoubleOrNull()
    }
}