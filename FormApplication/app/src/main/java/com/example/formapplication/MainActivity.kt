package com.example.formapplication

import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.formapplication.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Biến để lưu trữ background mặc định của EditText
    private var defaultNameBackground: Drawable? = null
    private var defaultEmailBackground: Drawable? = null
    private var defaultDobBackground: Drawable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Lưu lại background mặc định
        defaultNameBackground = binding.etName.background
        defaultEmailBackground = binding.etEmail.background
        defaultDobBackground = binding.etDob.background

        // Cài đặt xử lý
        setupCalendar()
        setupRegistration()
    }

    // --- YÊU CẦU 2: XỬ LÝ LỊCH VÀ NGÀY SINH ---
    private fun setupCalendar() {
        // 1. Xử lý nút "Select" để ẩn/hiện CalendarView
        binding.btnSelectDate.setOnClickListener {
            if (binding.calendarView.visibility == View.VISIBLE) {
                binding.calendarView.visibility = View.GONE
            } else {
                binding.calendarView.visibility = View.VISIBLE
            }
        }

        // 2. Xử lý khi người dùng chọn ngày trên CalendarView
        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // month bắt đầu từ 0 (0 = tháng 1), nên cần +1
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, dayOfMonth)

            // Định dạng ngày thành "dd/MM/yyyy"
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formattedDate = sdf.format(selectedDate.time)

            // 3. Cập nhật vào EditText và ẩn lịch đi
            binding.etDob.setText(formattedDate)
            binding.calendarView.visibility = View.GONE
        }
    }

    // --- YÊU CẦU 3: XỬ LÝ NÚT REGISTER VÀ VALIDATION ---
    private fun setupRegistration() {
        binding.btnRegister.setOnClickListener {
            if (validateInputs()) {
                // Nếu tất cả hợp lệ
                Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show()
                // (Bạn có thể thêm logic gửi dữ liệu ở đây)
            } else {
                // Nếu có lỗi
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateInputs(): Boolean {
        // 1. Reset tất cả màu nền về mặc định trước khi kiểm tra
        resetErrorStyles()

        val name = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val dob = binding.etDob.text.toString().trim()

        var isValid = true
        val errorColor = Color.parseColor("#FFCDD2") // Màu đỏ nhạt

        // 2. Kiểm tra từng trường
        if (name.isEmpty()) {
            binding.etName.setBackgroundColor(errorColor)
            isValid = false
        }

        if (email.isEmpty()) {
            binding.etEmail.setBackgroundColor(errorColor)
            isValid = false
        }

        if (dob.isEmpty()) {
            binding.etDob.setBackgroundColor(errorColor)
            isValid = false
        }

        return isValid
    }

    // Hàm này rất quan trọng, để xóa màu đỏ khi người dùng nhập lại
    private fun resetErrorStyles() {
        binding.etName.background = defaultNameBackground
        binding.etEmail.background = defaultEmailBackground
        binding.etDob.background = defaultDobBackground
    }
}