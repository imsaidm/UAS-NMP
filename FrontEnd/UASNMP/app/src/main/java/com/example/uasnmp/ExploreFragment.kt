package com.example.uasnmp

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
import com.example.uasnmp.databinding.FragmentExploreBinding
import org.json.JSONObject

class ExploreFragment : Fragment() {

    private lateinit var binding: FragmentExploreBinding
    private val healingList = mutableListOf<Healing>()
    private lateinit var adapter: ExploreAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExploreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        binding.fabAdd.setOnClickListener {
            val intent = Intent(requireContext(), NewLocationActivity::class.java)
            startActivity(intent)
        }

        loadHealingLocations()
    }

    override fun onResume() {
        super.onResume()
        loadHealingLocations()
    }

    private fun setupRecyclerView() {
        adapter = ExploreAdapter(healingList) { healing ->
            val intent = Intent(requireContext(), HealingDetailActivity::class.java)
            intent.putExtra("healing", healing)
            startActivity(intent)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    private fun loadHealingLocations() {
        val url = "http://10.0.2.2/uasnmp/get_all_locations.php"

        val request = StringRequest(
            Request.Method.GET, url,
            { response ->
                try {
                    val json = JSONObject(response)
                    if (json.getBoolean("status")) {
                        val dataArray = json.getJSONArray("data")
                        healingList.clear()

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
                            healingList.add(healing)
                        }

                        adapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(requireContext(), "Gagal ambil data", Toast.LENGTH_SHORT)
                            .show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Parsing error", Toast.LENGTH_SHORT).show()
                    Log.e("EXPLORE_LOAD", "Parsing error", e)
                }
            },
            { error ->
                Toast.makeText(
                    requireContext(),
                    "Volley error: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("EXPLORE_LOAD", "Volley error", error)
            }
        )

        Volley.newRequestQueue(requireContext()).add(request)
    }
}
