package com.example.dalla.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.dalla.R
import com.example.dalla.databinding.ItemCircleimageBinding
import com.example.dalla.data.MyStar

class CircleImgAdapter: RecyclerView.Adapter<CircleImgAdapter.StarBjViewHolder>()  {

    private var myStarList: ArrayList<MyStar> = arrayListOf()

    fun setData(newData : ArrayList<MyStar>){
        myStarList.clear()
        myStarList.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StarBjViewHolder {
        val binding = ItemCircleimageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StarBjViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return myStarList.size
    }

    override fun onBindViewHolder(holder: StarBjViewHolder, position: Int) {
        val mystar = myStarList[position]
        holder.bind(mystar)

    }


    inner class StarBjViewHolder(private val binding: ItemCircleimageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(myStar : MyStar){
            Glide.with(itemView.context)
                .load(myStar.profImg.url)
                .transform(CircleCrop())
                .into(binding.imgOvalThird)

            binding.tvStarBj.text = myStar.nickNm

            if (myStar.title =="방송 준비중") {
                binding.imgOvalFirst.setImageResource(R.drawable.se_4_c_8_e_90_d_6_2523_4655_8495_8_e_4_e_79781_af_4)
            }else{
                binding.imgOvalFirst.setImageResource(R.drawable.gradient_second)
            }
        }
    }
}