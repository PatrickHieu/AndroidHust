package com.example.studentmanagement

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var etHoten: EditText
    private lateinit var etMSSV: EditText
    private lateinit var btnAdd: Button
    private lateinit var btnUpdate: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: StudentAdapter
    private val studentList = mutableListOf<Student>()

    private var selectedPosition: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etHoten = findViewById(R.id.etHoten)
        etMSSV = findViewById(R.id.etMSSV)
        btnAdd = findViewById(R.id.btnAdd)
        btnUpdate = findViewById(R.id.btnUpdate)
        recyclerView = findViewById(R.id.recyclerView)

        studentList.add(Student("Nguyễn Văn A", "20200001"))
        studentList.add(Student("Trần Thị B", "20200002"))
        studentList.add(Student("Lê Văn C", "20200003"))

        adapter = StudentAdapter(studentList,
            onEditClick = { position -> showStudentInfo(position) },
            onDeleteClick = { position -> deleteStudent(position) }
        )
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        btnAdd.setOnClickListener {
            val name = etHoten.text.toString()
            val id = etMSSV.text.toString()

            if (name.isNotEmpty() && id.isNotEmpty()) {
                studentList.add(Student(name, id))
                adapter.notifyItemInserted(studentList.size - 1)
                clearInput()
            } else {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show()
            }
        }

        btnUpdate.setOnClickListener {
            if (selectedPosition != -1) {
                val name = etHoten.text.toString()
                val id = etMSSV.text.toString()

                if (name.isNotEmpty() && id.isNotEmpty()) {
                    studentList[selectedPosition] = Student(name, id)
                    adapter.notifyItemChanged(selectedPosition)

                    clearInput()
                    selectedPosition = -1
                    Toast.makeText(this, "Đã cập nhật", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Vui lòng chọn sinh viên để cập nhật", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showStudentInfo(position: Int) {
        val student = studentList[position]
        etHoten.setText(student.hoten)
        etMSSV.setText(student.mssv)
        selectedPosition = position
    }

    private fun deleteStudent(position: Int) {
        studentList.removeAt(position)
        adapter.notifyItemRemoved(position)
        adapter.notifyItemRangeChanged(position, studentList.size)

        if (selectedPosition == position) {
            clearInput()
            selectedPosition = -1
        }
    }

    private fun clearInput() {
        etHoten.text.clear()
        etMSSV.text.clear()
        etMSSV.requestFocus()
    }
}