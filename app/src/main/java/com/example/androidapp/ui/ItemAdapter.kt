package com.example.androidapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.androidapp.R
import com.example.androidapp.data.Item

class ItemAdapter(private val onClick: (Item) -> Unit) : ListAdapter<Item, ItemAdapter.ViewHolder>(DIFF) {
    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_list_row, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener { onClick(item) }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.item_title)
        private val desc: TextView = itemView.findViewById(R.id.item_description)

        fun bind(item: Item) {
            title.text = item.title
            desc.text = item.description ?: ""
        }
    }
}
