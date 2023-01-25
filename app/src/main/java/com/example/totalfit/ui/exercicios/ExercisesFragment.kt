package com.example.totalfit.ui.exercicios

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.example.totalfit.databinding.FragmentExercisesBinding
import com.example.totalfit.enums.OperationState
import com.example.totalfit.enums.OperationState.FINISHED
import com.example.totalfit.enums.OperationState.LOADING
import com.example.totalfit.model.Exercicio
import com.example.totalfit.ui.BaseFragment
import com.example.totalfit.ui.viewmodel.ExerciciosViewModel
import com.example.totalfit.ui.viewmodel.UiStateViewModel
import com.example.totalfit.ui.viewmodel.VisualComponents
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ExercisesFragment : BaseFragment() {

    private var _binding: FragmentExercisesBinding? = null
    val binding get() = _binding!!
    private lateinit var adapter: ExercisesAdapter

    private val uiStateViewModel: UiStateViewModel by activityViewModel()
    private val exerciciosViewModel: ExerciciosViewModel by viewModel()

    private val _state: MutableLiveData<OperationState> = MutableLiveData<OperationState>()
    val state: LiveData<OperationState> = _state

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExercisesBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUiState()
        setupAdapter()

        binding.floatActionButton.setOnClickListener {
            findNavController().navigate(
                ExercisesFragmentDirections.actionExercisesFragmentToNewExercicioFragment(null)
            )
        }
    }

    private fun setupAdapter() {
        adapter = ExercisesAdapter {
            it.id?.let { id ->
                findNavController().navigate(
                    ExercisesFragmentDirections.actionExercisesFragmentToNewExercicioFragment(id)
                )
            }
        }
        binding.fragmentExercisesRecyclerView.adapter = adapter
        exerciciosViewModel.listOfItems.observe(viewLifecycleOwner) {
            defineOperationStateValue(it)

            state.observe(viewLifecycleOwner) { operationState ->
                showProgressBar(operationState)
            }

            adapter.submitList(it)
        }
    }

    private fun defineOperationStateValue(list: List<Exercicio>) {
        when (list.size) {
            0 -> {
                _state.value = LOADING
            }
            else -> {
                _state.value = FINISHED
            }
        }
    }

    private fun showProgressBar(operationState: OperationState) {
        when(operationState) {
            LOADING -> {
                binding.fragmentExercisesRecyclerView.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
            }
            FINISHED -> {
                binding.fragmentExercisesRecyclerView.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun setupUiState() {
        uiStateViewModel.hasComponents = VisualComponents(
            appBar = true,
            bottomNavigation = true
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}