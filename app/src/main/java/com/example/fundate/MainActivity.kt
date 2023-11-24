package com.example.fundate

import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.fundate.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

// Written by Arunabha with ❤

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private var actionBarDrawerToggle: ActionBarDrawerToggle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionBarDrawerToggle = ActionBarDrawerToggle(
            this@MainActivity,
            binding.drawerLayout,
            R.string.open,
            R.string.close
        )
        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle!!)
        actionBarDrawerToggle!!.syncState()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        binding.navigationView.setNavigationItemSelectedListener(this)

        val navController = findNavController(R.id.fragment)
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favourite -> {
                Toast.makeText(this, "Favourite!", Toast.LENGTH_SHORT).show()
            }

            R.id.rateUs -> {
                Toast.makeText(this, "Rate us!", Toast.LENGTH_SHORT).show()
            }

            R.id.shareApp -> {
                Toast.makeText(this, "Share Fun Date!", Toast.LENGTH_SHORT).show()
            }

            R.id.termsAndCond -> {
                Toast.makeText(this, "Terms and conditions!", Toast.LENGTH_SHORT).show()
            }

            R.id.privacyPolicy -> {
                Toast.makeText(this, "Privacy policy!", Toast.LENGTH_SHORT).show()
            }

            R.id.developers -> {
                Toast.makeText(this, "Developers!", Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarDrawerToggle!!.onOptionsItemSelected(item)) {
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    @Suppress("DEPRECATION")
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.close()
        }
        else {
            super.onBackPressed()
        }
    }
}