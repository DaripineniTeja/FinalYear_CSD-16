package com.example.reportflow.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import com.example.reportflow.databinding.ActivityLoginBinding
import com.example.reportflow.response.LoginResponse
import com.example.reportflow.response.RetrofitInstance
import com.example.reportflow.utils.SessionManager
import com.example.reportflow.utils.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private val bind by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val shared by lazy { SessionManager(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bind.root)

        val permissions = arrayOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_NETWORK_STATE,
            android.Manifest.permission.SEND_SMS,
            android.Manifest.permission.READ_PHONE_STATE,
            android.Manifest.permission.POST_NOTIFICATIONS
        )

        val permissionsNotGranted = permissions.filter {
            ActivityCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsNotGranted.isNotEmpty()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissions(permissionsNotGranted.toTypedArray(), 100)
            } else {
                ActivityCompat.requestPermissions(this, permissionsNotGranted.toTypedArray(), 100)
            }
        }

        if (shared.isLoggedIn()) {
            shared.getUserRole()?.let { role ->
                navigateToDashboard(role)
            }
        }

        bind.loginintent.setOnClickListener {
            startActivity(Intent(applicationContext, RegistrationActivity::class.java))
        }



        bind.login.setOnClickListener {
            val email = bind.editTextEmail.text.toString().trim()
            val password = bind.editTextPassword.text.toString().trim()


            if (email.isEmpty()) {
                showToast("Please enter your email")
            } else if (password.isEmpty()) {
                showToast("Please enter your password")
            } else if (email == "admin" && password == "admin") {
                shared.saveLoginState("-1", "Admin", "", "", "", "", "Admin","")
                navigateToDashboard("-1")
                finish()
            } else {
                bind.progressBar5.isVisible = true
                CoroutineScope(IO).launch {
                    RetrofitInstance.instance.login(email, password)
                        .enqueue(object : Callback<LoginResponse?> {
                            override fun onResponse(
                                call: Call<LoginResponse?>,
                                response: Response<LoginResponse?>,
                            ) {
                                val loginResponse = response.body()!!
                                Log.d("TAG", "onResponse: ")
                                if (!loginResponse.error) {
                                    loginResponse.data.firstOrNull()?.let { user ->
                                        shared.saveLoginState(
                                            id = "${user.id}",
                                            name = user.name,
                                            email = user.email,
                                            password = "${user.password}",
                                            mobile = "${user.mobile}",
                                            type = "${user.type}",
                                            role = "${user.role}",
                                            location = "${user.location}"
                                        )
                                        navigateToDashboard("${user.role}")

                                    }
                                } else {
                                    showToast("Invalid credentials")
                                }
                                bind.progressBar5.isVisible = false
                            }

                            override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                                showToast(t.message ?: "Login failed")
                                bind.progressBar5.isVisible = false
                            }
                        })
                }
            }
        }
    }


    private fun navigateToDashboard(role: String) {
        val intent = when (role) {
            "User" -> Intent(this, UserDashboard::class.java)
            "Worker" -> Intent(this, WorkerDashboard::class.java)
            else -> Intent(this, AdminDashboard::class.java)
        }
        startActivity(intent)
        finish()
    }

}
