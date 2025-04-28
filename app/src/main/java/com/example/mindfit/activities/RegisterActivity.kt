package com.example.mindfit.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mindfit.databinding.ActivityRegisterBinding
import com.example.mindfit.data.repository.UserRepository

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userRepository = UserRepository.getInstance(applicationContext)

        setupUI()
    }

    private fun setupUI() {
        binding.btnRegister.setOnClickListener {
            if (validateInputs()) {
                registerUser()
            }
        }

        binding.tvLoginLink.setOnClickListener {
            navigateToLogin()
        }
    }

    private fun validateInputs(): Boolean {
        with(binding) {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            return when {
                email.isEmpty() -> {
                    etEmail.error = "Email requerido"
                    false
                }
                password.isEmpty() -> {
                    etPassword.error = "Contraseña requerida"
                    false
                }
                password != confirmPassword -> {
                    etConfirmPassword.error = "Las contraseñas no coinciden"
                    false
                }
                else -> true
            }
        }
    }

    private fun registerUser() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        if (userRepository.registerUser(email, password)) {
            Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
            navigateToMain()
        } else {
            Toast.makeText(this, "Error: El usuario ya existe", Toast.LENGTH_LONG).show()
        }
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}