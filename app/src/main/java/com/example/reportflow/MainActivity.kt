package com.example.reportflow

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.reportflow.databinding.ActivityMainBinding
import com.example.reportflow.ui.LoginActivity

class MainActivity : AppCompatActivity() {
    private val bind by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bind.root)

        bind.imageView3.apply {
            Glide.with(this@MainActivity).load(R.drawable.waterimage).into(this)

            alpha = 0f
            animate().alpha(1f).setDuration(4000).withStartAction {
            }.withEndAction {
                finish()
                startActivity(
                    Intent(
                        this@MainActivity,
                        LoginActivity::class.java
                    )
                )

            }

        }

    }
}