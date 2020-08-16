package com.app.imagesearchdemo.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.imagesearchdemo.R
import com.app.imagesearchdemo.data.ImageData
import com.app.imagesearchdemo.utils.loadImage
import kotlinx.android.synthetic.main.row_image.view.*


class ImageListAdapter(var list: MutableList<ImageData>, var callback: (Int,ImageData) -> Unit) :
    RecyclerView.Adapter<ImageListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.row_image, parent, false)
    )

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (!list[position].images.isNullOrEmpty()) {
            holder.itemView.imageView.loadImage(list[position].images!![0]!!.link!!)
        }
        holder.itemView.setOnClickListener {
            callback.invoke(position,list[position])
        }
    }


    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v)

}