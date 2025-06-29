package com.example.uasnmp

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.uasnmp.databinding.FragmentProfileBinding
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Locale

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private var userId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        userId = sharedPref.getInt("user_id", 0)

        if (userId != 0) {
            loadProfileData()
        } else {
            Toast.makeText(requireContext(), "User belum login", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadProfileData() {
        val url = "http://10.0.2.2/uasnmp/profile.php?user_id=$userId"

        val request = StringRequest(url, { response ->
            try {
                val json = JSONObject(response)
                if (json.getBoolean("success")) {
                    val data = json.getJSONObject("data")

                    val nama = data.getString("nama")
                    val email = data.getString("email")
                    val tanggalJoin = data.getString("tanggal_join")
                    val totalFav = data.getInt("total_fav")

                    // Format tanggal: 2003-12-18 ➜ 18 December 2003
                    val formattedDate = try {
                        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH)
                        val date = inputFormat.parse(tanggalJoin)
                        outputFormat.format(date!!)
                    } catch (e: Exception) {
                        "-"
                    }

                    binding.tvName.text = "Nama: $nama"
                    binding.tvEmail.text = "Email: $email"
                    binding.tvJoinDate.text = "Joined Since: $formattedDate"
                    binding.tvTotalFavourites.text = "Total Favourites: $totalFav"
                } else {
                    Toast.makeText(requireContext(), "Gagal memuat profil", Toast.LENGTH_SHORT)
                        .show()
                }
            } catch (e: Exception) {
                Log.e("PROFILE", "Parsing error", e)
                Toast.makeText(requireContext(), "Parsing error", Toast.LENGTH_SHORT).show()
            }
        }, {
            Log.e("PROFILE", "Volley error", it)
            Toast.makeText(requireContext(), "Gagal koneksi", Toast.LENGTH_SHORT).show()
        })

        Volley.newRequestQueue(requireContext()).add(request)
    }
}
