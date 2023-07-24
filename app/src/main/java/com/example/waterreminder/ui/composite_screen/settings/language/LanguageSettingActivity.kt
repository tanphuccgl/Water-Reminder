package com.example.waterreminder.ui.composite_screen.settings.language

import android.view.LayoutInflater
import androidx.lifecycle.MutableLiveData
import com.example.waterreminder.base.BaseActivity
import com.example.waterreminder.databinding.ActivityLanguageSettingBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LanguageSettingActivity : BaseActivity<ActivityLanguageSettingBinding>(){

    @Inject
    lateinit var adapter: LanguageSettingAdapter
    private val languageLiveData = MutableLiveData<List<LanguageSettingModel>>()

    override fun initView() {
        initAdapter()
        setListener()
    }


    override fun setBinding(layoutInflater: LayoutInflater): ActivityLanguageSettingBinding {
        return ActivityLanguageSettingBinding.inflate(layoutInflater)
    }

    private fun initAdapter() {
        languageLiveData.value = mutableListOf(
            LanguageSettingModel("English"),
            LanguageSettingModel("Portuguese"),
            LanguageSettingModel("French"),
            LanguageSettingModel("Vietnamese"),
            LanguageSettingModel("China")
        )

        binding.rcvLanguage.adapter = adapter

        languageLiveData.observe(this){languageLiveData ->
            adapter.submitList(languageLiveData)
        }


    }

    private fun setListener(){
        binding.imgBack.setOnClickListener {
            finish()
        }
    }


}

