package com.example.reportflow.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.reportflow.databinding.ActivityAddDataBinding
import com.example.reportflow.response.CommonResponse
import com.example.reportflow.response.RetrofitInstance
import com.example.reportflow.response.RetrofitInstance.TYPE
import com.example.reportflow.utils.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddCommonDataActivity : AppCompatActivity() {
    private val bind by lazy { ActivityAddDataBinding.inflate(layoutInflater) }
    private lateinit var pickImageResult: ActivityResultLauncher<Intent>
    private var selectedImagePath: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bind.root)


        val intent = "${intent.getStringExtra("role")}"



        bind.tiName.hint = "Enter ${intent} Title"
        bind.tiEmail.hint = "Enter ${intent} Email"
        bind.tiPassword.hint = "Enter ${intent} Password"
        bind.location.hint = "Enter ${intent} Location"
        bind.mobile.hint = "Enter ${intent} Mobile No."


        bind.button1.setOnClickListener {
            val title = bind.tiName.text.toString()
            val email = bind.tiEmail.text.toString()
            val password = bind.tiPassword.text.toString()
            val location = bind.location.text.toString()
            val mobile = bind.mobile.text.toString()

            when {
                title.isEmpty() -> showToast("Please enter a valid title")
                email.isEmpty() || !email.contains("@gmail.com") -> showToast("Please enter a valid email")
                password.isEmpty() || password.length < 6 -> showToast("Please enter a valid password")
                location.isEmpty() -> showToast("Please enter a valid location")
                mobile.isEmpty() || mobile.length < 10 -> showToast("Please enter a valid mobile no.")
                else -> {
                    bind.loadingBar.isVisible = true
                    bind.driverLayout.isVisible = false
                    CoroutineScope(IO).launch {
                        RetrofitInstance.instance.entryRegister(
                            name = title,
                            email = email,
                            password = password,
                            mobile = mobile,
                            type = TYPE,
                            role = intent,
                            location = location
                        ).enqueue(object : Callback<CommonResponse?> {
                            override fun onResponse(
                                p0: Call<CommonResponse?>,
                                p1: Response<CommonResponse?>,
                            ) {
                                if (p1.isSuccessful) {
                                    val response = p1.body()!!
                                    if (!response.error) {
                                        showToast("$intent added Successfully")
                                        finish()
                                    } else {
                                        showToast(response.message)
                                    }
                                } else {
                                    showToast("Response Failed")
                                }
                                bind.loadingBar.isVisible = false
                                bind.driverLayout.isVisible = true
                            }

                            override fun onFailure(
                                p0: Call<CommonResponse?>,
                                p1: Throwable,
                            ) {
                                showToast(p1.message!!)
                                bind.loadingBar.isVisible = false
                                bind.driverLayout.isVisible = true
                            }
                        })


                    }

                }
            }


        }
    }
}