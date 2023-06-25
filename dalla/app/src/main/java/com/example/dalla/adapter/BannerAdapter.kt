package com.example.dalla.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dalla.databinding.ItemBannerBinding
import com.example.dalla.data.Event

class BannerAdapter : RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {

    private var eventList : ArrayList<Event> = arrayListOf()

    val dataItemCount: Int
        get() = eventList.size
    fun setData(newData : ArrayList<Event>){
        eventList.clear()
        eventList.addAll(newData)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val binding = ItemBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BannerViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return if (eventList.size > 0) Int.MAX_VALUE else 0
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        val eventItem = eventList[position % eventList.size]
        holder.bind(eventItem.bannerUrl)
    }



    inner class BannerViewHolder(private val binding: ItemBannerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(eventBanner : String){
            Glide.with(binding.imgBannerGame)
                .load(eventBanner)
                .into(binding.imgBannerGame)
        }
    }

}