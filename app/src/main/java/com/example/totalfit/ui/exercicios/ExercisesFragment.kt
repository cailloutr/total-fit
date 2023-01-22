package com.example.totalfit.ui.exercicios

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.totalfit.databinding.FragmentExercisesBinding
import com.example.totalfit.ui.BaseFragment
import com.example.totalfit.ui.viewmodel.UiStateViewModel
import com.example.totalfit.ui.viewmodel.VisualComponents
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class ExercisesFragment : BaseFragment() {

    private var _binding: FragmentExercisesBinding? = null
    val binding get() = _binding!!

    private val uiStateViewModel: UiStateViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExercisesBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uiStateViewModel.hasComponents = VisualComponents(appBar = true, bottomNavigation = true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}