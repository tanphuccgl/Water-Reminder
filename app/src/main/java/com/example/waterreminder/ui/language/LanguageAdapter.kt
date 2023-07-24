package com.example.waterreminder.ui.language

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.waterreminder.R
import com.example.waterreminder.databinding.ItemLanguageBinding
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class LanguageAdapter @Inject constructor() :
    ListAdapter<LanguageModel, LanguageAdapter.LanguageViewHolder>(LanguageDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val binding =
            ItemLanguageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LanguageViewHolder(binding)
    }


    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        holder.bindData(getItem(position))
    }

    fun getSelectedLanguage(): LanguageModel? {
        return currentList.firstOrNull { it.active }
    }

    inner class LanguageViewHolder(val binding: ItemLanguageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(languageModel: LanguageModel) {
            binding.txtName.text = languageModel.name
            if (languageModel.active) {
                binding.layoutItem.setBackgroundColor(binding.root.context.getColor(R.color.teal_700))
            } else {
                binding.layoutItem.setBackgroundColor(Color.WHITE)
            }

            binding.layoutItem.setOnClickListener {
                resetChecked()
                languageModel.active = true
                notifyDataSetChanged()
            }
            when (languageModel.code) {
                "fr" -> {
                    Glide.with(binding.root.context).asBitmap()
                        .load(R.drawable.ic_lg_french)
                        .into(binding.icLang)
                }

                "es" -> {
                    Glide.with(binding.root.context).asBitmap()
                        .load(R.drawable.ic_span_flag)
                        .into(binding.icLang)
                }

                "de" -> {
                    Glide.with(binding.root.context).asBitmap()
                        .load(R.drawable.ic_german_flag)
                        .into(binding.icLang)
                }

                "pt" -> {
                    Glide.with(binding.root.context).asBitmap()
                        .load(R.drawable.ic_por_flag)
                        .into(binding.icLang)
                }

                "en" -> {
                    Glide.with(binding.root.context).asBitmap()
                        .load(R.drawable.ic_english_flag)
                        .into(binding.icLang)
                }

                "hi" -> {
                    Glide.with(binding.root.context).asBitmap()
                        .load(R.drawable.ic_hindi_flag)
                        .into(binding.icLang)
                }

                "in" -> {
                    Glide.with(binding.root.context).asBitmap()
                        .load(R.drawable.ic_indo_flag)
                        .into(binding.icLang)
                }

                "zh" -> {
                    Glide.with(binding.root.context).asBitmap()
                        .load(R.drawable.ic_china_flag)
                        .into(binding.icLang)
                }
            }
        }

        private fun resetChecked() {
            currentList.forEach { it.active = false }
        }
    }
}

class LanguageDiffCallback : DiffUtil.ItemCallback<LanguageModel>() {
    override fun areItemsTheSame(oldItem: LanguageModel, newItem: LanguageModel): Boolean {
        return oldItem.code == newItem.code
    }

    override fun areContentsTheSame(oldItem: LanguageModel, newItem: LanguageModel): Boolean {
        return oldItem == newItem
    }


}