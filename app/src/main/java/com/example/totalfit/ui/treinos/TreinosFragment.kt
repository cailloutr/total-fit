package com.example.totalfit.ui.treinos

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.totalfit.databinding.FragmentTreinosBinding
import com.example.totalfit.model.Treino
import com.example.totalfit.model.TreinoDocument
import com.example.totalfit.ui.BaseFragment
import com.example.totalfit.ui.viewmodel.NewExerciseViewModel
import com.example.totalfit.ui.viewmodel.UiStateViewModel
import com.example.totalfit.ui.viewmodel.VisualComponents
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.activityViewModel

private const val TAG = "TreinosFragment"

class TreinosFragment : BaseFragment() {

    private var _binding: FragmentTreinosBinding? = null
    val binding get() = _binding!!

    private val uiStateViewModel: UiStateViewModel by activityViewModel()
    private val newExerciseViewModel: NewExerciseViewModel by inject()

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
        uiStateViewModel.hasComponents = VisualComponents(appBar = true, bottomNavigation = true)

//        getById("RPrM08jZ8gFXVI2lFJM4").observe(viewLifecycleOwner) {
//            Log.i(TAG, "Observe: $it")
//        }
    }

    private fun getById(id: String): LiveData<List<String>> = MutableLiveData<List<String>>().apply{
        val firestore = Firebase.firestore
        firestore.collection("treino").document(id)
            .addSnapshotListener { s, _ ->
                s?.let { document ->
                    document.toObject<TreinoDocument>()?.toTreino(document.id)
                        ?.let { treino ->
                            Log.i(TAG, "Firestore: ${treino.exercicios}")
                            value = treino.exercicios
                        }
                }
            }
    }


    private fun getAllTreino() {
        val firestore = Firebase.firestore
        firestore.collection("treino")
            .addSnapshotListener { snapShot, _ ->
                snapShot?.let {
                    val treinoList: List<Treino> = snapShot.documents.mapNotNull {
                        it.toObject<TreinoDocument>()?.toTreino(it.id)
                    }
                    Log.i(TAG, "onViewCreated: $treinoList")
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}