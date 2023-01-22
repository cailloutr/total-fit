package com.example.totalfit.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.totalfit.R
import com.example.totalfit.databinding.ActivityMainBinding
import com.example.totalfit.ui.viewmodel.LoginViewModel
import com.example.totalfit.ui.viewmodel.UiStateViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val uiStateViewModel: UiStateViewModel by viewModel()
    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupAppBarMenu()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            title = destination.label
            uiStateViewModel.components.observe(this) {
                it?.let { hasComponents ->
                    if (hasComponents.appBar) {
                        supportActionBar?.show()
                    } else {
                        supportActionBar?.hide()
                    }
                    if (hasComponents.bottomNavigation) {
                        binding.bottomNavigation.visibility = View.VISIBLE
                    } else {
                        binding.bottomNavigation.visibility = View.GONE
                    }
                }
            }
        }

//        setupActionBarWithNavController(navController, appBarConfiguration)
        NavigationUI.setupWithNavController(binding.bottomNavigation,navController)
    }

    private fun setupAppBarMenu() {
        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.logged_in_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.itemId == R.id.menu_item_logou) {
                    loginViewModel.logout()
                    navigateToLogin()
                }
                return true
            }

        }, this, Lifecycle.State.RESUMED)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun navigateToLogin() {
        navController.navigate(R.id.action_global_login)
    }
}