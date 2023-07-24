package com.example.waterreminder.ui.composite_screen.home.switchcup

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.waterreminder.R
import com.example.waterreminder.databinding.IteamSwitchCupBinding
import com.example.waterreminder.database.cup.SwitchCup
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject
@ActivityScoped
class SwitchCupAdapter @Inject constructor(
    val onClick: getSwitchCup
): ListAdapter<SwitchCup, SwitchCupAdapter.ViewHolder>(SwitchCupDiffCallback()) {


    fun setData(dataList: List<SwitchCup>) {
        submitList(dataList)
    }

    inner class ViewHolder(private val binding: IteamSwitchCupBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("DiscouragedApi")
        fun bind(switchCup: SwitchCup, onClick: getSwitchCup) {
            binding.imgView.setImageResource(
                if (switchCup.isSelected && switchCup.quantityWater != "Customize") {
                    // Lấy tên ảnh được chọn
                    val imageName =
                        binding.root.context.resources.getResourceEntryName(switchCup.image)
                    imageName.replace("_unselected", "")
                } else {
                    // Lấy tên ảnh không được chọn
                    binding.root.context.resources.getResourceEntryName(switchCup.image)
                }.let { imageName ->
                    // Lấy ID ảnh tương ứng
                    val imageResId = binding.root.context.resources.getIdentifier(
                        imageName,
                        "drawable",
                        binding.root.context.packageName
                    )
                    imageResId
                }
            )
            binding.tvWater.text = switchCup.quantityWater
            if(switchCup.editImage){
                binding.imgDelete.visibility = View.VISIBLE
            }else{
                binding.imgDelete.visibility = View.GONE
            }

            binding.imgDelete.setOnClickListener {
                onClick.onItemDelete(switchCup)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: IteamSwitchCupBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.iteam_switch_cup,
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onClick)
        holder.itemView.setOnClickListener {
            if (item.quantityWater == "Customize"){
                item.isSelected = false
                notifyDataSetChanged()
                for (i in 0 until itemCount) {
                    if (i != position) {
                        getItem(i).isSelected = false
                    }
                }
                onClick.onItemClickCus(item)
            }
                item.isSelected = true
                notifyDataSetChanged()
                // Thay đổi trạng thái của các mục khác
                for (i in 0 until itemCount) {
                    if (i != position) {
                        getItem(i).isSelected = false
                    }
                }
        }

    }
}

class SwitchCupDiffCallback : DiffUtil.ItemCallback<SwitchCup>() {
    override fun areItemsTheSame(oldItem: SwitchCup, newItem: SwitchCup): Boolean {
        // Kiểm tra xem hai đối tượng có cùng item hay không (ví dụ: kiểm tra ID)
        return oldItem.image == newItem.image
    }

    override fun areContentsTheSame(oldItem: SwitchCup, newItem: SwitchCup): Boolean {
        // Kiểm tra xem hai đối tượng có cùng nội dung hay không
        return oldItem.quantityWater == newItem.quantityWater
    }
}

class getSwitchCup @Inject constructor() {

    var onItemClick: ((SwitchCup) -> Unit)? = null
    var onItemClickCus: ((SwitchCup) -> Unit)? = null
    var onItemDelete:((SwitchCup) -> Unit)? = null

    fun onItemClick(data: SwitchCup) {
        onItemClick?.invoke(data)
    }
    fun onItemDelete(data: SwitchCup) {
        onItemDelete?.invoke(data)
    }
    fun onItemClickCus(data: SwitchCup) {
        onItemClickCus?.invoke(data)
    }
}
