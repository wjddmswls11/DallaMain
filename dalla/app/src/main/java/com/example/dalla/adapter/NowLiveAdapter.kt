package com.example.dalla.adapter

import  android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dalla.R
import com.example.dalla.databinding.ItemNowliveBinding
import com.example.dalla.data.Room

class NowLiveAdapter: RecyclerView.Adapter<NowLiveAdapter.NowLiveViewHolder>() {

    private var roomList: ArrayList<Room> = arrayListOf()

    fun setData(newData : ArrayList<Room>){
        roomList.clear()
        roomList.addAll(newData)
        notifyDataSetChanged()
    }

    inner class NowLiveViewHolder(private val binding: ItemNowliveBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(room: Room) {
            Glide.with(itemView.context)
                .load(room.thumbnailUrl)
                .into(binding.imgNowLive)

            Glide.with(itemView.context)
                .load(room.teamMedalUrl)
                .into(binding.imgLiveMedal)
            Glide.with(itemView.context)
                .load(room.teamBgUrl)
                .into(binding.imgLiveMedal3)

            binding.tvLiveDjname.text = room.bjNickName
            binding.tvLiveTitle.text = room.roomTitle
            binding.tvPeopleG.text = room.countByeol.toString()
            binding.tvHeart.text = room.countGood.toString()
            binding.imgContent.setImageResource(R.drawable.badge_contents)


            if (room.bjMemSex == "f") {
                binding.imgLiveMedal2.setImageResource(R.drawable.ico_female)
            } else {
                binding.imgLiveMedal2.setImageResource(R.drawable.ico_male)
            }

            if (room.typeMedia == "v") {
                binding.imgNowliveVideo.setImageResource(R.drawable.ico_video)
            } else {
                binding.imgNowliveVideo.visibility = View.GONE
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NowLiveViewHolder {
        val binding = ItemNowliveBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NowLiveViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return roomList.size
    }


    override fun onBindViewHolder(holder: NowLiveViewHolder, position: Int) {
        val room = roomList[position]
        holder.bind(room)

    }


}