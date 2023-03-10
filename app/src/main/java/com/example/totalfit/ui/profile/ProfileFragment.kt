package com.example.totalfit.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.totalfit.databinding.FragmentProfileBinding
import com.example.totalfit.ui.BaseFragment
import com.example.totalfit.ui.viewmodel.ProfileViewModel
import com.example.totalfit.ui.viewmodel.UiStateViewModel
import com.example.totalfit.ui.viewmodel.VisualComponents
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class ProfileFragment : BaseFragment() {

    private var _binding: FragmentProfileBinding? = null
    val binding get() = _binding!!

    private val uiStateViewModel: UiStateViewModel by activityViewModel()
    private val profileViewModel: ProfileViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUiComponents()

        profileViewModel.getUser().observe(viewLifecycleOwner) {
            binding.userEmail.text = it.email
        }
    }

    private fun setupUiComponents() {
        uiStateViewModel.hasComponents = VisualComponents(
            appBar = true,
            bottomNavigation = true,
            logoutMenu = true
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}