package com.app.imagesearchdemo.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.imagesearchdemo.R
import kotlinx.android.synthetic.main.row_comment.view.*


class CommentListAdapter(var list: MutableList<String>, var callback: (Int) -> Unit) :
    RecyclerView.Adapter<CommentListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.row_comment, parent, false)
    )

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.textViewComment.text = list[position]
    }


    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v)

}