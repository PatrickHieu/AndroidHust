package com.example.gmailui

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        val emails = arrayListOf(
            EmailModel("Edurila.com", "$19 Only (First 10 spots)", "Are you looking to Learn Web Designing...", "12:34 PM", Color.parseColor("#4285F4")), // Blue
            EmailModel("Chris Abad", "Help make Campaign Monitor better", "Let us know your thoughts! No Images...", "11:22 AM", Color.parseColor("#DB4437")), // Red
            EmailModel("Tuto.com", "8h de formation gratuite et les nouv...", "Photoshop, SEO, Blender, CSS, WordPre...", "11:04 AM", Color.parseColor("#0F9D58")), // Green
            EmailModel("support", "Société Ovh : suivi de vos services - hp...", "SAS OVH - http://www.ovh.com 2 rue K...", "10:26 AM", Color.parseColor("#607D8B")), // Grey
            EmailModel("Matt from Ionic", "The New Ionic Creator Is Here!", "Announcing the all-new Creator, build...", "10:00 AM", Color.parseColor("#F4B400")), // Yellow
            EmailModel("Hà nội", "Thông báo lịch học", "Các bạn chú ý lịch học tuần sau...", "9:00 AM", Color.parseColor("#9C27B0")),
            EmailModel("GitHub", "Your password was changed", "Hello, We wanted to let you know...", "8:30 AM", Color.BLACK)
        )

        val adapter = EmailAdapter(emails)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}