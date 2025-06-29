package com.example.uasnmp

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.uasnmp.databinding.ActivityHealingDetailBinding
import com.squareup.picasso.Picasso
import org.json.JSONObject

class HealingDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHealingDetailBinding
    private var isFavorited = false
    private var userId: Int = 0
    private var fromFavoritePage = false // <-- Tambahan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHealingDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedPref = getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        userId = sharedPref.getInt("user_id", 0)
        fromFavoritePage = intent.getBooleanExtra("from_favorite", false)

        val healing = intent.getParcelableExtra<Healing>("healing")
        if (healing != null) {
            binding.tvName.text = healing.name
            binding.tvCategory.text = healing.category
            binding.tvLongDesc.text = healing.longDesc

            Picasso.get()
                .load(healing.imageUrl)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.placeholder_image)
                .into(binding.imgHealing)

            checkFavorite(healing.id)

            binding.btnAddToFav.setOnClickListener {
                toggleFavorite(healing.id, healing.name)
            }
        } else {
            Toast.makeText(this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun checkFavorite(healingId: Int) {
        val url =
            "http://10.0.2.2/uasnmp/favorite.php?action=check&user_id=$userId&healing_id=$healingId"

        val request = StringRequest(url, { response ->
            val json = JSONObject(response)
            isFavorited = json.optBoolean("favorited", false)
            updateFavoriteButton()
        }, {
            Toast.makeText(this, "Gagal cek favorit", Toast.LENGTH_SHORT).show()
        })

        Volley.newRequestQueue(this).add(request)
    }

    private fun toggleFavorite(healingId: Int, healingName: String) {
        val action = if (isFavorited) "remove" else "add"
        val url =
            "http://10.0.2.2/uasnmp/favorite.php?action=$action&user_id=$userId&healing_id=$healingId"

        val request = StringRequest(url, { response ->
            val json = JSONObject(response)
            if (json.optBoolean("success", false)) {
                isFavorited = !isFavorited
                updateFavoriteButton()

                val message = if (isFavorited)
                    "$healingName ditambahkan ke favorit"
                else
                    "$healingName dihapus dari favorit"

                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                if (!isFavorited && fromFavoritePage) {
                    finish()
                }
            }
        }, {
            Toast.makeText(this, "Gagal ubah status favorit", Toast.LENGTH_SHORT).show()
        })

        Volley.newRequestQueue(this).add(request)
    }

    private fun updateFavoriteButton() {
        binding.btnAddToFav.text = if (isFavorited) "Remove from Favorite" else "Add to Favorite"
    }
}
