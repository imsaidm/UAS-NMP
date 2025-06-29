package com.example.uasnmp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.uasnmp.databinding.ActivityChangePasswordBinding
import org.json.JSONObject

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangePasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnChangePassword.setOnClickListener {
            val oldPass = binding.etOldPassword.text.toString().trim()
            val newPass = binding.etNewPassword.text.toString().trim()
            val confirmPass = binding.etConfirmPassword.text.toString().trim()

            val sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE)
            val email = sharedPref.getString("email", null)

            if (email.isNullOrEmpty() || oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                Toast.makeText(this, "Semua field wajib diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newPass != confirmPass) {
                Toast.makeText(this, "Konfirmasi password tidak cocok", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            changePassword(email, oldPass, newPass)
        }
    }

    private fun changePassword(email: String, oldPass: String, newPass: String) {
        val url = "http://10.0.2.2/uasnmp/change_password.php"

        val request = object : StringRequest(
            Method.POST, url,
            { response ->
                try {
                    val json = JSONObject(response)
                    val message = json.optString("message", "Terjadi kesalahan")
                    val status = json.optBoolean("status", false)

                    if (status) {
                        Toast.makeText(this, "Password berhasil diganti", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "Kesalahan saat membaca respons", Toast.LENGTH_SHORT)
                        .show()
                }
            },
            {
                Toast.makeText(this, "Gagal terhubung ke server", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                return hashMapOf(
                    "email" to email,
                    "old_password" to oldPass,
                    "new_password" to newPass
                )
            }

            override fun getHeaders(): MutableMap<String, String> {
                return hashMapOf(
                    "Content-Type" to "application/x-www-form-urlencoded"
                )
            }
        }

        Volley.newRequestQueue(this).add(request)
    }
}
