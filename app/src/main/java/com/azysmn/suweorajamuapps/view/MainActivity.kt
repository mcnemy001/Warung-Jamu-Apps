package com.azysmn.suweorajamuapps.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.azysmn.suweorajamuapps.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    // NIM : 10122384
    // Nama : Aziyusman Maulana
    // Kelas : ANDRO-4
    // Tanggal : 03/06/2025

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, TransaksiFragment())
                .commit()
        }
        bottomNav.setOnItemSelectedListener { item ->
            val fragment = when (item.itemId) {
                R.id.menu_transaksi -> TransaksiFragment()
                R.id.menu_barang -> BarangFragment()
                R.id.menu_info -> InfoFragment()
                else -> null
            }
            fragment?.let{
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, it)
                    .commit()
                true
            } ?: false
        }
    }
}