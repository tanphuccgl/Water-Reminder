package com.example.waterreminder.ui.composite_screen.settings.sound

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.viewpager2.widget.ViewPager2
import com.example.waterreminder.R
import com.example.waterreminder.base.BaseActivity
import com.example.waterreminder.databinding.ActivitySoundSettingBinding
import com.example.waterreminder.ui.question.QuestionsAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SoundSettingActivity : BaseActivity<ActivitySoundSettingBinding>() {

    private var settingAdapter : SoundSettingAdapter? = null

    override fun initView() {
        initViewPager()
        setListener()
    }

    override fun setBinding(layoutInflater: LayoutInflater): ActivitySoundSettingBinding {
        return ActivitySoundSettingBinding.inflate(layoutInflater)
    }

    private fun initViewPager() {
        settingAdapter = SoundSettingAdapter(this)
        binding.viewPager2.adapter = settingAdapter
        binding.viewPager2.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position){
                    0 -> {
                        binding.rl2.setOnClickListener {
                            binding.viewPager2.currentItem = position + 1
                            binding.rl2.setBackgroundResource(R.drawable.bg_backgroud_until)
                            binding.tvPhone.setTextColor(resources.getColor(R.color.black))
                            binding.rl1.setBackgroundResource(R.drawable.bg_backgroud_until_blue)
                            binding.tvApp.setTextColor(resources.getColor(R.color.white))
                        }

                        binding.rl1.setBackgroundResource(R.drawable.bg_backgroud_until)
                        binding.tvApp.setTextColor(resources.getColor(R.color.black))
                        binding.rl2.setBackgroundResource(R.drawable.bg_backgroud_until_blue)
                        binding.tvPhone.setTextColor(resources.getColor(R.color.white))

                    }
                    1 -> {
                        binding.rl1.setOnClickListener {
                            binding.viewPager2.currentItem = position - 1
                            binding.rl1.setBackgroundResource(R.drawable.bg_backgroud_until)
                            binding.tvApp.setTextColor(resources.getColor(R.color.black))
                            binding.rl2.setBackgroundResource(R.drawable.bg_backgroud_until_blue)
                            binding.tvPhone.setTextColor(resources.getColor(R.color.white))


                        }

                        binding.rl2.setBackgroundResource(R.drawable.bg_backgroud_until)
                        binding.tvPhone.setTextColor(resources.getColor(R.color.black))
                        binding.rl1.setBackgroundResource(R.drawable.bg_backgroud_until_blue)
                        binding.tvApp.setTextColor(resources.getColor(R.color.white))

                    }
                }
            }
        })
    }

    private fun setListener(){
        binding.imgBack.setOnClickListener {
            finish()
        }
    }
}