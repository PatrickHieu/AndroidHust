package com.example.playstoreui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rvVertical = findViewById<RecyclerView>(R.id.rvVertical)
        val verticalData = listOf(
            AppModel("Mech Assemble: Zombie", "4.8 ★   624 MB", android.R.drawable.ic_menu_agenda),
            AppModel("MU: Hống Hỏa Đạo", "4.8 ★   339 MB", android.R.drawable.ic_menu_compass),
            AppModel("War Inc: Rising", "4.9 ★   231 MB", android.R.drawable.ic_menu_mapmode),
            AppModel("Genshin Impact", "4.5 ★   1.2 GB", android.R.drawable.ic_menu_myplaces)
        )
        rvVertical.adapter = VerticalAdapter(verticalData)
        rvVertical.layoutManager = LinearLayoutManager(this)


        val rvHorizontal = findViewById<RecyclerView>(R.id.rvHorizontal)
        val horizontalData = listOf(
            AppModel("Suno - AI Music", "4.2 ★", android.R.drawable.ic_menu_gallery),
            AppModel("Claude by Anthropic", "4.6 ★", android.R.drawable.ic_menu_view),
            AppModel("DramaBox - Movies", "4.0 ★", android.R.drawable.ic_menu_slideshow),
            AppModel("Piligram", "4.5 ★", android.R.drawable.ic_menu_camera),
            AppModel("ChatGPT", "4.9 ★", android.R.drawable.ic_menu_search)
        )
        rvHorizontal.adapter = HorizontalAdapter(horizontalData)

        rvHorizontal.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }
}