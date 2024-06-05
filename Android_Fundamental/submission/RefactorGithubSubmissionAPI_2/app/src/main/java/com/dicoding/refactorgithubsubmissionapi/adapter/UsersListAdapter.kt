package com.dicoding.refactorgithubsubmissionapi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.refactorgithubsubmissionapi.data.remote.response.ItemsItem
import com.dicoding.refactorgithubsubmissionapi.databinding.ItemUserBinding

class UsersListAdapter : ListAdapter<ItemsItem, UsersListAdapter.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemsItem>() {
            override fun areItemsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    /*
    * setiap kali RecyclerView butuh ViewHolder baru untuk menampilkan item, maka RecyclerView akan panggil onCreateViewHolder()
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    /*
    * ViewHolder() -> semacam wadah untuk setiap item yg akan ditampilkan dalam list
    * fungsinya untuk simpan referensi ke tampilan2 yg bakal digunain untuk nampilin satu item dalam list
    * bind() method -> untuk bind data ke view
    * tapi bind() dipanggil nya nanti di onBindViewHolder(),
    * ** kalau RecyclerView perlu nampilin item baru, dia coba periksa dulu ViewHolder yg udah ga digunakan
    * ** (biasanya item yang sudah tidak terlihat di layar)
    * ** karena ada ViewHolder yg ga dipake, daripada buat ViewHolder baru (pakai onCreateViewHolder), better pakai ViewHolder yg ga kepake aja
    * ** (ini konsep recycling ViewHolder)
     */
    class ViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(userAccount: ItemsItem) {
            binding.tvUsername.text = userAccount.login
            Glide.with(itemView)
                .load(userAccount.avatarUrl)
                .into(binding.imgvProfileAccount)
        }
    }

    private var itemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(userAccount: ItemsItem)  // method dipanggil saat item ada yang di-klik
    }

    /*
    * setter untuk itemClickListener
     */
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }

    /*
    * method ketika RecyclerView butuh buat bind data ke ViewHolder
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userAccount = getItem(position)
        holder.bind(userAccount)  // panggil method bind() dari ViewHolder

        /*
        * kalau ada item yang di-klik
         */
        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(userAccount)
        }
    }
}