package com.example.totalfit.ui.exercicios

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import coil.load
import com.example.totalfit.R
import com.example.totalfit.databinding.FragmentNewExerciseBinding
import com.example.totalfit.extension.snackbar
import com.example.totalfit.model.Exercicio
import com.example.totalfit.ui.BaseFragment
import com.example.totalfit.ui.dialogs.ImagePickerBottomSheet
import com.example.totalfit.ui.dialogs.LoadImageUrlDialog
import com.example.totalfit.ui.viewmodel.NewExerciseViewModel
import com.example.totalfit.ui.viewmodel.UiStateViewModel
import com.example.totalfit.ui.viewmodel.VisualComponents
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.ByteArrayOutputStream


class NewExerciseFragment : BaseFragment() {

    private var _binding: FragmentNewExerciseBinding? = null
    val binding get() = _binding!!

    private val uiStateViewModel: UiStateViewModel by activityViewModel()
    private val viewModel: NewExerciseViewModel by viewModel()

    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

    private val args: NewExerciseFragmentArgs by navArgs()

    private val exercicioId: String? by lazy {
        args.id
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupPicturesSelector()
    }

    private fun setupPicturesSelector() {
        // Registers a photo picker activity launcher in single-select mode.
        pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                // Callback is invoked after the user selects a media item or closes the
                // photo picker.
                if (uri != null) {
                    Log.d("PhotoPicker", "Selected URI: $uri")
                    viewModel.imageUrl = uri
                    binding.fragmentNewExerciseImageView.load(viewModel.imageUrl) {
                        placeholder(R.drawable.ic_image)
                        error(R.drawable.ic_image)
                        crossfade(true)
                    }
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewExerciseBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uiStateViewModel.hasComponents = VisualComponents()

        exercicioId?.let { id ->
            viewModel.getById(id).observe(viewLifecycleOwner) {
                if (it != null) {
                    binding.content
                        .newExerciseContentTextInputLayoutTitle.editText?.setText(it.nome)

                    binding.content.newExerciseContentTextInputLayoutDescription.editText?.setText(
                        it.observacoes
                    )
                }
            }
        }

        setupToolbar()
        setupNewImageFab()
    }

    private fun setupToolbar() {
        binding.fragmentNewExerciseToolbar.setupWithNavController(findNavController())
        binding.fragmentNewExerciseToolbar.inflateMenu(R.menu.fragment_new_exercise_menu)
        binding.fragmentNewExerciseToolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.fragment_new_exercise_menu_save) {
                save()
            }
            true
        }
    }

    private fun save() {
        val title =
            binding.content.newExerciseContentTextInputLayoutTitle.editText?.text.toString()

        val description =
            binding.content.newExerciseContentTextInputLayoutDescription
                .editText?.text.toString()

        val exercicio = Exercicio(
            id = exercicioId,
            nome = title,
            observacoes = description
        )


        // Get the data from an ImageView as bytes
        val imageView = binding.fragmentNewExerciseImageView
        val bitmap = imageView.drawable?.toBitmap()
        val baos = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val image = baos.toByteArray()

        viewModel.save(exercicio, image).observe(viewLifecycleOwner) { repositoryState ->
            repositoryState?.let {
                if (it) {
                    binding.root.snackbar("Exercício salvo")
                    findNavController().popBackStack()
                    return@observe
                }
                binding.root.snackbar("Erro ao salvar exercício")
            }
        }
    }

    private fun setupNewImageFab() {
        binding.newImageFab.setOnClickListener {
            ImagePickerBottomSheet {
                if (it == R.id.bottom_sheet_menu_option_gallery) {
                    // Launch the photo picker and allow the user to choose only images.
                    pickMedia.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                } else {
                    LoadImageUrlDialog { uri ->
                        viewModel.imageUrl = uri
                        binding.fragmentNewExerciseImageView.load(viewModel.imageUrl)
                    }.show(parentFragmentManager, LoadImageUrlDialog.TAG)
                }
            }.show(
                parentFragmentManager,
                ImagePickerBottomSheet.TAG
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}