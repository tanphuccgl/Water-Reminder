package com.example.waterreminder.ui.reminder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.example.waterreminder.R
import com.example.waterreminder.databinding.LayoutReminder2Binding
import com.example.waterreminder.helper.PreferenceHelper
import com.example.waterreminder.helper.PreferenceHelper.Companion.PREF_UNIT_WEIGHT
import com.example.waterreminder.helper.PreferenceHelper.Companion.PREF_WEIGHT
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject
@AndroidEntryPoint
class Reminder2Fragment : Fragment() {

    @Inject
    lateinit var preferenceHelper: PreferenceHelper

    private lateinit var binding: LayoutReminder2Binding
    companion object {
        const val REQUEST_CODE_GENDER = 101
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutReminder2Binding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private val utils: Array<String> = arrayOf("kg", "lbs")

    fun initView() {
        setFragmentResultListener(REQUEST_CODE_GENDER.toString()) { _, result ->
            // Xử lý kết quả khi quay lại từ Fragment trước đó
            val selectedGender = result.getString("selectedGender")
            if (selectedGender == "Male") {
                binding.imageWeight.setImageResource(R.drawable.ic_weight_male)
            } else {
                binding.imageWeight.setImageResource(R.drawable.ic_weight_female)
            }
            // pass data sent Reminder3Fragment
            val resultToFragmentC = bundleOf("selectedGender" to selectedGender)
            parentFragmentManager.setFragmentResult(Reminder3Fragment.REQUEST_CODE_GENDER.toString(), resultToFragmentC)
        }

        var weight: Int = preferenceHelper.getInt(PREF_WEIGHT).takeIf { it != 0 } ?: 40
        var unitWeight: String = preferenceHelper.getString(PREF_UNIT_WEIGHT)
            ?.takeIf { it.isNotBlank() } ?: "kg"

        // pick number
        binding.number1.minValue = 20
        binding.number1.maxValue = 300
        binding.number1.value = 40

        binding.number2.minValue = 0
        binding.number2.maxValue = utils.size -1
        binding.number2.displayedValues = utils

        binding.number2.setOnValueChangedListener { _, _, newVal ->
            unitWeight = utils[newVal]
            if (unitWeight == "lbs") {
                // Cập nhật dữ liệu cho pick number 1 tương ứng với lbs
                binding.number1.minValue = 44
                binding.number1.maxValue = 660
                binding.number1.value = 88
            } else {
                // Cập nhật dữ liệu cho pick number 1 tương ứng với kg
                binding.number1.minValue = 20
                binding.number1.maxValue = 300
                binding.number1.value = 40
            }

            preferenceHelper.setString(PREF_UNIT_WEIGHT,unitWeight)
        }
        binding.number1.setOnValueChangedListener { _, _, newVal ->
            weight = newVal
            preferenceHelper.setInt(PREF_WEIGHT,weight)
        }

        preferenceHelper.setString(PREF_UNIT_WEIGHT,unitWeight)
        preferenceHelper.setInt(PREF_WEIGHT,weight)
    }


}
