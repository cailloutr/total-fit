package com.example.totalfit.ui.treinos

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.totalfit.databinding.FragmentTreinosBinding
import com.example.totalfit.model.Treino
import com.example.totalfit.ui.BaseFragment
import com.example.totalfit.ui.viewmodel.TreinosViewModel
import com.example.totalfit.ui.viewmodel.UiStateViewModel
import com.example.totalfit.ui.viewmodel.VisualComponents
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.activityViewModel

//private const val TAG = "TreinosFragment"

class TreinosFragment : BaseFragment() {

    private var _binding: FragmentTreinosBinding? = null
    val binding get() = _binding!!

    private val uiStateViewModel: UiStateViewModel by activityViewModel()
    private val treinosViewModel: TreinosViewModel by inject()

    private lateinit var adapter: TreinosAdapter

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

        setupAdapter()
    }

    private fun setupAdapter() {
        adapter = TreinosAdapter(
            requireContext()
        ) {
            it.id?.let { id ->
                navigateToNewTreinoFragment(id)
            }
        }

        binding.fragmentTreinosRecyclerView.adapter = adapter

        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(binding.fragmentTreinosRecyclerView)

        treinosViewModel.listOfTreinos.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        binding.fragmentTreinosFab.setOnClickListener {
            navigateToNewTreinoFragment(null)
        }
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
                            deletedItem.id?.let { treinosViewModel.remove(it) }
                        }
                        undoAction = false
                    }

                    override fun onShown(sb: Snackbar?) {
                        super.onShown(sb)
                    }
                })
            snackBar.show()
        }

        private fun deleteItem(position: Int) {
            val currentList = adapter.currentList.toMutableList()
            currentList.removeAt(position)
            adapter.submitList(currentList)
        }

        private fun undoDeleteItem(position: Int, deletedItem: Treino) {
            val currentList = adapter.currentList.toMutableList()
            currentList.add(position, deletedItem)
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