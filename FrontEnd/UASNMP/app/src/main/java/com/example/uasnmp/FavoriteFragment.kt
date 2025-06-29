package com.example.uasnmp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.uasnmp.databinding.FragmentFavoriteBinding
import org.json.JSONObject

class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var adapter: FavoriteAdapter
    private val favoriteList = mutableListOf<Healing>()

    private var userId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        userId = sharedPref.getInt("user_id", 0) // ✅ Cocok sama waktu login


        if (userId == -1) {
            Toast.makeText(
                requireContext(),
                "User tidak ditemukan, silakan login ulang",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        adapter = FavoriteAdapter(favoriteList) { healing ->
            val intent = Intent(requireContext(), HealingDetailActivity::class.java)
            intent.putExtra("healing", healing)
            intent.putExtra("from_favorite", true)
            startActivity(intent)
        }



        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        loadFavoriteLocations()
    }

    override fun onResume() {
        super.onResume()
        loadFavoriteLocations()
    }


    private fun loadFavoriteLocations() {
        val url = "http://10.0.2.2/uasnmp/favorite.php?action=list&user_id=$userId"

        val request = StringRequest(
            Request.Method.GET, url,
            { response ->
                try {
                    val json = JSONObject(response)
                    if (json.getBoolean("success")) {
                        val dataArray = json.getJSONArray("data")
                        favoriteList.clear()

                        for (i in 0 until dataArray.length()) {
                            val obj = dataArray.getJSONObject(i)
                            val healing = Healing(
                                id = obj.getInt("id"),
                                name = obj.getString("name"),
                                imageUrl = obj.getString("image_url"),
                                shortDesc = obj.getString("short_desc"),
                                longDesc = obj.getString("long_desc"),
                                category = obj.getString("category")
                            )
                            favoriteList.add(healing)
                        }

                        adapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(requireContext(), "Data favorit kosong", Toast.LENGTH_SHORT)
                            .show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Parsing error", Toast.LENGTH_SHORT).show()
                    Log.e("FAVORITE_LOAD", "Parsing error", e)
                }
            },
            { error ->
                Toast.makeText(
                    requireContext(),
                    "Gagal koneksi: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("FAVORITE_LOAD", "Volley error", error)
            }
        )

        Volley.newRequestQueue(requireContext()).add(request)
    }
}
