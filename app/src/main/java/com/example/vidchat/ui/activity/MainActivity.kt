package com.example.vidchat.ui.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.vidchat.R
import com.example.vidchat.databinding.ActivityLoginBinding
import com.example.vidchat.databinding.ActivityMainBinding
import com.example.vidchat.ui.activity.ui.LoginActivity

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferences
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        sharedPref = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)

        val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)
        if (!isLoggedIn) {
            navigateToLoginSignupActivity()
        }

        val username=sharedPref.getString("username", "User")


        Toast.makeText(this, "Welcome $username", Toast.LENGTH_SHORT).show()
        binding.usernameTV.setText(username)
        binding.logoutBtn.setOnClickListener{logout()}
   }

    private fun navigateToLoginSignupActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }


    private fun logout() {
        // Clear login state in SharedPreferences
        val editor = sharedPref.edit()
        editor.clear() // Clears all stored values
        editor.apply()

        // Navigate back to LoginActivity
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
    }







}