package com.viewpoint.dangder.view.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.viewpoint.dangder.databinding.ItemImageListBinding
import java.util.*

class ImageListAdapter : ListAdapter<Uri, ImageListAdapter.ViewHolder>(diffUtil) {

    interface OnStartDragListener {
        fun onStartDrag(viewHolder: ImageListAdapter.ViewHolder)
    }

    var onStartDragListener: OnStartDragListener? = null

    inner class ViewHolder(private val binding: ItemImageListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(uri: Uri) {
            Glide.with(binding.root)
                .load(uri)
                .fitCenter()
                .apply(RequestOptions.bitmapTransform(RoundedCorners(32)))
                .into(binding.initdogImageItem)

            binding.initdogImageItem.setOnTouchListener { v, event ->
                if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                    onStartDragListener?.onStartDrag(this)
                }
                return@setOnTouchListener false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemImageListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))

    }

    fun addItem(uri: Uri) {
        val new = this.currentList.toMutableList().apply {
            add(uri)
        }

        this.submitList(new)
    }

    fun swapItem(from: Int, to: Int) {
        val copy = this.currentList.toMutableList()
        Collections.swap(copy, from, to)
        this.submitList(copy)
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Uri>() {
            override fun areItemsTheSame(oldItem: Uri, newItem: Uri): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }

            override fun areContentsTheSame(oldItem: Uri, newItem: Uri): Boolean {
                return oldItem == newItem
            }

        }
    }
}