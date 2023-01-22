package com.example.totalfit.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.totalfit.R
import com.example.totalfit.databinding.FragmentLoginBinding
import com.example.totalfit.extension.snackbar
import com.example.totalfit.model.User
import com.example.totalfit.repository.Resource
import com.example.totalfit.ui.viewmodel.LoginViewModel
import com.example.totalfit.ui.viewmodel.UiStateViewModel
import com.example.totalfit.ui.viewmodel.VisualComponents
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModel()
    private val uiStateViewModel: UiStateViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uiStateViewModel.hasComponents = VisualComponents()

        binding.fragmentLoginLoginButton.setOnClickListener {
            loginUser()
        }

        binding.fragmentLoginSignInButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signInFragment)
        }
    }

    private fun loginUser() {
        resetAllFormErrors()

        val email = binding.fragmentLoginEmailEdittext.editText?.text.toString()
        val password = binding.fragmentLoginPasswordEdittext.editText?.text.toString()

        if (validateFields(email, password)) {
            val user = User(email, password)
            viewModel.login(user).observe(viewLifecycleOwner) { resource ->
                if (resource.data) {
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment2)
                } else {
                    showLoginError(resource, resource.error)
                }
            }
        }
    }

    private fun showLoginError(
        resource: Resource<Boolean>,
        error: Exception?
    ) {
        val message: String? = when (resource.error) {
            is FirebaseAuthInvalidUserException -> {
                binding.fragmentLoginEmailEdittext.error =
                    getString(R.string.firebase_exception_invalid_credential)
                error?.localizedMessage
            }
            is FirebaseAuthInvalidCredentialsException -> {
                binding.fragmentLoginPasswordEdittext.error =
                    getString(R.string.firebase_exception_invalid_password)
                error?.localizedMessage
            }
            is IllegalArgumentException -> {
                binding.fragmentLoginPasswordEdittext.error =
                    getString(R.string.empty_field_exception)
                getString(R.string.empty_field_exception)
            }
            else -> {
                getString(R.string.firebase_exception_unknown)
            }
        }
        binding.root.snackbar(message.toString())
    }

    private fun resetAllFormErrors() {
        binding.fragmentLoginEmailEdittext.error = null
        binding.fragmentLoginPasswordEdittext.error = null
    }

    private fun validateFields(email: String, password: String): Boolean {
        var isValid = true
        if (email.isEmpty()) {
            binding.fragmentLoginEmailEdittext.error = getString(R.string.empty_field_exception)
            isValid = false

        }

        if (password.isEmpty()) {
            binding.fragmentLoginPasswordEdittext.error = getString(R.string.empty_field_exception)
            isValid = false
        }
        return isValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}