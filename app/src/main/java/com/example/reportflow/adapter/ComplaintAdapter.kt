package com.example.reportflow.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.reportflow.databinding.ItemComplaintBinding
import com.example.reportflow.model.Reports
import com.example.reportflow.utils.spanned
import java.util.Locale


class ComplaintAdapter(
    private val context: Context,
    private var complaints: List<Reports>,
    private var onclick: (Reports) -> Unit,
) : RecyclerView.Adapter<ComplaintAdapter.ComplaintViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComplaintViewHolder {
        val binding = ItemComplaintBinding.inflate(LayoutInflater.from(context), parent, false)
        return ComplaintViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ComplaintViewHolder, position: Int) {
        val complaint = complaints[position]
        holder.binding.complaintImage.setOnClickListener {
            onclick(complaint)
        }
        holder.bind(complaint)
    }

    override fun getItemCount(): Int = complaints.size

    inner class ComplaintViewHolder(val binding: ItemComplaintBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(complaint: Reports) {
            binding.userNameText.text = spanned("<b>Name</b>: ${complaint.userName}")
            binding.userMobileText.text = spanned("<b>Mobile</b>: ${complaint.userMobile}")
            binding.descriptionText.text = spanned("<b>Desc</b>: ${complaint.description}")
            binding.timestampText.text = spanned("<b>time</b>: ${complaint.timestamp}")
            binding.statusText.text = spanned("<b>Status</b>: ${complaint.status}")

            // Load image using Picasso
            Glide.with(context).load(complaint.imageUri).into(binding.complaintImage)

            // Parse location and use Geocoder
            val (latitude, longitude) = complaint.location.split(",").map { it.toDouble() }
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            val address = addresses?.firstOrNull()?.getAddressLine(0) ?: "Unknown Location"
            binding.locationText.text = address

            binding.locationtrack.setOnClickListener {


                val gmmIntentUri = Uri.parse("google.navigation:q=$latitude,$longitude&mode=d")

                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")

                if (mapIntent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(mapIntent)
                } else {
                    val webIntent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://www.google.com/maps/dir/?api=1&destination=$latitude,$longitude")
                    )
                    context.startActivity(webIntent)
                }
            }


// Show worker info only if workerName is not empty
            if (complaint.workerName.isNullOrEmpty()) {
                binding.workerInfoText.visibility = android.view.View.GONE
            } else {
                binding.workerInfoText.visibility = android.view.View.VISIBLE
                binding.workerInfoText.text =
                    "Worker: ${complaint.workerName}, ${complaint.workerMobile}"
            }
        }
    }

    fun updateList(newList: List<Reports>) {
        complaints = newList
        notifyDataSetChanged()
    }
}