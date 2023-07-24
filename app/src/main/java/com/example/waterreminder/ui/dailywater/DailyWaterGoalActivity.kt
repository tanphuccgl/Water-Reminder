package com.example.waterreminder.ui.dailywater

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.viewpager2.widget.ViewPager2
import com.example.waterreminder.R
import com.example.waterreminder.base.BaseActivity
import com.example.waterreminder.databinding.ActivityDailyWaterGoalBinding
import com.example.waterreminder.databinding.ActivityIntroduceBinding
import com.example.waterreminder.ui.composite_screen.CompositeScreenActivity
import com.example.waterreminder.ui.dailywater.dailywaterfragment.DrinkMuchWaterFragment
import com.example.waterreminder.ui.question.QuestionsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DailyWaterGoalActivity : BaseActivity<ActivityDailyWaterGoalBinding>(){

    private var dailyWaterAdapter : DailyWaterAdapter? = null

    override fun initView() {
        initUI()
    }

    private fun initUI() {
        initViewPager()
    }

    override fun setBinding(layoutInflater: LayoutInflater): ActivityDailyWaterGoalBinding {
        return ActivityDailyWaterGoalBinding.inflate(layoutInflater)
    }

    private fun initViewPager() {

        dailyWaterAdapter = DailyWaterAdapter(this)
        binding.viewPager2.adapter = dailyWaterAdapter
        binding.viewPager2.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position){
                    0 -> {
                        binding.tvNext.setOnClickListener {
                            binding.viewPager2.currentItem = position +1
                        }
                        binding.tvPage.text = "1"
                    }
                    1 -> {
                        binding.tvNext.setOnClickListener {
                            startActivity(Intent(this@DailyWaterGoalActivity, CompositeScreenActivity::class.java))
                        }
                        binding.tvPage.text = "2"
                    }
                }
            }
        })

    }

}