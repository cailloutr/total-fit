package com.example.totalfit.ui.exercicios

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.totalfit.databinding.FragmentExercisesBinding
import com.example.totalfit.enums.OperationState
import com.example.totalfit.enums.OperationState.FINISHED
import com.example.totalfit.enums.OperationState.LOADING
import com.example.totalfit.model.Exercicio
import com.example.totalfit.ui.BaseFragment
import com.example.totalfit.ui.viewmodel.ExerciciosViewModel
import com.example.totalfit.ui.viewmodel.UiStateViewModel
import com.example.totalfit.ui.viewmodel.VisualComponents
import com.google.android.material.snackbar.Snackbar
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
            navigateToNewExercicioFragment(null)
        }

    }

    private fun navigateToNewExercicioFragment(id: String?) {
        findNavController().navigate(
            ExercisesFragmentDirections.actionExercisesFragmentToNewExercicioFragment(id)
        )
    }

    private fun setupAdapter() {
        adapter = ExercisesAdapter {
            it.id?.let { id ->
                navigateToNewExercicioFragment(id)
            }
        }

        binding.fragmentExercisesRecyclerView.adapter = adapter

        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(binding.fragmentExercisesRecyclerView)

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

    //TODO: Melhorar a questão do status
    private fun showProgressBar(operationState: OperationState) {
        when (operationState) {
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

    // Setup if the fragment has a appbar or a bottom navigation
    private fun setupUiState() {
        uiStateViewModel.hasComponents = VisualComponents(
            appBar = true,
            bottomNavigation = true,
            logoutMenu = true
        )
    }


    // Setup de swipe to delete action
    private val simpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        var undoAction: Boolean = false

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            val deletedItem = adapter.currentList[position]
            deleteItem(position)

            val snackBar = Snackbar.make(
                requireContext(),
                binding.root,
                getString(com.example.totalfit.R.string.exercises_fragment_simple_callback_on_swipe_delete),
                Snackbar.LENGTH_LONG
            )
                .setAction(getString(com.example.totalfit.R.string.exercises_fragment_simple_callback_on_swipe_undo)) {
                    undoDeleteItem(position, deletedItem)
                }.addCallback(object : Snackbar.Callback() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)

                    if (!undoAction) {
                        deletedItem.id?.let { exerciciosViewModel.remove(it) }
                    }
                    undoAction = false
                }
            })
            snackBar.show()
        }

        private fun deleteItem(position: Int) {
            val currentList = adapter.currentList.toMutableList()
            currentList.removeAt(position)
//            adapter.notifyItemRemoved(position)
            adapter.submitList(currentList)
        }

        private fun undoDeleteItem(position: Int, deletedItem: Exercicio) {
            val currentList = adapter.currentList.toMutableList()
            currentList.add(position, deletedItem)
//            adapter.notifyItemInserted(position)
            adapter.submitList(currentList)
            undoAction = true
        }

        // Draws the color red bellow the view on swipe to indicate a delete action
        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

            val DIRECTION_RIGHT = 1
            val DIRECTION_LEFTT = 0

            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE && isCurrentlyActive) {
                val direction = if (dX > 0) DIRECTION_RIGHT else DIRECTION_LEFTT
//                val absoluteDisplacement = abs(dX)

                when (direction) {
                    DIRECTION_RIGHT -> {
                        val view = viewHolder.itemView
                        val bg = ColorDrawable()

                        bg.color = Color.parseColor("#ff726f")
                        bg.setBounds(view.left, view.top, view.right, view.bottom)
                        bg.draw(c)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}