package com.example.totalfit.ui.exercicios

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.example.totalfit.R
import com.example.totalfit.databinding.ExercicioItemBinding
import com.example.totalfit.model.Exercicio

//private const val TAG = "ExercisesAdapter"

class ExercisesAdapter(
    private val onClickListener: (Exercicio) -> Unit
) :
    ListAdapter<Exercicio, ExercisesAdapter.ExercicioViewHolder>(DiffCallback) {

    inner class ExercicioViewHolder(
        private val binding: ExercicioItemBinding
    ) : ViewHolder(binding.root) {
        fun bind(exercicio: Exercicio) {
            binding.exercicioItemName.text = exercicio.nome
            binding.exercicioItemDescription.text = exercicio.observacoes
            binding.exercicioItemImage.load(exercicio.imageUrl) {
                placeholder(R.drawable.ic_exercise)
                fallback(R.drawable.ic_exercise)
                error(R.drawable.ic_exercise)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExercicioViewHolder {
        return ExercicioViewHolder(
            ExercicioItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ExercicioViewHolder, position: Int) {
        val exercicio = getItem(position)
        holder.bind(exercicio)

        holder.itemView.setOnClickListener {
            onClickListener(exercicio)
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Exercicio>() {
            override fun areItemsTheSame(oldItem: Exercicio, newItem: Exercicio): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Exercicio, newItem: Exercicio): Boolean {
                return oldItem == newItem
            }
        }
    }
}