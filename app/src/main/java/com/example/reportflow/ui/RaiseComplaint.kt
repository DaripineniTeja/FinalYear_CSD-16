package com.example.reportflow.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.reportflow.databinding.ActivityRaiseComplaintBinding
import com.example.reportflow.response.CommonResponse
import com.example.reportflow.response.RetrofitInstance
import com.example.reportflow.response.RetrofitInstance.TYPE
import com.example.reportflow.utils.SessionManager
import com.example.reportflow.utils.showToast
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class RaiseComplaint : AppCompatActivity() {

    private val binding by lazy { ActivityRaiseComplaintBinding.inflate(layoutInflater) }
    private val fused by lazy { LocationServices.getFusedLocationProviderClient(this@RaiseComplaint) }
    private val shared by lazy { SessionManager(this) }
    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>
    private var selectedImageUri: Uri? = null
    private var base64Image: String? = null


    private var userId = "user123" // Replace with actual user ID
    private var userName = "John Doe" // Replace with actual user name
    private var userMobile = "9876543210" // Replace with actual mobile number

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        userId = "${shared.getUserId()}"
        userName = "${shared.getUserName()}"
        userMobile = "${shared.getUserMobile()}"

        setupActivityResultLaunchers()
        setupClickListeners()
    }

    private fun setupActivityResultLaunchers() {
        cameraLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    result.data?.extras?.get("data")?.let { bitmap ->
                        (bitmap as? Bitmap)?.let {
                            binding.imageView.setImageBitmap(it)
                            base64Image = convertBitmapToBase64(it)
                            selectedImageUri = null // Clear URI if we're using direct bitmap
                        }
                    }
                }
            }

        galleryLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    result.data?.data?.let { uri ->
                        selectedImageUri = uri
                        binding.imageView.setImageURI(uri)
                        base64Image = null // Will convert when submitting
                    }
                }
            }
    }

    @SuppressLint("MissingPermission")
    private fun setupClickListeners() {
        binding.captureImageButton.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraLauncher.launch(intent)
        }


        binding.submitButton.setOnClickListener {
            fused.lastLocation.addOnSuccessListener {
                val description = binding.descriptionEditText.text.toString()

                when {
                    description.isEmpty() -> showToast("Please enter complaint description")
                    base64Image == null && selectedImageUri == null -> showToast("Please capture or select an image")
                    else -> submitComplaint(description, "${it.latitude},${it.longitude}")
                }
            }
        }
    }

    private fun submitComplaint(description: String, location: String) {
        binding.progressBar2.isVisible = true
        binding.submitButton.isEnabled = false

        CoroutineScope(IO).launch {
            try {
                val timeStamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                    .format(Date())

                val imageString = base64Image ?: selectedImageUri?.let { uri ->
                    contentResolver.openInputStream(uri)?.use { inputStream ->
                        Base64.encodeToString(inputStream.readBytes(), Base64.NO_WRAP)
                    }
                }

                imageString?.let { encodedImage ->
                    RetrofitInstance.instance.submitComplaint(
                        userId = userId,
                        userName = userName,
                        userMobile = userMobile,
                        description = description,
                        imageUri = encodedImage,
                        location = location,
                        timeStamp = timeStamp,
                        status = "Pending", // Default status
                        type = TYPE
                    ).enqueue(object : Callback<CommonResponse?> {
                        override fun onResponse(
                            call: Call<CommonResponse?>,
                            response: Response<CommonResponse?>,
                        ) {
                            binding.progressBar2.isVisible = false
                            binding.submitButton.isEnabled = true

                            if (response.isSuccessful) {
                                response.body()?.let {
                                    if (!it.error) {
                                        showToast("Complaint submitted successfully")
                                        finish()
                                    } else {
                                        showToast(it.message)
                                    }
                                }
                            } else {
                                showToast("Failed to submit complaint: ${response.message()}")
                            }
                        }

                        override fun onFailure(call: Call<CommonResponse?>, t: Throwable) {
                            binding.progressBar2.isVisible = false
                            binding.submitButton.isEnabled = true
                            showToast("Network error: ${t.message}")
                        }
                    })
                } ?: run {
                    showToast("Failed to process image")
                    binding.progressBar2.isVisible = false
                    binding.submitButton.isEnabled = true
                }
            } catch (e: Exception) {
                binding.progressBar2.isVisible = false
                binding.submitButton.isEnabled = true
                showToast("Error: ${e.message}")
            }
        }
    }

    private fun convertBitmapToBase64(bitmap: Bitmap): String {
        return try {
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream)
            Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.NO_WRAP)
        } catch (e: Exception) {
            showToast("Image compression failed")
            ""
        }
    }


}