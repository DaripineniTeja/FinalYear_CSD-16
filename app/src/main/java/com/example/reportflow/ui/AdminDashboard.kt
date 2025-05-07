package com.example.reportflow.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.reportflow.databinding.ActivityAdminDashboardBinding
import com.example.reportflow.utils.SessionManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AdminDashboard : AppCompatActivity() {
    private val bind by lazy { ActivityAdminDashboardBinding.inflate(layoutInflater) }
    private val shared by lazy { SessionManager(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bind.root)

        bind.logout.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Confirm") { _, _ ->
                    shared.clearLoginState()
                    finishAffinity()
                    startActivity(Intent(this@AdminDashboard, LoginActivity::class.java))
                }
                .setNegativeButton("Dismiss") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }


        bind.viewComplaints.setOnClickListener {
            startActivity(Intent(this, ViewComplaints::class.java).apply {
                putExtra( "role","Admin")
            })
        }

        bind.addWorkers.setOnClickListener {
            startActivity(Intent(this, AddCommonDataActivity::class.java).apply {
                putExtra( "role","Worker")
            })
        }
        bind.viewUsers.setOnClickListener {
            startActivity(Intent(this, ViewList::class.java).apply {
                putExtra( "role","User")
            })
        }


        bind.viewworkers.setOnClickListener {
            startActivity(Intent(this, ViewList::class.java).apply {
                putExtra( "role","Worker")
            })
        }



    }
}