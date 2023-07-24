package com.example.waterreminder.ui.reminder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.waterreminder.R
import com.example.waterreminder.base.BaseActivity
import com.example.waterreminder.base.BaseFragment
import com.example.waterreminder.databinding.LayoutReminder1Binding
import com.example.waterreminder.helper.PreferenceHelper
import com.example.waterreminder.helper.PreferenceHelper.Companion.PREF_IS_CHECK
import com.example.waterreminder.helper.PreferenceHelper.Companion.PREF_SELECTED_GENDER
import com.example.waterreminder.ui.reminder.Reminder2Fragment.Companion.REQUEST_CODE_GENDER
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject
@AndroidEntryPoint
class Reminder1Fragment : Fragment() {

    @Inject
    lateinit var preferenceHelper: PreferenceHelper

    private lateinit var binding: LayoutReminder1Binding

    private var selectedGender: String = ""
    private var isRemindersGenerated = true



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutReminder1Binding.inflate(inflater, container, false)
        initView()
        return binding.root
    }



    fun initView() {
         selectedGender = preferenceHelper.getString(PREF_SELECTED_GENDER)
            ?.takeIf { it.isNotEmpty() } ?: "Male"
        // click image
        binding.imageBoy.setOnClickListener {
            selectedGender = "Male"
            binding.imageBoy.setImageResource(R.drawable.ic_male_selected)
            binding.tvMade.setTextColor(ContextCompat.getColor(requireActivity(), R.color.teal_700))

            binding.imageGirl.setImageResource(R.drawable.ic_female)
            binding.tvFemade.setTextColor(ContextCompat.getColor(requireActivity(),R.color.text_color2))

            preferenceHelper.setString(PREF_SELECTED_GENDER, selectedGender)

            val result = bundleOf("selectedGender" to selectedGender)
            parentFragmentManager.setFragmentResult(REQUEST_CODE_GENDER.toString(), result)

        }

        binding.imageGirl.setOnClickListener {
            selectedGender = "Female"
            binding.imageGirl.setImageResource(R.drawable.ic_female_selected)
            binding.tvFemade.setTextColor(ContextCompat.getColor(requireActivity(), R.color.teal_700))

            binding.imageBoy.setImageResource(R.drawable.ic_male)
            binding.tvMade.setTextColor(ContextCompat.getColor(requireActivity(),R.color.text_color2))

            preferenceHelper.setString(PREF_SELECTED_GENDER, selectedGender)

            val result = bundleOf("selectedGender" to selectedGender)
            parentFragmentManager.setFragmentResult(REQUEST_CODE_GENDER.toString(), result)
        }

        preferenceHelper.setString(PREF_SELECTED_GENDER, selectedGender)
        val result = bundleOf("selectedGender" to selectedGender)
        parentFragmentManager.setFragmentResult(REQUEST_CODE_GENDER.toString(), result)

        preferenceHelper.setBoolean(PREF_IS_CHECK,isRemindersGenerated)

    }


}