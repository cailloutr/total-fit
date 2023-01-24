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

class ExercisesAdapter :
    ListAdapter<Exercicio, ExercisesAdapter.ExercicioViewHolder>(DiffCallback) {

    inner class ExercicioViewHolder(
        private val binding: ExercicioItemBinding
    ) : ViewHolder(binding.root) {
        fun bind(exercicio: Exercicio) {
            binding.exercicioItemImage.load(exercicio.imageUrl) {
                placeholder(R.drawable.ic_exercise)
                error(R.drawable.ic_exercise)
                crossfade(true)
            }
            binding.exercicioItemName.text = exercicio.nome
            binding.exercicioItemDescription.text = exercicio.observacoes
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
        holder.bind(getItem(position))
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