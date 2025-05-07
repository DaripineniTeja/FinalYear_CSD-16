package com.example.reportflow.response

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.reportflow.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class NotificationService : Service() {

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val schemeNames =
            intent.getStringArrayListExtra("UPLIFTING_LIST") ?: return START_NOT_STICKY

        showNotifications(schemeNames)

        return START_NOT_STICKY
    }

    @SuppressLint("MissingPermission")
    private fun showNotifications(schemeNames: List<String>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "UPLIFTING_SCHEME_CHANNEL",
                "Uplifting Farmer Schemes",
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }



        CoroutineScope(IO).launch {
            for (schemeName in schemeNames) {
                val notification =
                    NotificationCompat.Builder(this@NotificationService, "UPLIFTING_SCHEME_CHANNEL")
                        .setContentTitle("New Scheme Available!")
                        .setContentText("Check out the scheme: $schemeName")
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .build()

                // Show notification
                NotificationManagerCompat.from(this@NotificationService).notify(1, notification)
                delay(120000L)
            }

        }

    }

    override fun onBind(intent: Intent?): IBinder? {
        // This is not bound, so return null
        return null
    }
}




