package com.laurentdarl.confidentialnotesapplication.presentation.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.laurentdarl.confidentialnotesapplication.R
import com.laurentdarl.confidentialnotesapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.notesFragment,
            R.id.favoriteFragment2,
            R.id.profileFragment
        ))
        val navHost = supportFragmentManager.findFragmentById(R.id.notes_container) as NavHostFragment
        val navController = navHost.navController
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.bottomNav.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id) {
                R.id.signInFragment -> {
                  hideStatusBar()
                  hideBottomNav()
                }
                R.id.signUpFragment -> {
                    hideStatusBar()
                    hideBottomNav()
                }
                else -> {
                    viewVisible()
                }
            }
        }
    }

    private fun viewVisible() {
        binding.bottomNav.visibility = View.VISIBLE
        binding.toolbar.visibility = View.VISIBLE
    }

    private fun hideBottomNav() {
        binding.bottomNav.visibility = View.GONE
    }

    private fun hideStatusBar() {
        binding.toolbar.visibility = View.GONE
        supportActionBar?.hide()
    }

    override fun onSupportNavigateUp() = Navigation.findNavController(
        this, R.id.notes_container).navigateUp()
}