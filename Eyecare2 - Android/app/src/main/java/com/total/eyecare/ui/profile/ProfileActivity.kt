package com.total.eyecare.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import com.total.eyecare.R

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val backButton = findViewById<ImageButton>(R.id.back_profile)
        backButton.setOnClickListener{
            finish()
        }
    }
    override fun onBackPressed() {
        finish()
    }
}