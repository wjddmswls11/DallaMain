package com.example.dalla.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dalla.databinding.ItemNowtop10Binding
import com.example.dalla.data.NowTop10
import android.util.Log

class NowTopAdapter: RecyclerView.Adapter<NowTopAdapter.NowTopViewHolder>() {

    private var nowTopTenList : ArrayList<NowTop10> = arrayListOf()

    fun setData(newData : ArrayList<NowTop10>){
        nowTopTenList.clear()
        nowTopTenList.addAll(newData)
        Log.d("NowTopAdapter", "Data set: $newData")
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NowTopViewHolder {
        val binding = ItemNowtop10Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NowTopViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return nowTopTenList.size
    }

    override fun onBindViewHolder(holder: NowTopViewHolder, position: Int) {
        val nowTop10 = nowTopTenList[position]
        Log.d("NowTopAdapter", "Binding position $position: $nowTop10")
        holder.bind(nowTop10)
    }



    inner class NowTopViewHolder(private val binding : ItemNowtop10Binding) : RecyclerView.ViewHolder(binding.root) {
       fun bind(nowTop10: NowTop10){
           Glide.with(itemView.context).load(nowTop10.tv_starBj).into(binding.imgNowTop10)
           binding.imgNowtop10Number.setImageResource(nowTop10.img_num)
           binding.tvNowtop10Name.text = nowTop10.bjName
       }
    }

}