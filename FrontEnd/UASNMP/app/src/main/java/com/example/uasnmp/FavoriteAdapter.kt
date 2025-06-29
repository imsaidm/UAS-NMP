package com.example.uasnmp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.uasnmp.databinding.ItemFavoriteCardBinding

class FavoriteAdapter(
    private val favorites: List<Healing>,
    private val onItemClick: (Healing) -> Unit
) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    inner class FavoriteViewHolder(val binding: ItemFavoriteCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(healing: Healing) {
            binding.tvName.text = healing.name
            binding.tvCategory.text = healing.category
            binding.root.setOnClickListener { onItemClick(healing) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemFavoriteCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FavoriteViewHolder(binding)
    }

    override fun getItemCount(): Int = favorites.size

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(favorites[position])
    }
}
