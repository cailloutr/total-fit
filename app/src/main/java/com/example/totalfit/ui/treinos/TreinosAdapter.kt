package com.example.totalfit.ui.treinos

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.totalfit.R
import com.example.totalfit.databinding.TreinoItemBinding
import com.example.totalfit.model.Treino
import com.example.totalfit.util.SPELLED_DAYS

class TreinosAdapter(
    private val context: Context,
    private val onClickListener: (Treino) -> Unit
) : ListAdapter<Treino, TreinosAdapter.TreinoViewHolder>(diffCallback) {

    inner class TreinoViewHolder(
        private val binding: TreinoItemBinding
    ) : ViewHolder(binding.root) {
        fun bind(treino: Treino) {
            binding.treinoItemNome.text = context.getString(R.string.treino_item_name, treino.nome)
            binding.treinoItemDescricao.text = context.getString(R.string.treino_item_description, treino.descricao)
            binding.treinoItemData.text = context.getString(
                R.string.treino_item_data,
                treino.getFormattedDate(SPELLED_DAYS)
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TreinoViewHolder {
        return TreinoViewHolder(
            TreinoItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TreinoViewHolder, position: Int) {
        val treino = getItem(position)
        holder.bind(treino)

        holder.itemView.setOnClickListener {
            onClickListener(treino)
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Treino>() {
            override fun areItemsTheSame(oldItem: Treino, newItem: Treino): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Treino, newItem: Treino): Boolean {
                return oldItem == newItem
            }

        }
    }
}