package com.example.waterreminder.ui.composite_screen.settings.schedule

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.waterreminder.R
import com.example.waterreminder.database.reminder.ReminderEntity
import com.example.waterreminder.databinding.ItemScheduleSettingBinding
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class ScheduleSettingAdapter @Inject constructor(
    val clickListener : getClickShedule,
): ListAdapter<ReminderEntity, ScheduleSettingAdapter.ViewHolder>(ScheduleSettingDiffCallback()){

    var mon: String = ""
    var tue: String = ""
    var wed: String = ""
    var thu: String = ""
    var fri: String = ""
    var sat: String = ""
    var sun: String = ""

    fun setData(dataList: List<ReminderEntity>?) {
        submitList(dataList)
    }

    inner class ViewHolder(private val binding: ItemScheduleSettingBinding):
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(reminderEntity: ReminderEntity, clickListener: getClickShedule) {

            binding.tvClock.text = reminderEntity.time

            if (reminderEntity.isSunday) {
                sun = "Sun"
                binding.rlSun.setBackgroundResource(R.drawable.bg_roll_call_day)
                binding.tvSun.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.white
                    )
                )
            } else {
                sun = ""
                binding.rlSun.setBackgroundResource(R.drawable.bg_drink_target)
                binding.tvSun.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.black
                    )
                )
            }
            if (reminderEntity.isSaturday) {
                sat = "Sat"
                binding.rlSat.setBackgroundResource(R.drawable.bg_roll_call_day)
                binding.tvSat.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.white
                    )
                )
            } else {
                sat = ""
                binding.rlSat.setBackgroundResource(R.drawable.bg_drink_target)
                binding.tvSat.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.black
                    )
                )
            }
            if (reminderEntity.isFriday) {
                fri = "Fri"
                binding.rlFri.setBackgroundResource(R.drawable.bg_roll_call_day)
                binding.tvFri.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.white
                    )
                )
            } else {
                fri = ""
                binding.rlFri.setBackgroundResource(R.drawable.bg_drink_target)
                binding.tvFri.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.black
                    )
                )
            }
            if (reminderEntity.isThursday) {
                thu = "Thu"
                binding.rlThu.setBackgroundResource(R.drawable.bg_roll_call_day)
                binding.tvThu.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.white
                    )
                )
            } else {
                thu = ""
                binding.rlThu.setBackgroundResource(R.drawable.bg_drink_target)
                binding.tvThu.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.black
                    )
                )
            }
            if (reminderEntity.isWednesday) {
                wed = "Wed"
                binding.rlWed.setBackgroundResource(R.drawable.bg_roll_call_day)
                binding.tvWed.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.white
                    )
                )
            } else {
                wed = ""
                binding.rlWed.setBackgroundResource(R.drawable.bg_drink_target)
                binding.tvWed.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.black
                    )
                )
            }
            if (reminderEntity.isTuesday) {
                tue = "Tue"
                binding.rlTue.setBackgroundResource(R.drawable.bg_roll_call_day)
                binding.tvTue.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.white
                    )
                )
            } else {
                tue = ""
                binding.rlTue.setBackgroundResource(R.drawable.bg_drink_target)
                binding.tvTue.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.black
                    )
                )
            }
            if (reminderEntity.isMonday) {
                mon = "Mon"
                binding.rlMon.setBackgroundResource(R.drawable.bg_roll_call_day)
                binding.tvMon.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.white
                    )
                )
            } else {
                mon = ""
                binding.rlMon.setBackgroundResource(R.drawable.bg_drink_target)
                binding.tvMon.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.black
                    )
                )
            }

            binding.sth.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    binding.sth.thumbTintList = ColorStateList.valueOf(Color.parseColor("#2196F3"))
                    binding.sth.trackTintList = ColorStateList.valueOf(Color.parseColor("#2196F3"))
                } else {
                    binding.sth.thumbTintList = ColorStateList.valueOf(Color.WHITE)
                    binding.sth.trackTintList = ColorStateList.valueOf(Color.WHITE)
                }
            }


            if (reminderEntity.isCheck) {
                binding.ll2.setBackgroundResource(R.color.white)
                binding.ll3.visibility = View.GONE
                binding.view1.visibility = View.GONE
                binding.view2.visibility = View.VISIBLE
                binding.ll4.visibility = View.GONE
                binding.ll5.visibility = View.VISIBLE
                if (sun.isNotEmpty() && sat.isNotEmpty() && fri.isNotEmpty() && thu.isNotEmpty() && wed.isNotEmpty() && tue.isNotEmpty() && mon.isNotEmpty()) {
                    binding.tvDay.text = "every day"
                } else {
                    val value = "$sun $mon $tue $wed $thu $fri $sat"
                    binding.tvDay.text = value.trim()
                }


            } else {
                binding.ll2.setBackgroundResource(R.color.gray)
                binding.ll3.visibility = View.VISIBLE
                binding.view1.visibility = View.VISIBLE
                binding.view2.visibility = View.GONE
                binding.ll4.visibility = View.VISIBLE
                binding.ll4.setBackgroundResource(R.color.gray)
                binding.ll5.visibility = View.GONE
            }


            binding.rlMon.setOnClickListener {
                clickListener.onItemClickIsMon(reminderEntity)
            }
            binding.rlTue.setOnClickListener {
                clickListener.onItemClickIsTue(reminderEntity)
            }
            binding.rlWed.setOnClickListener {
                clickListener.onItemClickIsWed(reminderEntity)
            }
            binding.rlThu.setOnClickListener {
                clickListener.onItemClickIsThu(reminderEntity)
            }
            binding.rlFri.setOnClickListener {
                clickListener.onItemClickIsFri(reminderEntity)
            }
            binding.rlSat.setOnClickListener {
                clickListener.onItemClickIsSat(reminderEntity)
            }
            binding.rlSun.setOnClickListener {
                clickListener.onItemClickIsSun(reminderEntity)
            }
            binding.tvClock.setOnClickListener {
                clickListener.onItemClickClock(reminderEntity)
            }

            binding.llSum.setOnClickListener {
                clickListener.onItemClick(reminderEntity)
            }

            binding.tvDelete.setOnClickListener {
                clickListener.onDeleteClick(reminderEntity)
            }
            binding.sth.setOnClickListener {
                clickListener.onItemClickSwitch(reminderEntity)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemScheduleSettingBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_schedule_setting,
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


class ScheduleSettingDiffCallback : DiffUtil.ItemCallback<ReminderEntity>() {
    override fun areItemsTheSame(oldItem: ReminderEntity, newItem: ReminderEntity): Boolean {
        return oldItem.time == newItem.time
    }

    override fun areContentsTheSame(oldItem: ReminderEntity, newItem: ReminderEntity): Boolean {
        return oldItem.isMonday == newItem.isMonday
    }
}

class getClickShedule @Inject constructor() {

    var onItemClick: ((ReminderEntity) -> Unit)? = null
    var onItemClickIsMon: ((ReminderEntity) -> Unit)? = null
    var onItemClickIsTue: ((ReminderEntity) -> Unit)? = null
    var onItemClickIsWed: ((ReminderEntity) -> Unit)? = null
    var onItemClickIsThu: ((ReminderEntity) -> Unit)? = null
    var onItemClickIsFri: ((ReminderEntity) -> Unit)? = null
    var onItemClickIsSat: ((ReminderEntity) -> Unit)? = null
    var onItemClickIsSun: ((ReminderEntity) -> Unit)? = null
    var onItemClickClock: ((ReminderEntity) -> Unit)? = null
    var onItemClickSwitch: ((ReminderEntity) -> Unit)? = null
    var onDeleteClick: ((ReminderEntity) -> Unit)? = null

    fun onItemClick(data: ReminderEntity) {
        onItemClick?.invoke(data)
    }
    fun onItemClickIsMon(data: ReminderEntity) {
        onItemClickIsMon?.invoke(data)
    }
    fun onItemClickIsTue(data: ReminderEntity) {
        onItemClickIsTue?.invoke(data)
    }
    fun onItemClickIsWed(data: ReminderEntity) {
        onItemClickIsWed?.invoke(data)
    }fun onItemClickIsThu(data: ReminderEntity) {
        onItemClickIsThu?.invoke(data)
    }fun onItemClickIsFri(data: ReminderEntity) {
        onItemClickIsFri?.invoke(data)
    }fun onItemClickIsSat(data: ReminderEntity) {
        onItemClickIsSat?.invoke(data)
    }fun onItemClickIsSun(data: ReminderEntity) {
        onItemClickIsSun?.invoke(data)
    }fun onItemClickClock(data: ReminderEntity) {
        onItemClickClock?.invoke(data)
    }fun onItemClickSwitch(data: ReminderEntity) {
        onItemClickSwitch?.invoke(data)
    }

    fun onDeleteClick(data: ReminderEntity) {
        onDeleteClick?.invoke(data)
    }
}
