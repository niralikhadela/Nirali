package com.example.myappcrypto.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myappcrypto.R

class PriceAdapter : RecyclerView.Adapter<PriceAdapter.PriceViewHolder>() {
    private val items: MutableList<Pair<String, String>> = mutableListOf()

    fun addItem(date: String, price: String) {
        items.add(date to price)
        notifyItemInserted(items.size - 1)
    }

    fun clearItems() {
        items.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PriceViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_table_cell, parent, false)
        return PriceViewHolder(view)
    }

    override fun onBindViewHolder(holder: PriceViewHolder, position: Int) {
        val (date, price) = items[position]
        holder.bind(date, price)
    }

    override fun getItemCount(): Int = items.size

    class PriceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textDate: TextView = itemView.findViewById(R.id.textDate)
        private val textPrice: TextView = itemView.findViewById(R.id.textPrice)

        fun bind(date: String, price: String) {
            textDate.text = date
            textPrice.text = price
        }
    }
}
