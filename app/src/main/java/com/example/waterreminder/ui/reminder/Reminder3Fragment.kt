package com.example.waterreminder.ui.reminder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.example.waterreminder.R
import com.example.waterreminder.databinding.LayoutReminder3Binding
import com.example.waterreminder.helper.PreferenceHelper
import com.example.waterreminder.helper.PreferenceHelper.Companion.PREF_WAKE_UP_TIME_HOUR
import com.example.waterreminder.helper.PreferenceHelper.Companion.PREF_WAKE_UP_TIME_MINUTE
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@AndroidEntryPoint
class Reminder3Fragment: Fragment() {

    @Inject
    lateinit var preferenceHelper: PreferenceHelper

    private lateinit var binding: LayoutReminder3Binding

    companion object {
        const val REQUEST_CODE_GENDER = 102
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutReminder3Binding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }
     fun initView() {

         setFragmentResultListener(REQUEST_CODE_GENDER.toString()) { _, result ->
             // Xử lý kết quả khi quay lại từ Fragment trước đó
             val selectedGender = result.getString("selectedGender")
             if (selectedGender == "Male") {
                 binding.imageWakeUp.setImageResource(R.drawable.ic_wake_up_time_male)
             } else {
                 binding.imageWakeUp.setImageResource(R.drawable.ic_wake_up_time_female)
             }
             // pass data sent Reminder3Fragment
             val resultToFragmentC = bundleOf("selectedGender" to selectedGender)
             parentFragmentManager.setFragmentResult(Reminder4Fragment.REQUEST_CODE_GENDER.toString(), resultToFragmentC)
         }

         var wakeUpTimeHour = preferenceHelper.getString(PREF_WAKE_UP_TIME_HOUR)
             ?.takeIf { it.isNotBlank() } ?: "06"
         var wakeUpTimeMinute = preferenceHelper.getString(PREF_WAKE_UP_TIME_MINUTE)
             ?.takeIf { it.isNotBlank() } ?: "00"
       // pick number
        binding.number1.minValue = 1
        binding.number1.maxValue = 24
        binding.number1.value = 6



        val displayedValues = Array(24) { i -> String.format("%02d", i + 1) } // Tạo mảng chuỗi từ 01 đến 24
        binding.number1.displayedValues = displayedValues

        binding.number1.setOnValueChangedListener { _, _, newVal ->
            wakeUpTimeHour = displayedValues[newVal - 1]
            // Thực hiện các thao tác khác với giá trị đã chọn
            preferenceHelper.setString(PREF_WAKE_UP_TIME_HOUR, wakeUpTimeHour)

        }

        binding.number2.minValue = 0
        binding.number2.maxValue = 59
        binding.number2.value = 0



        val displayedValues2 = Array(60) { i -> String.format("%02d",i) } // Tạo mảng chuỗi từ 01 đến 24
        binding.number2.displayedValues = displayedValues2

        binding.number2.setOnValueChangedListener { _, _, newVal ->
            wakeUpTimeMinute = displayedValues2[newVal]
            preferenceHelper.setString(PREF_WAKE_UP_TIME_MINUTE, wakeUpTimeMinute)
        }

         preferenceHelper.setString(PREF_WAKE_UP_TIME_HOUR, wakeUpTimeHour)
         preferenceHelper.setString(PREF_WAKE_UP_TIME_MINUTE, wakeUpTimeMinute)

    }

}