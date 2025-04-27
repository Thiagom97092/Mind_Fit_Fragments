package com.example.mindfit.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.mindfit.R
import com.example.mindfit.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configuración del ViewBinding
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ocultar ActionBar si es necesario
        supportActionBar?.hide()

        setupClickListeners()
    }

    private fun setupClickListeners() {
        // Navegación a Login
        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        // Navegación a Registro
        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        // Opcional: Botón de acceso rápido (para desarrollo)
        binding.btnSkip.setOnClickListener {
            Toast.makeText(this, "Modo desarrollo: Acceso rápido", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun onBackPressed() {
        // Opcional: Personalizar comportamiento al presionar atrás
        finishAffinity() // Cierra todas las actividades y sale de la app
    }
}