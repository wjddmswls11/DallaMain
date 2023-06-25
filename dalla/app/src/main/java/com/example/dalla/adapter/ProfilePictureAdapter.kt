package com.example.dalla.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dalla.data.Banner
import com.example.dalla.databinding.ItemProfilepictureBinding

class ProfilePictureAdapter:
    RecyclerView.Adapter<ProfilePictureAdapter.ProfilePictureViewHolder>() {

    private var bannerList: ArrayList<Banner> = arrayListOf()

    val dataItemCount : Int
        get() = bannerList.size

    fun setData(newData : ArrayList<Banner>){
        bannerList.clear()
        bannerList.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfilePictureAdapter.ProfilePictureViewHolder {
        val binding = ItemProfilepictureBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProfilePictureViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return if (bannerList.size > 0) Int.MAX_VALUE else 0
    }

    override fun onBindViewHolder(holder: ProfilePictureAdapter.ProfilePictureViewHolder, position: Int) {
        val index = position % bannerList.size
        holder.bind(bannerList[index])
    }


    inner class ProfilePictureViewHolder(private val binding: ItemProfilepictureBinding) : RecyclerView.ViewHolder(binding.root) {
        //무한 스크롤을 사용할 경우 posotion이 아니라 Banner를 이용
        fun bind(banner: Banner) {
            Glide.with(itemView.context)
                .load(banner.imageProfile)
                .into(binding.imageSlider)

            binding.tvProfileFirst.text = banner.title
            binding.tvProfileSecond.text = banner.memNick
        }
    }



}