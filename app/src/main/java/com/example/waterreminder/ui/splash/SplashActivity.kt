package com.example.waterreminder.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import com.example.waterreminder.base.BaseActivity
import com.example.waterreminder.databinding.ActivitySplashBinding
import com.example.waterreminder.extensions.setLocale
import com.example.waterreminder.helper.PreferenceHelper
import com.example.waterreminder.ui.introduce.IntroduceActivity
import com.example.waterreminder.ui.language.LanguageActivity
import com.example.waterreminder.ui.question.QuestionsActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    @Inject
    lateinit var preferenceHelper: PreferenceHelper

    private var currentProgress  = 0

    override fun initView() {
        val local = preferenceHelper.getString(PreferenceHelper.PREF_CURRENT_LANGUAGE) ?: "en"
        setLocale(local)
        loadProgressbar()

    }

    private fun loadProgressbar(){
        Thread(Runnable {
            while (currentProgress  < 100) {
                currentProgress  += 10
                binding.loadFile.progress = currentProgress
                binding.loadFile.max = 100
                try {
                    Thread.sleep(300)

                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
            startNextAct()
        }).start()
    }

    override fun setBinding(layoutInflater: LayoutInflater): ActivitySplashBinding {
       return ActivitySplashBinding.inflate(layoutInflater)
    }

    private fun startNextAct() {
        if (preferenceHelper.getBoolean(PreferenceHelper.PREF_FIRST_APP_OPENED) == true) {
            navigateTo(QuestionsActivity::class.java)
        } else if (preferenceHelper.getBoolean(PreferenceHelper.PREF_SHOWED_START_LANGUAGE) == true) {
            startActivity(
                IntroduceActivity.newIntent(this)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            )
        } else {
            startActivity(
                LanguageActivity.newIntent(this, true)
            )
        }
        finish()
    }
}