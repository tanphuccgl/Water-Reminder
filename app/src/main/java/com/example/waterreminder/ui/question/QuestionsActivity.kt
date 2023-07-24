package com.example.waterreminder.ui.question

import android.content.Intent
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.viewpager2.widget.ViewPager2
import com.example.waterreminder.R
import com.example.waterreminder.base.BaseActivity
import com.example.waterreminder.databinding.ActivityQuestionsBinding
import com.example.waterreminder.ui.dailywater.DailyWaterGoalActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuestionsActivity : BaseActivity<ActivityQuestionsBinding>() {

    var dots:Array<ImageView>? = null
    private var questionsAdapter : QuestionsAdapter? = null

    override fun initView() {
        initViewPager()

    }


    override fun setBinding(layoutInflater: LayoutInflater): ActivityQuestionsBinding {
       return ActivityQuestionsBinding.inflate(layoutInflater)
    }

    private fun initViewPager() {
        dots = arrayOf(
            binding.cricle1, binding.cricle2, binding.cricle3, binding.cricle4
        )


        questionsAdapter = QuestionsAdapter(this)
        binding.viewPager.adapter = questionsAdapter
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> {

                        binding.btnNext.setOnClickListener {
                            binding.viewPager.currentItem = position + 1
                        }
                        binding.btnBack.setOnClickListener {
                            finish()
                        }
                    }

                    1 -> {

                        binding.btnNext.setOnClickListener {
                            binding.viewPager.currentItem = position + 1
                        }
                        binding.btnBack.setOnClickListener {
                            binding.viewPager.currentItem = position - 1
                        }
                    }

                    2 -> {

                        binding.btnNext.setOnClickListener {
                            binding.viewPager.currentItem = position + 1
                        }
                        binding.btnBack.setOnClickListener {
                            binding.viewPager.currentItem = position - 1
                        }
                    }

                    3 -> {
                        binding.btnNext.setOnClickListener {
                            startActivity(Intent(this@QuestionsActivity, DailyWaterGoalActivity::class.java))
                        }
                        binding.btnBack.setOnClickListener {
                            binding.viewPager.currentItem = position - 1
                        }
                    }
                }
                changeContentInit(position)
            }
        })
    }



    private fun changeContentInit(position: Int) {
        for (i in 0..3) {
            if (i == position) dots!![i].setImageResource(R.drawable.ic_dot_selected) else dots!![i].setImageResource(
                R.drawable.ic_dot_not_select
            )
        }
    }


}