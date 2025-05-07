package com.example.reportflow.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.reportflow.response.CommonResponse
import com.example.reportflow.response.RetrofitInstance
import com.example.reportflow.response.RetrofitInstance.TYPE
import com.example.reportflow.databinding.ActivityRegistrationBinding
import com.example.reportflow.utils.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrationActivity : AppCompatActivity() {
    private val bind by lazy { ActivityRegistrationBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bind.root)

        bind.Login.setOnClickListener {
            finish()
        }

        bind.addData.setOnClickListener {
            val name = bind.editTextName.text.toString().trim()
            val mobile = bind.editTextMoblie.text.toString().trim()
            val password = bind.editTextPassword.text.toString().trim()
            val email = bind.editTextEmail.text.toString().trim()
            val location = bind.editTextAdress.text.toString().trim()

            when {
                name.isEmpty() -> showToast("Please enter your name")

                mobile.isEmpty() || mobile.length < 10 -> showToast("Please enter a valid 10-digit mobile number")

                password.isEmpty() || password.length < 6 -> showToast("Password must be at least 6 characters long")

                email.isEmpty() || !email.contains("@gmail.com") -> showToast("Please enter a valid email")

                location.isEmpty() -> showToast("Please enter your address")

                else -> {

                    bind.progressBar5.isVisible = true
                    CoroutineScope(IO).launch {

                        RetrofitInstance.instance.entryRegister(
                            name = name, email, password, mobile, TYPE, "User", location
                        ).enqueue(object : Callback<CommonResponse?> {
                            override fun onResponse(
                                call: Call<CommonResponse?>,
                                response: Response<CommonResponse?>,
                            ) {
                                val registerResponse = response.body()!!
                                if (!registerResponse.error) {
                                    finish()
                                    runOnUiThread {
                                        showToast("Registration Successful")
                                    }
                                } else {
                                    showToast(
                                        response.body()?.message
                                            ?: "Registration failed"
                                    )
                                }
                                bind.progressBar5.isVisible = false
                            }

                            override fun onFailure(
                                call: Call<CommonResponse?>,
                                t: Throwable,
                            ) {
                                showToast("Error: ${t.message}")
                                bind.progressBar5.isVisible = false
                            }
                        }
                        )
                    }


                }
            }
        }

    }
}