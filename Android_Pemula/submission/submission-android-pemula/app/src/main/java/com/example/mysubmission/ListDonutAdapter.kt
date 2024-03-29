package com.example.mysubmission

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class ListDonutAdapter(private val listDonut: ArrayList<Donut>) : RecyclerView.Adapter<ListDonutAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data: Donut)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_row_donut, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (name, price, stock, description, photo) = listDonut[position]
        holder.imgvDonut.setImageResource(photo)
        holder.tvDonutName.text = name
        holder.tvDonutPrice.text = price
        holder.tvDonutDesc.text = description

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DonutDetail::class.java)
            intent.putExtra("DATA", listDonut[holder.adapterPosition])
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return listDonut.size
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgvDonut: ImageView = itemView.findViewById(R.id.imgv_donut_photo)
        val tvDonutName: TextView = itemView.findViewById(R.id.tv_item_donut_name)
        val tvDonutPrice: TextView = itemView.findViewById(R.id.tv_item_donut_price)
        val tvDonutDesc: TextView = itemView.findViewById(R.id.tv_item_donut_dec_overview)
    }
}