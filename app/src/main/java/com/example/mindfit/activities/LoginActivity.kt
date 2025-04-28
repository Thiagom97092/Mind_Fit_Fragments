package com.example.mindfit.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mindfit.databinding.ActivityLoginBinding
import com.example.mindfit.data.repository.UserRepository

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userRepository = UserRepository.getInstance(applicationContext)

        setupLoginButton()
        setupRegisterLink()
    }

    private fun setupLoginButton() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            when {
                email.isEmpty() -> showEmailError()
                password.isEmpty() -> showPasswordError()
                !userRepository.userExists(email) -> showUserNotRegistered()
                !userRepository.loginUser(email, password) -> showInvalidCredentials()
                else -> navigateToMainActivity()
            }
        }
    }

    private fun setupRegisterLink() {
        // Cambia tvRegisterLink por el ID correcto de tu layout
        binding.tvRegisterLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun showEmailError() {
        binding.etEmail.error = "Email requerido"
        binding.etEmail.requestFocus()
    }

    private fun showPasswordError() {
        binding.etPassword.error = "Contraseña requerida"
        binding.etPassword.requestFocus()
    }

    private fun showUserNotRegistered() {
        Toast.makeText(
            this,
            "Correo no registrado. Por favor regístrate",

            Toast.LENGTH_LONG
        ).show()
    }

    private fun showInvalidCredentials() {
        Toast.makeText(
            this,
            "Contraseña incorrecta",
            Toast.LENGTH_SHORT
        ).show()
        binding.etPassword.requestFocus()
    }

    private fun navigateToMainActivity() {
        val resulado =
        Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
        finish()
    }
}