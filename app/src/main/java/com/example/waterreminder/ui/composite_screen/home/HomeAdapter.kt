package com.example.waterreminder.ui.composite_screen.home
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.waterreminder.R
import com.example.waterreminder.database.WaterReminderEntity
import com.example.waterreminder.databinding.ItemClockDrinkWaterBinding
import com.example.waterreminder.domain.DrinkWaterRecord
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class HomeAdapter @Inject constructor(
     val clickListener: getClickHome
    ) : ListAdapter<WaterReminderEntity, HomeAdapter.ViewHolder>(HomeDiffCallback()) {

    private var mItems: List<WaterReminderEntity>? = null

    fun setData(dataList: List<WaterReminderEntity>?) {
        this.mItems = dataList
        submitList(dataList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemClockDrinkWaterBinding =
            DataBindingUtil.inflate(LayoutInflater.from(parent.context),R.layout.item_clock_drink_water,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item,clickListener)
    }
    inner class ViewHolder(private val binding: ItemClockDrinkWaterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: WaterReminderEntity,clickListener: getClickHome) {

            binding.imgView.setImageResource(data.image)
            binding.tvClock.text = data.time
            binding.tvWater.text = data.quantityWater

            binding.btnMenu.setOnClickListener {
                val popupMenu = PopupMenu(itemView.context,binding.btnMenu)
                val inflater: MenuInflater = popupMenu.menuInflater
                inflater.inflate(R.menu.menu_popu, popupMenu.menu)
                popupMenu.show()

                // Xử lý sự kiện khi mục menu được chọn
                popupMenu.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.menu_edit -> {
                            clickListener.onEditClick(data)
                            true
                        }
                        R.id.menu_delete -> {
                            clickListener.onDeleteClick(data)
                            true
                        }
                        else -> false
                    }
                }
            }
            }
        }

}

class HomeDiffCallback : DiffUtil.ItemCallback<WaterReminderEntity>() {
    override fun areItemsTheSame(oldItem: WaterReminderEntity, newItem: WaterReminderEntity): Boolean {
        // Kiểm tra xem hai đối tượng có cùng item hay không (ví dụ: kiểm tra ID)
        return oldItem.checkedDate == newItem.checkedDate
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: WaterReminderEntity, newItem: WaterReminderEntity): Boolean {
        return oldItem == newItem
    }
}

class getClickHome @Inject constructor() {

    var onItemClick: ((WaterReminderEntity) -> Unit)? = null
    var onDeleteClick: ((WaterReminderEntity) -> Unit)? = null

    fun onEditClick(data: WaterReminderEntity) {
        onItemClick?.invoke(data)
    }
    fun onDeleteClick(data: WaterReminderEntity) {
        onDeleteClick?.invoke(data)
    }
}