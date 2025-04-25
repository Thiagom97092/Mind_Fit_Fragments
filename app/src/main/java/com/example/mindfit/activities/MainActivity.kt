package com.example.mindfit.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.mindfit.R
import com.example.mindfit.databinding.ActivityMainBinding
import com.example.mindfit.fragments.HistorialFragment
import com.example.mindfit.fragments.QRFragment
import com.example.mindfit.fragments.ReservasFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(ReservasFragment())

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_reservas -> replaceFragment(ReservasFragment())
                R.id.nav_qr -> replaceFragment(QRFragment())
                R.id.nav_historial -> replaceFragment(HistorialFragment())
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}