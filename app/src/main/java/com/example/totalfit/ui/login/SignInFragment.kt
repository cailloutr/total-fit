package com.example.totalfit.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.totalfit.R
import com.example.totalfit.databinding.FragmentSignInBinding
import com.example.totalfit.extension.snackbar
import com.example.totalfit.model.User
import com.example.totalfit.repository.Resource
import com.example.totalfit.ui.viewmodel.SignInViewModel
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignInFragment() : Fragment() {

    private val viewModel: SignInViewModel by viewModel()
    var _binding: FragmentSignInBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragmentLoginSignInButton.setOnClickListener {
            singInNewUser()
        }
    }

    private fun singInNewUser() {
        resetAllFormErrors()

        val email = binding.fragmentLoginEmailEdittext.editText?.text.toString().trim()
        val password = binding.fragmentLoginPasswordEdittext.editText?.text.toString().trim()
        val confirmPassword = binding.fragmentLoginConfirmPasswordEdittext.editText?.text.toString().trim()

        if (validateFields(email, password, confirmPassword)) {
            val user = User(email, password)
            viewModel.signIn(user).observe(viewLifecycleOwner) { resource ->
                if (resource.data) {
                    findNavController().popBackStack()
                    binding.root.snackbar(getString(R.string.snack_bar_message_sign_in_success))
                } else {
                    showSigningInError(resource, resource.error)
                }
            }
        }

    }

    private fun showSigningInError(
        resource: Resource<Boolean>,
        error: Exception?
    ) {
        val message: String? = when (resource.error) {
            is FirebaseAuthWeakPasswordException -> {
                binding.fragmentLoginPasswordEdittext.error =
                    getString(R.string.firebase_expection_weak_password)
                error?.localizedMessage
            }
            is FirebaseAuthInvalidCredentialsException -> {
                error?.localizedMessage
            }
            is FirebaseAuthUserCollisionException -> {
                binding.fragmentLoginEmailEdittext.error =
                    getString(R.string.firebase_exception_email)
                error?.localizedMessage
            }
            is IllegalArgumentException -> {
                getString(R.string.sign_in_exception_empty_fields)
            }
            else -> {
                getString(R.string.firebase_exception_unknown)
            }
        }
        binding.root.snackbar(message.toString())
    }

    private fun validateFields(
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        var isValid = true

        if (email.isEmpty()) {
            binding.fragmentLoginEmailEdittext.error = getString(R.string.empty_field_exception)
            isValid = false
        }

        if (password.isEmpty()) {
            binding.fragmentLoginPasswordEdittext.error = getString(R.string.empty_field_exception)
            isValid = false
        } else if (confirmPassword.isEmpty() || confirmPassword != password) {
            binding.fragmentLoginConfirmPasswordEdittext.error =
                getString(R.string.diferent_passwords_error)
            isValid = false
        }
        return isValid
    }

    private fun resetAllFormErrors() {
        binding.fragmentLoginEmailEdittext.error = null
        binding.fragmentLoginPasswordEdittext.error = null
        binding.fragmentLoginConfirmPasswordEdittext.error = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}