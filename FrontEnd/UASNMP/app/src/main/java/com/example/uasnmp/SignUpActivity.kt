package com.example.uasnmp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.uasnmp.databinding.ActivitySignUpBinding
import org.json.JSONObject

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            val nama = binding.etNama.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val repeat = binding.etRepeatPassword.text.toString()

            if (password != repeat) {
                Toast.makeText(this, "Password tidak sama", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            registerUser(nama, email, password)
        }

        binding.tvGoToLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun registerUser(nama: String, email: String, password: String) {
        val url = "http://10.0.2.2/uasnmp/register.php"

        val request = object : StringRequest(
            Method.POST, url,
            { response ->
                val json = JSONObject(response)
                if (json.getBoolean("status")) {
                    Toast.makeText(this, "Berhasil daftar!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, json.getString("message"), Toast.LENGTH_SHORT).show()
                }
            },
            {
                Toast.makeText(this, "Gagal koneksi", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                return hashMapOf(
                    "nama" to nama,
                    "email" to email,
                    "password" to password
                )
            }
        }

        Volley.newRequestQueue(this).add(request)
    }
}
