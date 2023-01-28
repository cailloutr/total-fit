package com.example.totalfit.ui.treinos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.totalfit.databinding.FragmentTreinosBinding
import com.example.totalfit.ui.BaseFragment
import com.example.totalfit.ui.viewmodel.TreinosViewModel
import com.example.totalfit.ui.viewmodel.UiStateViewModel
import com.example.totalfit.ui.viewmodel.VisualComponents
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.activityViewModel

//private const val TAG = "TreinosFragment"

class TreinosFragment : BaseFragment() {

    private var _binding: FragmentTreinosBinding? = null
    val binding get() = _binding!!

    private val uiStateViewModel: UiStateViewModel by activityViewModel()
    private val treinosViewModel: TreinosViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentTreinosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uiStateViewModel.hasComponents = VisualComponents(
            appBar = true,
            bottomNavigation = true,
            logoutMenu = true
        )

//        getById("RPrM08jZ8gFXVI2lFJM4").observe(viewLifecycleOwner) {
//            Log.i(TAG, "Observe: $it")
//        }

        val adapter = TreinosAdapter(
            requireContext()
        ) {
            it.id?.let { id ->
                navigateToNewTreinoFragment(id)
            }
        }

        binding.fragmentTreinosRecyclerView.adapter = adapter

        treinosViewModel.listOfTreinos.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        binding.fragmentTreinosFab.setOnClickListener {
            navigateToNewTreinoFragment(null)
        }
    }

    private fun navigateToNewTreinoFragment(id: String?) {
        findNavController().navigate(
            TreinosFragmentDirections.actionTreinosFragmentToNewTreinoFragment(id)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}