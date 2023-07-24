package com.example.waterreminder.ui.language

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.example.waterreminder.MainActivity
import com.example.waterreminder.R
import com.example.waterreminder.base.BaseActivity
import com.example.waterreminder.databinding.ActivityLanguageBinding
import com.example.waterreminder.extensions.setLocale
import com.example.waterreminder.helper.PreferenceHelper
import com.example.waterreminder.helper.PreferenceHelper.Companion.PREF_SHOWED_START_LANGUAGE
import com.example.waterreminder.ui.introduce.IntroduceActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class LanguageActivity :  BaseActivity<ActivityLanguageBinding>() {
    @Inject
    lateinit var preferenceHelper: PreferenceHelper

    @Inject
    lateinit var languageAdapter: LanguageAdapter

    private val languages = mutableListOf<LanguageModel>()

    private var fromSplash = false
    override fun setBinding(layoutInflater: LayoutInflater): ActivityLanguageBinding {
        return ActivityLanguageBinding.inflate(layoutInflater)
    }

    override fun initView() {
        initData()
        setupListLanguage()
        initEvent()
    }

    private fun initData() {
        languages.add(LanguageModel("English", "en"))
        languages.add(LanguageModel("Hindi", "hi"))
        languages.add(LanguageModel("China", "zh"))
        languages.add(LanguageModel("Spanish", "es"))
        languages.add(LanguageModel("French", "fr"))
        languages.add(LanguageModel("Portuguese", "pt"))

    }

    private fun initListLanguage(selectedLanguage: String) {
        if(languages.any{it.code == selectedLanguage}) {
            languages.first { it.code == selectedLanguage }.active = true
        } else{
            languages.first { it.code == "en" }.active = true
        }
        binding.lvLanguage.adapter = languageAdapter
        languageAdapter.submitList(languages)
    }

    private fun setupListLanguage() {
        fromSplash = intent.getBooleanExtra(FROM_SPLASH, false)
        if (fromSplash) {
            val locale = Locale.getDefault()
            initListLanguage(locale.language)
        } else {
            val selectedLanguage =
                preferenceHelper.getString(PreferenceHelper.PREF_CURRENT_LANGUAGE)
            initListLanguage(selectedLanguage ?: "")
        }
    }

    private fun initEvent() {
        binding.btnDone.setOnClickListener {
            val selectedLanguage = languageAdapter.getSelectedLanguage()
            if (selectedLanguage == null) {
                Toast.makeText(this, "Please select language", Toast.LENGTH_SHORT).show()
            } else {
                setLocale(selectedLanguage.code)
                preferenceHelper.setString(
                    PreferenceHelper.PREF_CURRENT_LANGUAGE,
                    selectedLanguage.code
                )
                if (fromSplash) {
                    preferenceHelper.setBoolean(PREF_SHOWED_START_LANGUAGE, true)
                    startActivity(
                        IntroduceActivity.newIntent(this).addFlags(
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        )
                    )
                } else {
                    val intent = MainActivity.newIntent(this)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finishAffinity()
                }
            }
        }
    }

   companion object {
        const val FROM_SPLASH = "from_splash"
        fun newIntent(context: Context, fromSplash: Boolean = false): Intent {
            val intent = Intent(context, LanguageActivity::class.java)
            intent.putExtra(FROM_SPLASH, fromSplash)
            if (fromSplash) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            return intent
        }

    }


}