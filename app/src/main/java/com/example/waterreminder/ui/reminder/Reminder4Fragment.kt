package com.example.waterreminder.ui.reminder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.example.waterreminder.R
import com.example.waterreminder.databinding.LayoutReminder4Binding
import com.example.waterreminder.helper.PreferenceHelper
import com.example.waterreminder.helper.PreferenceHelper.Companion.PREF_SLEEP_TIME_HOUR
import com.example.waterreminder.helper.PreferenceHelper.Companion.PREF_SLEEP_TIME_MINUTE
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@AndroidEntryPoint
class Reminder4Fragment : Fragment() {

    @Inject
    lateinit var preferenceHelper: PreferenceHelper

    private lateinit var binding: LayoutReminder4Binding

    companion object {
        const val REQUEST_CODE_GENDER = 103
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutReminder4Binding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

     fun initView() {

         setFragmentResultListener(REQUEST_CODE_GENDER.toString()) { _, result ->
             val selectedGender = result.getString("selectedGender")
             if (selectedGender == "Male") {
                 binding.imgBed.setImageResource(R.drawable.ic_sleeping_time_male)
             } else {
                 binding.imgBed.setImageResource(R.drawable.ic_sleeping_time_female)
             }
         }

         var sleepTimeHour = preferenceHelper.getString(PREF_SLEEP_TIME_HOUR)
             ?.takeIf { it.isNotBlank() } ?: "22"
         var sleepTimeMinute = preferenceHelper.getString(PREF_SLEEP_TIME_MINUTE)
             ?.takeIf { it.isNotBlank() } ?: "00"
        // pick number
        binding.number1.minValue = 1
        binding.number1.maxValue = 24
        binding.number1.value = 22

        val displayedValues = Array(24) { i -> String.format("%02d", i + 1) } // Tạo mảng chuỗi từ 01 đến 24
        binding.number1.displayedValues = displayedValues

        binding.number1.setOnValueChangedListener { _, _, newVal ->
            sleepTimeHour = displayedValues[newVal - 1]
            // Thực hiện các thao tác khác với giá trị đã chọn
            preferenceHelper.setString(PREF_SLEEP_TIME_HOUR, sleepTimeHour)
        }

        binding.number2.minValue = 0
        binding.number2.maxValue = 59
        binding.number2.value = 0



        val displayedValues2 = Array(60) { i -> String.format("%02d",i) } // Tạo mảng chuỗi từ 01 đến 24
        binding.number2.displayedValues = displayedValues2

        binding.number2.setOnValueChangedListener { _, _, newVal ->
            sleepTimeMinute = displayedValues2[newVal]
            preferenceHelper.setString(PREF_SLEEP_TIME_MINUTE, sleepTimeMinute)
        }

         preferenceHelper.setString(PREF_SLEEP_TIME_HOUR, sleepTimeHour)
         preferenceHelper.setString(PREF_SLEEP_TIME_MINUTE, sleepTimeMinute)
    }


}