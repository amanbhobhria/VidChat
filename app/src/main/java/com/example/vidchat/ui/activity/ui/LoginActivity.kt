package com.example.vidchat.ui.activity.ui

import LoginViewModelFactory
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import com.example.vidchat.R
import com.example.vidchat.Repository.AuthRepository
import com.example.vidchat.databinding.ActivityLoginBinding
import com.example.vidchat.ui.activity.MainActivity
import com.example.vidchat.viewmodel.LoginViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {


    private var isPasswordVisible = false // Toggle state
    private var username="user"


    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(AuthRepository())

    }


    private lateinit var sharedPref: SharedPreferences

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)



        val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)
        if (isLoggedIn) {
            navigateToMainActivity()
        }





        loginViewModel.loginState.observe(this) { state ->
            when (state) {
                is LoginViewModel.LoginState.Loading -> {
                    binding.loading.visibility = View.VISIBLE
                    binding.login.isEnabled = false
                }

                is LoginViewModel.LoginState.Success -> {
                    binding.loading.visibility = View.GONE
                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                    // Navigate to HomeActivity

                    saveLoginState(true, username)

                    navigateToMainActivity()
                }

                is LoginViewModel.LoginState.Error -> {
                    binding.loading.visibility = View.GONE
                    binding.login.isEnabled = true
                    Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
                }

            }
        }


        binding.login.setOnClickListener {
            Log.d("LoginActivity##", "Login1 PRessed")
            val email = binding.username.text.toString().trim()
            val password = binding.password.text.toString().trim()
            username=email
//            if (validateInput(email, password)) {
            loginViewModel.login(email, password)
//            }
        }

        binding.signUpTv.setOnClickListener {
            Log.d("LoginActivity##", "SignupPressed")

            binding.login.text = "SignUP"
            binding.confirmPassword.visibility = View.VISIBLE
            binding.signUpTv.visibility = View.GONE

            binding.login.setOnClickListener {

                Log.d("LoginActivity##", "Login PRessed")

                val email = binding.username.text.toString().trim()
                val password = binding.password.text.toString().trim()
                username=email
                val confirmPassword = binding.confirmPassword.text.toString().trim()
                if (password.equals(confirmPassword)) {
                    loginViewModel.signup(email, password)
                    binding.confirmPassword.visibility = View.GONE
                    navigateToMainActivity()

                    Toast.makeText(
                        this, "Signed IN", Toast.LENGTH_SHORT
                    ).show()

                    saveLoginState(true, email)

                } else {
                    Toast.makeText(
                        this, "Password and confirm password don't match", Toast.LENGTH_SHORT
                    ).show()
                }

            }

        }

        binding.password.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = binding.password.compoundDrawablesRelative[2] // End drawable
                if (drawableEnd != null && event.rawX >= (binding.password.right - drawableEnd.bounds.width())) {
                    togglePasswordVisibility(binding.password)
                    return@setOnTouchListener true
                }
            }
            false
        }


    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun validateInput(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            binding.username.error = "Email is required"
            binding.username.requestFocus()
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.username.error = "Please enter a valid email"
            binding.username.requestFocus()
            return false
        }
        if (password.isEmpty()) {
            binding.password.error = "Password is required"
            binding.password.requestFocus()
            return false
        }
        if (password.length < 6) {
            binding.password.error = "Password must be at least 6 characters"
            binding.password.requestFocus()
            return false
        }
        return true
    }


    private fun saveLoginState(isLoggedIn: Boolean, username: String) {
        val editor = sharedPref.edit()
        editor.putBoolean("isLoggedIn", isLoggedIn)
        editor.putString("username", username)
        editor.apply()
    }


    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
//        super.onBackPressed()

    }

    private fun togglePasswordVisibility(editText: EditText) {
        isPasswordVisible = !isPasswordVisible
        if (isPasswordVisible) {
            // Show password
            editText.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            editText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                0,
                R.drawable.baseline_remove_red_eye_24,
                0
            )
        } else {
            // Hide password
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            editText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                0,
                R.drawable.baseline_remove_red_eye_24,
                0
            )
        }
        editText.setSelection(editText.text.length) // Maintain cursor position
    }


}
