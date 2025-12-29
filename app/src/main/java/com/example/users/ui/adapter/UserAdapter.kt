package com.example.users.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.users.databinding.ItemGridUserBinding
import com.example.users.databinding.ItemUserBinding
import com.example.users.domain.models.User

class UserAdapter(
    private var isGrid: Boolean,
    private val onClick: (User) -> Unit
) : ListAdapter<User, RecyclerView.ViewHolder>(DiffCallback()) {

    companion object {
        private const val VIEW_TYPE_LIST = 0
        private const val VIEW_TYPE_GRID = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (isGrid) VIEW_TYPE_GRID else VIEW_TYPE_LIST
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {

        return when (viewType) {

            VIEW_TYPE_GRID -> {
                val binding = ItemGridUserBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                GridViewHolder(binding)
            }

            else -> {
                val binding = ItemUserBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ListViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val user = getItem(position)

        when (holder) {
            is ListViewHolder -> holder.bind(user)
            is GridViewHolder -> holder.bind(user)
        }
    }

    fun switchView(isGrid: Boolean) {
        this.isGrid = isGrid
        notifyItemRangeChanged(0, itemCount)
    }

    inner class ListViewHolder(
        private val binding: ItemUserBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) = with(binding) {
            tvName.text = buildString {
                append(user.name)
                append(" ")
                append(user.lastname)
            }
            tvCompany.text = user.company

            Glide.with(root.context)
                .load(user.photoResId)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .circleCrop()
                .into(imgPhoto)

            root.setOnClickListener { onClick(user) }
        }
    }

    inner class GridViewHolder(
        private val binding: ItemGridUserBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) = with(binding) {
            tvNameGrid.text = buildString {
                append(user.name)
                append(" ")
                append(user.lastname)
            }

            Glide.with(root.context)
                .load(user.photoResId)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(photoGrid)

            root.setOnClickListener { onClick(user) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: User, newItem: User) =
            oldItem == newItem
    }
}
