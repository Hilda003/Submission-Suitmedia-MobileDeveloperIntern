package com.example.submission_suitmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.submission_suitmedia.data.DataItem
import com.example.submission_suitmedia.databinding.ItemUserBinding

class UserAdapter(
    private val users: List<DataItem>,
    private val onUserClick: (DataItem) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int = users.size

    inner class UserViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: DataItem) {
            binding.apply {
                txtUsername.text = "${user.firstName} ${user.lastName}"
                txtEmailUser.text = user.email
                Glide.with(itemView.context)
                    .load(user.avatar)
                    .circleCrop()
                    .into(imgUser)

                root.setOnClickListener {
                    onUserClick(user)
                }
            }
        }
    }
}
