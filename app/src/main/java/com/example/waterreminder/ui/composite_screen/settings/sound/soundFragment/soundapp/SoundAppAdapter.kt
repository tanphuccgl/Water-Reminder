package com.example.waterreminder.ui.composite_screen.settings.sound.soundFragment.soundapp

import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.waterreminder.R
import com.example.waterreminder.databinding.ItemSoundAppSettingBinding
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class SoundAppAdapter @Inject constructor(
    val clickListener : getClickSound
): ListAdapter<SoundModel, SoundAppAdapter.ViewHolder>(SoundSettingDiffCallback()){


    inner class ViewHolder(private val binding: ItemSoundAppSettingBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(soundModel: SoundModel, clickListener: getClickSound){
                if(soundModel.isCheck){
                    binding.btnDone.visibility = View.VISIBLE
                    val mediaPlayer = MediaPlayer.create(binding.root.context,soundModel.file)
                    mediaPlayer.start()
                }else{
                    binding.btnDone.visibility = View.GONE
                }

                binding.tvName.text = soundModel.name
                binding.ll1.setOnClickListener {
                    clickListener.onItemClick(soundModel)
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemSoundAppSettingBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_sound_app_setting,
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item,clickListener)
        holder.itemView.setOnClickListener {
            item.isCheck = true
            notifyDataSetChanged()

            for (i in 0 until itemCount) {
                if (i != position) {
                    getItem(i).isCheck = false
                }
            }
            clickListener.onItemClick(item)
        }
    }
}

class SoundSettingDiffCallback : DiffUtil.ItemCallback<SoundModel>() {
    override fun areItemsTheSame(oldItem: SoundModel, newItem: SoundModel): Boolean {
        return oldItem.file == newItem.file
    }

    override fun areContentsTheSame(oldItem: SoundModel, newItem: SoundModel): Boolean {
        return oldItem.name == newItem.name
    }
}

class getClickSound @Inject constructor() {

    var onItemClick: ((SoundModel) -> Unit)? = null

    fun onItemClick(data: SoundModel) {
        onItemClick?.invoke(data)
    }
}
