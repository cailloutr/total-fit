package com.example.totalfit.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.totalfit.databinding.FragmentNewExerciseBottomSheetImageMenuBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ImagePickerBottomSheet(
    private val action: (id: Int) -> Unit
) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentNewExerciseBottomSheetImageMenuBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewExerciseBottomSheetImageMenuBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bottomSheetMenuOptionGallery.setOnClickListener {
            action(it.id)
        }

        binding.bottomSheetMenuOptionWeb.setOnClickListener {
            action(it.id)
        }
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }
}