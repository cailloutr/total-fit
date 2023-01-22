package com.example.totalfit.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.totalfit.R
import com.example.totalfit.ui.viewmodel.LoginViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

open class BaseFragment : Fragment() {

    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkIfIsLogged()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
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

        }, viewLifecycleOwner)
    }

    private fun checkIfIsLogged() {
        if (loginViewModel.isNotLogged()) {
            navigateToLogin()
        }
    }

    private fun navigateToLogin() {
        findNavController().navigate(R.id.action_global_login)
    }
}