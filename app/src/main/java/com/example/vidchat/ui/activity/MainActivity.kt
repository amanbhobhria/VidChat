package com.example.vidchat.ui.activity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.vidchat.R
import com.example.vidchat.ui.activity.ui.LoginActivity

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferences



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)
        if (isLoggedIn) {
            navigateToLoginSignupActivity()
        }



    }

    private fun navigateToLoginSignupActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}