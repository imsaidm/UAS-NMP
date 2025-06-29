package com.example.uasnmp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.uasnmp.databinding.ActivityNewLocationBinding
import org.json.JSONObject

class NewLocationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewLocationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Spinner kategori
        val categories = listOf(
            "Cafe", "Resto", "Warkop", "Hotel", "Karaoke",
            "Arcade", "Playground", "Billiard", "Bowling", "Bar"
        )
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            categories
        )
        binding.spinnerCategory.adapter = adapter

        binding.btnSave.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val imageUrl = binding.etImageUrl.text.toString().trim()
            val shortDesc = binding.etShortDesc.text.toString().trim()
            val longDesc = binding.etLongDesc.text.toString().trim()
            val category = binding.spinnerCategory.selectedItem.toString().trim()

            if (name.isEmpty() || imageUrl.isEmpty() || shortDesc.isEmpty() || longDesc.isEmpty()) {
                Toast.makeText(this, "Semua field wajib diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            saveHealingLocation(name, imageUrl, shortDesc, longDesc, category)
        }
    }

    private fun saveHealingLocation(
        name: String,
        imageUrl: String,
        shortDesc: String,
        longDesc: String,
        category: String
    ) {
        val url = "http://10.0.2.2/uasnmp/add_location.php"

        val request = object : StringRequest(
            Method.POST, url,
            { response ->
                Log.d("DEBUG_RESPONSE", response)
                try {
                    val json = JSONObject(response)
                    val message = json.getString("message")
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

                    if (json.getBoolean("status")) {
                        // Jika berhasil, kembali ke HomeActivity dan buka Explore
                        val intent = Intent(this, HomeActivity::class.java)
                        intent.putExtra("navigateTo", "explore")
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }
                } catch (e: Exception) {
                    Log.e("JSON_ERROR", "Parsing JSON gagal", e)
                    Toast.makeText(this, "Respons server tidak valid", Toast.LENGTH_LONG).show()
                }
            },
            { error ->
                Log.e("VOLLEY_ERROR", "Koneksi gagal", error)
                Toast.makeText(this, "Gagal koneksi: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                return hashMapOf(
                    "name" to name,
                    "image_url" to imageUrl,
                    "short_desc" to shortDesc,
                    "long_desc" to longDesc,
                    "category" to category
                )
            }

            override fun getHeaders(): MutableMap<String, String> {
                return hashMapOf("Content-Type" to "application/x-www-form-urlencoded")
            }
        }

        Volley.newRequestQueue(this).add(request)
    }
}
