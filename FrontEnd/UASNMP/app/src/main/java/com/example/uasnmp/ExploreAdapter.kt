package com.example.uasnmp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class ExploreAdapter(
    private val list: List<Healing>,
    private val onReadMoreClick: (Healing) -> Unit
) : RecyclerView.Adapter<ExploreAdapter.ExploreViewHolder>() {

    inner class ExploreViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgHealing: ImageView = view.findViewById(R.id.imgHealing)
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvCategory: TextView = view.findViewById(R.id.tvCategory)
        val tvShortDesc: TextView = view.findViewById(R.id.tvShortDesc)
        val btnReadMore: Button = view.findViewById(R.id.btnReadMore)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExploreViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_healing, parent, false)
        return ExploreViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExploreViewHolder, position: Int) {
        val item = list[position]
        holder.tvName.text = item.name
        holder.tvCategory.text = item.category
        holder.tvShortDesc.text = item.shortDesc
        Picasso.get()
            .load(item.imageUrl)
            .placeholder(R.drawable.placeholder_image) // Opsional: tambahkan gambar ini di drawable
            .error(R.drawable.placeholder_image) // Opsional: tambahkan gambar ini di drawable
            .into(holder.imgHealing, object : Callback {
                override fun onSuccess() {}
                override fun onError(e: Exception?) {
                    e?.printStackTrace()
                }
            })

        holder.btnReadMore.setOnClickListener {
            onReadMoreClick(item)
        }
    }

    override fun getItemCount(): Int = list.size
}
