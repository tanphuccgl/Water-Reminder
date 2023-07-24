package com.example.waterreminder.ui.introduce

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import com.example.waterreminder.base.BaseActivity
import com.example.waterreminder.databinding.ActivityIntroduceBinding
import com.example.waterreminder.helper.PreferenceHelper
import com.example.waterreminder.ui.composite_screen.CompositeScreenActivity
import com.example.waterreminder.ui.question.QuestionsActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class IntroduceActivity :  BaseActivity<ActivityIntroduceBinding>() {

    @Inject
    lateinit var preferenceHelper: PreferenceHelper

    override fun initView() {
        initUI()

    }

    @SuppressLint("SetTextI18n")
    private fun initUI() {
        binding.textView.text = "Hello,\nwellcome to your smart\nwater reminder"
        binding.textView2.text = "Here comes few simple question\nbefore we can personalize your daily\ngoal and schedule"

        val gender = preferenceHelper.getString(PreferenceHelper.PREF_SELECTED_GENDER)
        val unitWeight = preferenceHelper.getString(PreferenceHelper.PREF_UNIT_WEIGHT)
        val wakeUpTimeHour = preferenceHelper.getString(PreferenceHelper.PREF_WAKE_UP_TIME_HOUR)
        val sleepTimeHour = preferenceHelper.getString(PreferenceHelper.PREF_SLEEP_TIME_HOUR)


        binding.button.setOnClickListener {
            if(gender != null && unitWeight != null && gender.isNotEmpty() && unitWeight.isNotEmpty() && wakeUpTimeHour != null && sleepTimeHour != null && wakeUpTimeHour.isNotEmpty() && sleepTimeHour.isNotEmpty()){
                startActivity(Intent(this,CompositeScreenActivity::class.java))
            }else {
                startActivity(Intent(this, QuestionsActivity::class.java))
            }
        }
    }

    override fun setBinding(layoutInflater: LayoutInflater): ActivityIntroduceBinding {
        return ActivityIntroduceBinding.inflate(layoutInflater)
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, IntroduceActivity::class.java)
        }
    }
}