package com.example.uasnmp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.uasnmp.databinding.ActivityLoginBinding
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email dan password wajib diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            loginUser(email, password)
        }
    }

    private fun loginUser(email: String, password: String) {
        val url = "http://10.0.2.2/uasnmp/login.php"

        val request = object : StringRequest(
            Method.POST, url,
            { response ->
                val json = JSONObject(response)
                if (json.getBoolean("status")) {
                    Toast.makeText(this, "Login berhasil", Toast.LENGTH_SHORT).show()

                    val user = json.getJSONObject("data")
                    val userId = user.getInt("id")

                    val sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE)
                    sharedPref.edit()
                        .putString("email", email)
                        .putInt("user_id", userId) // ⬅️ simpan ID
                        .apply()

                    startActivity(Intent(this, HomeActivity::class.java))
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
                return hashMapOf("email" to email, "password" to password)
            }
        }

        Volley.newRequestQueue(this).add(request)
    }
}
