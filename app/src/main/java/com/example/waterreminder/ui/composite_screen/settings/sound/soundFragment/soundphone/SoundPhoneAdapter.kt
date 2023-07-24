package com.example.waterreminder.ui.composite_screen.settings.sound.soundFragment.soundphone

import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.waterreminder.R
import com.example.waterreminder.database.WaterReminderEntity
import com.example.waterreminder.database.cup.SwitchCup
import com.example.waterreminder.database.sound.SoundEntity
import com.example.waterreminder.databinding.ItemSoundPhoneSettingBinding
import com.example.waterreminder.ui.composite_screen.settings.sound.soundFragment.soundapp.SoundModel
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject
@ActivityScoped
class SoundPhoneAdapter @Inject constructor(
    val clickListener : getClickSoundPhone
): ListAdapter<SoundEntity, SoundPhoneAdapter.ViewHolder>(SoundPhoneSettingDiffCallback()){

    private var handler: Handler? = null
    private var stopMusicRunnable: Runnable? = null

    fun setData(dataList: List<SoundEntity>) {
        submitList(dataList)
    }

    inner class ViewHolder(private val binding: ItemSoundPhoneSettingBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(soundEntity: SoundEntity, clickListener: getClickSoundPhone){
            if(soundEntity.isCheck && soundEntity.name != "More"){
                binding.btnDone.setImageResource(R.drawable.ic_check_done_language)
                var mediaPlayer = MediaPlayer()
                if(soundEntity.isCheckRaw == 1){
                    mediaPlayer = MediaPlayer.create(binding.root.context,soundEntity.rawId)
                    mediaPlayer.start()
                }else{
                    try {
                        mediaPlayer.setDataSource(soundEntity.file)
                        mediaPlayer.prepare()
                        handler = Handler(Looper.getMainLooper())
                        stopMusicRunnable = Runnable {
                            mediaPlayer.stop()
                        }
                        handler?.postDelayed(stopMusicRunnable!!, 5000)
                        // Bắt đầu phát nhạc
                        mediaPlayer.start()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

            }else{
                binding.btnDone.setImageResource(R.color.white)
            }

            if(soundEntity.name == "More"){
                binding.tvName.setTextColor(ContextCompat.getColor(binding.root.context,R.color.green))
                binding.tvName.text = soundEntity.name
                binding.ll1.setOnClickListener {
                    clickListener.onItemClickMore(soundEntity)
                }
            }else{
                binding.tvName.text = soundEntity.name
                binding.tvName.setTextColor(ContextCompat.getColor(binding.root.context,R.color.black))
                binding.ll1.setOnClickListener {
                    clickListener.onItemClick(soundEntity)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemSoundPhoneSettingBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_sound_phone_setting,
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item,clickListener)
    }

}

class SoundPhoneSettingDiffCallback : DiffUtil.ItemCallback<SoundEntity>() {
    override fun areItemsTheSame(oldItem: SoundEntity, newItem: SoundEntity): Boolean {
        return oldItem.file == newItem.file
    }

    override fun areContentsTheSame(oldItem: SoundEntity, newItem: SoundEntity): Boolean {
        return oldItem.name == newItem.name
    }
}

class getClickSoundPhone @Inject constructor() {

    var onItemClick: ((SoundEntity) -> Unit)? = null
    var onItemClickMore: ((SoundEntity) -> Unit)? = null

    fun onItemClick(data: SoundEntity) {
        onItemClick?.invoke(data)
    }fun onItemClickMore(data: SoundEntity) {
        onItemClickMore?.invoke(data)
    }
}

