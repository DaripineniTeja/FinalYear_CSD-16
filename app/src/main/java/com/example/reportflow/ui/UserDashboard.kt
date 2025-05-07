package com.example.reportflow.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.reportflow.databinding.ActivityUserDashboardBinding
import com.example.reportflow.utils.SessionManager

class UserDashboard : AppCompatActivity() {
    private val bind by lazy { ActivityUserDashboardBinding.inflate(layoutInflater) }
    private val shared by lazy { SessionManager(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bind.root)


        bind.viewcomplaints.setOnClickListener {
            startActivity(Intent(this, ViewComplaints::class.java).apply {
                putExtra("role", "User")
            })
        }

        bind.vieworkers.setOnClickListener {
            startActivity(Intent(this, ViewList::class.java).apply {
                putExtra("role", "Worker")
            })
        }

        bind.profile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        bind.raisecomplaint.setOnClickListener {
            startActivity(Intent(this, RaiseComplaint::class.java).apply {
                putExtra("role", "User")
            })
        }

    }
}