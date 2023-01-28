package com.example.totalfit.ui.treinos

import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.contains
import androidx.core.util.forEach
import androidx.core.util.remove
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.totalfit.databinding.ExercicioInTreinoItemBinding
import com.example.totalfit.model.Exercicio
import com.example.totalfit.model.Treino

private const val TAG = "Adapter"

class AddExercicioInTreinoAdapter(
    private val listener: (List<String>) -> Unit
) :
    ListAdapter<Exercicio, AddExercicioInTreinoAdapter.ExercicioListViewHolder>(diffCallback) {

    var treino: Treino? = null
    var itemStateArray = SparseBooleanArray()

    inner class ExercicioListViewHolder(
        val binding: ExercicioInTreinoItemBinding
    ) : ViewHolder(binding.root) {
        fun bind(exercicio: Exercicio, isSelected: Boolean) {
            binding.apply {
                exercicioItemName.text = exercicio.nome
                exercicioItemDescription.text = exercicio.observacoes

                if (isSelected) {
                    binding.exercicioItemButtonAdd.visibility = View.GONE
                    binding.exercicioItemButtonRemove.visibility = View.VISIBLE
                } else {
                    binding.exercicioItemButtonAdd.visibility = View.VISIBLE
                    binding.exercicioItemButtonRemove.visibility = View.GONE
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExercicioListViewHolder {
        return ExercicioListViewHolder(
            ExercicioInTreinoItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ExercicioListViewHolder, position: Int) {
        holder.bind(getItem(position), itemStateArray[holder.adapterPosition, false])

        holder.binding.exercicioItemButtonAdd.setOnClickListener {
            if (!itemStateArray.contains(holder.adapterPosition)) {
                itemStateArray.put(holder.adapterPosition, true)
                holder.binding.exercicioItemButtonAdd.visibility = View.GONE
                holder.binding.exercicioItemButtonRemove.visibility = View.VISIBLE
            }

            Log.i(TAG, "$itemStateArray")

            listener(returnSelectedItems())
        }

        holder.binding.exercicioItemButtonRemove.setOnClickListener {
            if (itemStateArray.contains(holder.adapterPosition)) {
                itemStateArray.remove(holder.adapterPosition, true)
                holder.binding.exercicioItemButtonAdd.visibility = View.VISIBLE
                holder.binding.exercicioItemButtonRemove.visibility = View.GONE
            }

            Log.i(TAG, "$itemStateArray")
            listener(returnSelectedItems())
        }
    }

    fun returnSelectedItems(): List<String>{
        val listOfItem = mutableListOf<String>()
        itemStateArray.forEach { key, _ ->
            listOfItem.add(currentList[key].id.toString())
        }
        return listOfItem
    }

    fun listOfIdsToSparseBooleanArray(list: List<String>): SparseBooleanArray{
        val sparseBooleanArray = SparseBooleanArray()
        for (treino in currentList) {
            for (id in list) {
                if (id == treino.id) {
                    sparseBooleanArray.put(currentList.indexOf(treino), true)
                }
            }
        }
        return sparseBooleanArray
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Exercicio>() {
            override fun areItemsTheSame(oldItem: Exercicio, newItem: Exercicio): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Exercicio, newItem: Exercicio): Boolean {
                return oldItem == newItem
            }
        }
    }

}


