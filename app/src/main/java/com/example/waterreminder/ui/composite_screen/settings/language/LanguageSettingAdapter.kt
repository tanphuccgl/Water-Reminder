package com.example.waterreminder.ui.composite_screen.settings.language

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.waterreminder.R
import com.example.waterreminder.databinding.ItemLanguageSettingBinding
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class LanguageSettingAdapter @Inject constructor(
    val clickListener: getClickLanguage
): ListAdapter<LanguageSettingModel,LanguageSettingAdapter.ViewHolder>(LanguageSettingDiffCallback()){

    private var mItems: List<LanguageSettingModel>? = null

    fun setData(dataList: List<LanguageSettingModel>) {
        this.mItems = dataList
        notifyDataSetChanged()
    }
    inner class ViewHolder(private val binding: ItemLanguageSettingBinding):
            RecyclerView.ViewHolder(binding.root){
                fun bind(languageSettingModel: LanguageSettingModel,clickListener: getClickLanguage){
                    if(languageSettingModel.active){
                        binding.btnDone.visibility = View.VISIBLE
                    }else{
                        binding.btnDone.visibility = View.GONE
                    }
                }
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemLanguageSettingBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_language_setting,
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val item = getItem(position)
        holder.bind(item,clickListener)
        holder.itemView.setOnClickListener {
            item.active = true
            notifyDataSetChanged()

            for (i in 0 until itemCount) {
                if (i != position) {
                    getItem(i).active = false
                }
            }
        }
    }
}

class LanguageSettingDiffCallback : DiffUtil.ItemCallback<LanguageSettingModel>() {
    override fun areItemsTheSame(oldItem: LanguageSettingModel, newItem: LanguageSettingModel): Boolean {
        return oldItem.name == newItem.name
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: LanguageSettingModel, newItem: LanguageSettingModel): Boolean {
        return oldItem == newItem
    }
}

class getClickLanguage @Inject constructor() {

    var onItemClick: ((LanguageSettingModel) -> Unit)? = null

    fun onItemClick(data: LanguageSettingModel) {
        onItemClick?.invoke(data)
    }
}