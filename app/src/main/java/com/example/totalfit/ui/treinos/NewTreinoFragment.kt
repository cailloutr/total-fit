package com.example.totalfit.ui.treinos

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.totalfit.R
import com.example.totalfit.databinding.FragmentNewTreinoBinding
import com.example.totalfit.extension.snackbar
import com.example.totalfit.model.Treino
import com.example.totalfit.ui.viewmodel.NewTreinoViewModel
import com.example.totalfit.ui.viewmodel.TreinosViewModel
import com.example.totalfit.ui.viewmodel.UiStateViewModel
import com.example.totalfit.ui.viewmodel.VisualComponents
import com.example.totalfit.util.CALENDAR_NUMBER
import com.example.totalfit.util.MyMaskEditText
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.Timestamp
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "NewTreinoFragment"

class NewTreinoFragment : Fragment() {

    private var _binding: FragmentNewTreinoBinding? = null
    val binding get() = _binding!!

    private val uiStateViewModel: UiStateViewModel by activityViewModel()
    private val newTreinoViewModel: NewTreinoViewModel by inject()
    private val treinoViewModel: TreinosViewModel by inject()

    private val args: NewTreinoFragmentArgs by navArgs()

    private val treinoId: String? by lazy {
        args.id
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentNewTreinoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uiStateViewModel.hasComponents = VisualComponents(appBar = true)

        loadTreinoDataIntoTheView()
        setupDateEditText()
        setupAppBarMenu()
    }

    private fun loadTreinoDataIntoTheView() {
        if (treinoId != null) {
            treinoViewModel.getById(treinoId!!).observe(viewLifecycleOwner) { treino ->
                setupAdapter(treino)
                binding.apply {
                    newTreinoInputLayoutName.editText?.setText(treino.nome)
                    newTreinoInputLayoutDescription.editText?.setText(treino.descricao)
                    newTreinoInputLayoutDate.editText?.setText(
                        treino.getFormattedDate(
                            CALENDAR_NUMBER
                        )
                    )
                }

                newTreinoViewModel.listOfExercicioNoTreino = treino.exercicios
            }
        } else {
            setupAdapter()
        }
    }

    private fun setupAppBarMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.save_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.itemId == R.id.menu_save) {
                    saveNewTreino()
                }
                return true
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun saveNewTreino() {
        val name = binding.newTreinoInputLayoutName.editText?.text.toString()
        val description = binding.newTreinoInputLayoutDescription.editText?.text.toString()
        val date = newTreinoViewModel.dateOfTreino
        val exerciciosDoTreino = newTreinoViewModel.listOfExercicioNoTreino

        val treino = Treino(
            id = treinoId,
            nome = name,
            descricao = description,
            data = date,
            exercicios = exerciciosDoTreino
        )

        newTreinoViewModel.save(treino).observe(viewLifecycleOwner) { repositoryState ->
            repositoryState?.let {
                if (it) {
                    binding.root.snackbar("Treino salvo")
                    findNavController().popBackStack()
                    return@observe
                }
                binding.root.snackbar("Erro ao salvar treino")
            }
        }
    }

    private fun setupAdapter(treino: Treino? = null) {
        val adapter = AddExercicioInTreinoAdapter {
            newTreinoViewModel.listOfExercicioNoTreino = it
            Log.i(TAG, "setupAdapter: ${newTreinoViewModel.listOfExercicioNoTreino}")
        }

        binding.fragmentNewTreinoRecyclerView.adapter = adapter
        newTreinoViewModel.listExercicios.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            adapter.treino = treino
            treino?.exercicios?.let { list ->
                adapter.itemStateArray = adapter.listOfIdsToSparseBooleanArray(list)
            }
        }


    }

    private fun setupDateEditText() {
        binding.newTreinoInputLayoutDate.editText?.addTextChangedListener(
            MyMaskEditText.insert(
                "##/##/####", binding.newTreinoInputLayoutDate.editText!!
            )
        )

        binding.newTreinoInputLayoutDate.setEndIconOnClickListener {


            // Makes only dates from today forward selectable.
            val constraintsBuilder =
                CalendarConstraints.Builder()
                    .setValidator(DateValidatorPointForward.now())

//            val calendar = Calendar.getInstance(Locale("pt", "br"))

            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText(getString(R.string.date_picker_label))
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .setCalendarConstraints(constraintsBuilder.build())
                    .build()
            datePicker.addOnPositiveButtonClickListener {
                val date = Date(it)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "br"))

                newTreinoViewModel.dateOfTreino = Timestamp(date)
                binding.newTreinoInputLayoutDate.editText?.setText(
                    dateFormat.format(date)
                )
            }
            datePicker.show(parentFragmentManager, "MATERIAL_DATE_PICKER")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}