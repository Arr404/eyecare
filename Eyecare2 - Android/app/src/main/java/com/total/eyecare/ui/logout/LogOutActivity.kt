package com.total.eyecare.ui.logout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.total.eyecare.R
import com.total.eyecare.ui.login.LoginActivity

class LogOutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_out)
        Firebase.auth.signOut()
        val i = Intent(this@LogOutActivity,LoginActivity::class.java)
        startActivity(i)
        finish()
    }
}