package com.example.mindfit.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mindfit.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        // Aquí iría la lógica de registro con Firebase/API
        Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
        navigateToMain()
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