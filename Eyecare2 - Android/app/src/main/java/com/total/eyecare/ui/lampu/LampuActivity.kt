package com.total.eyecare.ui.lampu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import com.total.eyecare.R

class lampuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lampu)
        val backButton = findViewById<ImageButton>(R.id.back_lampu)
        backButton.setOnClickListener{
            finish()
        }
    }
    override fun onBackPressed() {
        finish()
    }
}