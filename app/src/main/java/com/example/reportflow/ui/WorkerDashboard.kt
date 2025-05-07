package com.example.reportflow.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.reportflow.R
import com.example.reportflow.databinding.ActivityWorkerDashboardBinding

class WorkerDashboard : AppCompatActivity() {
    private val bind by lazy { ActivityWorkerDashboardBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bind.root)

        bind.viewComplaints.setOnClickListener {
            startActivity(Intent(this, ViewComplaints::class.java).apply {
                putExtra("role", "Worker")
            })
        }

        bind.viewaccepted.setOnClickListener {
            startActivity(Intent(this, ViewComplaints::class.java).apply {
                putExtra("role", "Worker1")
            })
        }

        bind.profile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }



    }
}