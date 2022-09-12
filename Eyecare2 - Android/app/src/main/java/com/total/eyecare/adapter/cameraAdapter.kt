package com.total.eyecare.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.total.eyecare.R
import com.total.eyecare.classes.camera

class CameraAdapter(private val listProfile: List<camera>) : RecyclerView.Adapter<CameraAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.camera_item, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val profile = listProfile[position]

        Glide.with(holder.itemView.context)
            .load(profile.thumbnail)
            .into(holder.imgPhoto)

        holder.tvName.text = profile.title
        holder.tvLocation.text = profile.ip
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listProfile[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = listProfile.size

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgPhoto: ImageView = itemView.findViewById(R.id.img_item_photo)
        var tvName: TextView = itemView.findViewById(R.id.tv_item_name)
        var tvLocation: TextView = itemView.findViewById(R.id.tv_item_location)
    }
    interface OnItemClickCallback {
        fun onItemClicked(data: camera)
    }
}