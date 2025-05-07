package com.example.reportflow.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.reportflow.databinding.ViewUserListBinding
import com.example.reportflow.model.Entries
import com.example.reportflow.utils.spanned

class ViewListAdapter(var list: List<Entries>, var onclick: (Entries) -> Unit) :
    RecyclerView.Adapter<ViewListAdapter.ViewNGO>() {
    class ViewNGO(val binding: ViewUserListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(entries: Entries) {
            binding.name.text = spanned("<b>Name:</b> ${entries.name}")
            binding.number.text = spanned("<b>Mobile:</b> ${entries.mobile}")
            binding.email.text = spanned("<b>Email:</b> ${entries.email}")
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewNGO {
        val view = ViewUserListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewNGO(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewNGO, position: Int) {
        holder.bind(list[position])
        holder.binding.root.setOnClickListener {
            onclick(list[position])
        }
    }
}