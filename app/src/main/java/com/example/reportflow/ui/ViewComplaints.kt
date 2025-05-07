package com.example.reportflow.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reportflow.adapter.ComplaintAdapter
import com.example.reportflow.databinding.ActivityViewComplaintsBinding
import com.example.reportflow.model.Reports
import com.example.reportflow.response.CommonResponse
import com.example.reportflow.response.LoginResponse
import com.example.reportflow.response.RetrofitInstance
import com.example.reportflow.response.RetrofitInstance.TYPE
import com.example.reportflow.utils.SessionManager
import com.example.reportflow.utils.showToast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewComplaints : AppCompatActivity() {
    private val bind by lazy { ActivityViewComplaintsBinding.inflate(layoutInflater) }
    private val shared by lazy { SessionManager(this) }
    var userId = ""
    var role = ""
    var status = ""
    var mobile = ""
    private lateinit var adapter: ComplaintAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bind.root)
        mobile = "${shared.getUserMobile()}"
        showToast(mobile)


        adapter = ComplaintAdapter(this@ViewComplaints, emptyList()) {
            if (shared.getUserRole() == "Worker" && (it.status == "Accepted" || it.status == "Pending")) {
                status = when (it.status) {
                    "Pending" -> "Accepted"
                    "Accepted" -> "Completed"
                    else -> "Completed" // Handles empty string and any other cases
                }
                openDialog(it, status)
            } else {
                showToast(it.location)
            }
        }


        bind.complaintrecycler.adapter = adapter
        bind.complaintrecycler.layoutManager = LinearLayoutManager(this@ViewComplaints)

        userId = "${shared.getUserId()}"
        role = intent.getStringExtra("role").toString()
        showToast("No Error : $userId")

        loadData()

    }

    private fun loadData() {
        RetrofitInstance.instance.getComplaints().enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>,
            ) {
                val body = response.body()!!
                val list = if (role == "User") {
                    body.data2.filter { it.userId == "$userId" }
                } else if (role == "Worker1") {
                    body.data2.filter { it.workerId == "$userId" }
                } else if (role == "Worker") {
                    body.data2.filter { it.status == "Pending" }
                } else {
                    body.data2
                }
                Log.d("sdkjfjds", "onResponse: $list")
                if (body.error) {
                    showToast("True: ${body.message}")
                } else {
                    if (list.isEmpty()) {
                        showToast("No Complaints")
                    } else {
                        adapter.updateList(list.filter { it.type == TYPE })
                    }


                }

            }

            override fun onFailure(p0: Call<CommonResponse?>, p1: Throwable) {
                showToast("Server Error : ${p1.message}")

            }
        })


    }

    private fun openDialog(it: Reports,status1: String) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Update Status")
            .setMessage("Do you want to update this?")
            .setPositiveButton("Confirm") { dialog, _ ->
                updateRequest(it,status1)

            }.setNeutralButton("Dismiss") { dialog, _ ->
                dialog.dismiss()

            }.show()

    }

    private fun updateRequest(it: Reports,status1: String) {
        RetrofitInstance.instance.updateStatus(it.id,"${shared.getUserId()}","${shared.getUserName()}",mobile,status1)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    p0: Call<LoginResponse?>,
                    p1: Response<LoginResponse?>,
                ) {
                    val body = p1.body()!!
                    if (body.error == false) {
                        showToast("Status Updated")
                        loadData()
                    } else {
                        showToast("Error is true")
                    }
                }

                override fun onFailure(
                    p0: Call<LoginResponse?>,
                    p1: Throwable,
                ) {
                    showToast("Server Error : ${p1.message}")
                }


            })
    }

}