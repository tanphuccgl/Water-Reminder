package com.example.waterreminder.ui.composite_screen.settings

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.waterreminder.R
import com.example.waterreminder.databinding.LayoutDialogGenderBinding
import com.example.waterreminder.databinding.LayoutDialogIntakeBinding
import com.example.waterreminder.databinding.LayoutDialogModeBinding
import com.example.waterreminder.databinding.LayoutDialogUntilBinding
import com.example.waterreminder.databinding.LayoutDialogWeightBinding
import com.example.waterreminder.databinding.LayoutSettingBinding
import com.example.waterreminder.helper.PreferenceHelper
import com.example.waterreminder.helper.PreferenceHelper.Companion.PREF_CAPACITY
import com.example.waterreminder.helper.PreferenceHelper.Companion.PREF_SELECTED_GENDER
import com.example.waterreminder.helper.PreferenceHelper.Companion.PREF_SLEEP_TIME_HOUR
import com.example.waterreminder.helper.PreferenceHelper.Companion.PREF_SLEEP_TIME_MINUTE
import com.example.waterreminder.helper.PreferenceHelper.Companion.PREF_UNIT_WEIGHT
import com.example.waterreminder.helper.PreferenceHelper.Companion.PREF_WAKE_UP_TIME_HOUR
import com.example.waterreminder.helper.PreferenceHelper.Companion.PREF_WAKE_UP_TIME_MINUTE
import com.example.waterreminder.helper.PreferenceHelper.Companion.PREF_WEIGHT
import com.example.waterreminder.ui.composite_screen.settings.language.LanguageSettingActivity
import com.example.waterreminder.ui.composite_screen.settings.schedule.ScheduleSettingActivity
import com.example.waterreminder.ui.composite_screen.settings.sound.SoundSettingActivity
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.TimeZone
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment(){

    @Inject
    lateinit var preferenceHelper: PreferenceHelper

    private lateinit var binding: LayoutSettingBinding

    private val calendar = Calendar.getInstance()

    private lateinit var gender: String
    private var weight: Int = 0
    private lateinit var unitWeight: String
    private lateinit var wakeUpTimeHour: String
    private lateinit var wakeUpTimeMinute: String
    private lateinit var sleepTimeHour: String
    private lateinit var sleepTimeMinute: String
    private  var dailyWater: Int = 0
    private  var selectedWeightUnit: String = "kg"
    private  var selectedCapacity: String = "ml"
    private lateinit var selectedIntake: String
    private lateinit var selectedGender: String


    private lateinit var dialogUntilBinding: LayoutDialogUntilBinding
    private lateinit var dialogIntakeBinding: LayoutDialogIntakeBinding
    private lateinit var dialogGenderBinding: LayoutDialogGenderBinding
    private lateinit var dialogWeightBinding: LayoutDialogWeightBinding
    private lateinit var dialogModeBinding: LayoutDialogModeBinding

    companion object {
        private const val REQUEST_CODE_LANGUAGE = 100
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutSettingBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        setListeners()
    }

    @SuppressLint("SetTextI18n") //bỏ qua cảnh báo liên quan đến việc sử dụng setText()
    private fun initUI() {
        //use
        gender = preferenceHelper.getString(PREF_SELECTED_GENDER).toString()
        weight = preferenceHelper.getInt(PREF_WEIGHT)
        unitWeight = preferenceHelper.getString(PREF_UNIT_WEIGHT).toString()
        wakeUpTimeHour = preferenceHelper.getString(PREF_WAKE_UP_TIME_HOUR).toString()
        wakeUpTimeMinute = preferenceHelper.getString(PREF_WAKE_UP_TIME_MINUTE).toString()
        sleepTimeHour = preferenceHelper.getString(PREF_SLEEP_TIME_HOUR).toString()
        sleepTimeMinute = preferenceHelper.getString(PREF_SLEEP_TIME_MINUTE).toString()
        dailyWater = preferenceHelper.getInt(PreferenceHelper.PREF_DAILY_WATER)

    }

    @SuppressLint("SetTextI18n")
    private fun setListeners(){
        binding.tvGender.text = gender
        binding.tvWeight.text = "$weight $unitWeight"

        if (wakeUpTimeHour.toInt() > 12) {
            val formattedHour = (wakeUpTimeHour.toInt() - 12).toString().padStart(2, '0')
            binding.tvWakeupTime.text = "$formattedHour:$wakeUpTimeMinute PM"
        } else {
            binding.tvWakeupTime.text = "$wakeUpTimeHour:$wakeUpTimeMinute AM"
        }

        if (sleepTimeHour.toInt() > 12) {
            val formattedHour = (sleepTimeHour.toInt() - 12).toString().padStart(2, '0')
            binding.tvBedTime.text = "$formattedHour:$sleepTimeMinute PM"
        } else {
            binding.tvBedTime.text = "$sleepTimeHour:$sleepTimeMinute AM"
        }

        binding.tvUnit.setOnClickListener {
            showDialogBoxUnit()
        }
        binding.tvIntake.setOnClickListener {
            showDialogBoxIntake()
        }

        binding.tvGender.setOnClickListener {
            showDialogBoxGender()
        }

        binding.tvWeight.setOnClickListener {
            showDialogBoxWeight()
        }

        binding.tvWakeupTime.setOnClickListener {
            showDialogWakeupTime()
        }

        binding.tvBedTime.setOnClickListener {
            showDialogBedTime()
        }

        binding.tvLanguage.setOnClickListener {
            val intent = Intent(requireContext(),LanguageSettingActivity::class.java)
            startActivityForResult(intent,REQUEST_CODE_LANGUAGE)
        }

        binding.tvSound.setOnClickListener {
            startActivity(Intent(requireContext(),SoundSettingActivity::class.java))
        }

        binding.tvSchedule.setOnClickListener {
            startActivity(Intent(requireContext(),ScheduleSettingActivity::class.java))
        }

        binding.tvMode.setOnClickListener {
         showDialogMode()
        }
    }

    private fun showDialogMode(){
        dialogModeBinding = LayoutDialogModeBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(dialogModeBinding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        // Thiết lập kích thước của Dialog
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window?.attributes)
        val widthInDp = 300
        val widthInPx = (widthInDp * (context?.resources?.displayMetrics?.density ?: 0f)).toInt()
        layoutParams.width = widthInPx
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        // Áp dụng các thay đổi vào Dialog
        dialog.window?.attributes = layoutParams
        // onclick mode
        onClickMode(dialog)

        dialog.show()
    }

    private fun onClickMode(dialog: Dialog) {

        dialogModeBinding.tvOk.setOnClickListener {
            binding.tvAuto.text
            dialog.dismiss()
        }
        dialogModeBinding.tvCancel.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun showDialogBedTime() {
        val timePicker = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->

            calendar.set(Calendar.HOUR_OF_DAY,hourOfDay)
            calendar.set(Calendar.MINUTE,minute)
            calendar.timeZone = TimeZone.getDefault()

            updateTimeLabelBed(calendar)
        }

        binding.tvBedTime.setOnClickListener {
            TimePickerDialog(requireContext(),timePicker,calendar.get(Calendar.HOUR_OF_DAY)
                ,calendar.get(Calendar.MINUTE),false).show()
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun updateTimeLabelBed(calendar: Calendar) {
        val format = SimpleDateFormat("hh:mm aa")
        val time = format.format(calendar.time)
        binding.tvBedTime.text = time
    }

    private fun showDialogWakeupTime() {
        val initialHour = wakeUpTimeHour.toInt()
        val initialMinute = wakeUpTimeMinute.toInt()

        val timePicker = TimePickerDialog.OnTimeSetListener { timePicker, hourOfDay, minute ->

            calendar.set(Calendar.HOUR_OF_DAY,hourOfDay)
            calendar.set(Calendar.MINUTE,minute)
            calendar.timeZone = TimeZone.getDefault()

            updateTimeLabel(calendar)
        }

        binding.tvWakeupTime.setOnClickListener {
            TimePickerDialog(requireContext(),timePicker,initialHour
                ,initialMinute,false).show()
        }

    }

    @SuppressLint("SimpleDateFormat")
    private fun updateTimeLabel(calendar: Calendar) {
        val format = SimpleDateFormat("hh:mm aa")
        val time = format.format(calendar.time)
        binding.tvWakeupTime.text = time
    }

    private fun showDialogBoxWeight() {
        dialogWeightBinding = LayoutDialogWeightBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(dialogWeightBinding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        // Thiết lập kích thước của Dialog
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window?.attributes)
        val widthInDp = 300
        val widthInPx = (widthInDp * (context?.resources?.displayMetrics?.density ?: 0f)).toInt()
        layoutParams.width = widthInPx
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        // Áp dụng các thay đổi vào Dialog
        dialog.window?.attributes = layoutParams
        // onclick Weight
        onClickWeight(dialog)

        dialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun onClickWeight(dialog: Dialog) {

        // lắng nghe sự kiện click của unit rồi đổi giá trị

        if (unitWeight == "kg"){
            dialogWeightBinding.number1.minValue = 20
            dialogWeightBinding.number1.maxValue = 300
            dialogWeightBinding.number1.value = weight

            dialogWeightBinding.tvUntil.text = unitWeight
        }else {
            dialogWeightBinding.number1.minValue = 44
            dialogWeightBinding.number1.maxValue = 660
            dialogWeightBinding.number1.value = weight

            dialogWeightBinding.tvUntil.text = unitWeight
        }

        dialogWeightBinding.number1.setOnValueChangedListener{ _, _, newVal ->
            weight = newVal
        }

        dialogWeightBinding.tvOk.setOnClickListener {
            binding.tvWeight.text = "$weight $unitWeight"
            dialog.dismiss()
        }

        dialogWeightBinding.tvCancel.setOnClickListener {
            dialog.dismiss()
        }

    }

    private fun showDialogBoxGender() {
        dialogGenderBinding = LayoutDialogGenderBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(dialogGenderBinding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        // Thiết lập kích thước của Dialog
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window?.attributes)
        val widthInDp = 300
        val widthInPx = (widthInDp * (context?.resources?.displayMetrics?.density ?: 0f)).toInt()
        layoutParams.width = widthInPx
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        // Áp dụng các thay đổi vào Dialog
        dialog.window?.attributes = layoutParams
        // onclick Gender
        onClickGender(dialog)

        dialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun onClickGender(dialog: Dialog) {
        if(gender == dialogGenderBinding.radioFemale.text){
            dialogGenderBinding.radioFemale.isChecked = true
        }else{
            dialogGenderBinding.radioMale.isChecked = true
        }

        dialogGenderBinding.radioFemale.setOnClickListener {
            dialogGenderBinding.radioFemale.isChecked = true
            dialogGenderBinding.radioMale.isChecked = false

            selectedGender = dialogGenderBinding.radioFemale.text as String
        }

        dialogGenderBinding.radioMale.setOnClickListener {
            dialogGenderBinding.radioFemale.isChecked = false
            dialogGenderBinding.radioMale.isChecked = true

            selectedGender = dialogGenderBinding.radioMale.text as String
        }

        dialogGenderBinding.tvOk.setOnClickListener {
            binding.tvGender.text = selectedGender
            preferenceHelper.setString(PREF_SELECTED_GENDER,selectedGender)
            dialog.dismiss()
        }

        dialogGenderBinding.tvCancel.setOnClickListener {
            dialog.dismiss()
        }


    }

    private fun showDialogBoxIntake() {
        dialogIntakeBinding = LayoutDialogIntakeBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(dialogIntakeBinding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        // Thiết lập kích thước của Dialog
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window?.attributes)
        val widthInDp = 300
        val widthInPx = (widthInDp * (context?.resources?.displayMetrics?.density ?: 0f)).toInt()
        layoutParams.width = widthInPx
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        // Áp dụng các thay đổi vào Dialog
        dialog.window?.attributes = layoutParams
        // onclick intake
        onClickIntake(dialog)
        dialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun onClickIntake(dialog: Dialog) {
        if(dailyWater > 0){
            dialogIntakeBinding.tvQuantity.text = "$dailyWater"
            dialogIntakeBinding.seekBar.progress = dailyWater
        }

        dialogIntakeBinding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                dialogIntakeBinding.tvQuantity.text = "$progress"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
        selectedIntake = dialogIntakeBinding.tvQuantity.text as String

        dialogIntakeBinding.imgReset.setOnClickListener {
            dialogIntakeBinding.tvQuantity.text = "$dailyWater"
            dialogIntakeBinding.seekBar.progress = dailyWater
        }

        dialogIntakeBinding.tvOk.setOnClickListener {
            binding.tvIntake.text = "$selectedIntake ml"
            preferenceHelper.setInt(PreferenceHelper.PREF_DAILY_WATER,selectedIntake.toInt())
            dialog.dismiss()
        }

        dialogIntakeBinding.tvCancel.setOnClickListener {
            dialog.dismiss()
        }


    }

    private fun showDialogBoxUnit() {
        dialogUntilBinding = LayoutDialogUntilBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(dialogUntilBinding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        // Thiết lập kích thước của Dialog
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window?.attributes)
        val widthInDp = 300
        val widthInPx = (widthInDp * (context?.resources?.displayMetrics?.density ?: 0f)).toInt()
        layoutParams.width = widthInPx
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        // Áp dụng các thay đổi vào Dialog
        dialog.window?.attributes = layoutParams
       // onclick unit
        onClickUnit(dialog)
        preferenceHelper.setString(PREF_UNIT_WEIGHT,selectedWeightUnit)
        preferenceHelper.setString(PREF_CAPACITY,selectedCapacity)
        dialog.show()

    }

    @SuppressLint("SetTextI18n")
    private fun onClickUnit(dialog: Dialog) {
        dialogUntilBinding.rlWeightKg.setOnClickListener {
            dialogUntilBinding.tvKg.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            dialogUntilBinding.rlWeightKg.setBackgroundResource(R.drawable.bg_backgroud_until)
            dialogUntilBinding.tvIbs.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            dialogUntilBinding.rlWeightIbs.setBackgroundResource(R.drawable.bg_backgroud_until_blue)

            selectedWeightUnit = dialogUntilBinding.tvKg.text as String

        }
        dialogUntilBinding.rlWeightIbs.setOnClickListener {
            dialogUntilBinding.tvIbs.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            dialogUntilBinding.rlWeightKg.setBackgroundResource(R.drawable.bg_backgroud_until)
            dialogUntilBinding.tvKg.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            dialogUntilBinding.rlWeightKg.setBackgroundResource(R.drawable.bg_backgroud_until_blue)

            selectedWeightUnit = dialogUntilBinding.tvIbs.text as String
        }
        dialogUntilBinding.rlMl.setOnClickListener {
            dialogUntilBinding.tvMl.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            dialogUntilBinding.rlMl.setBackgroundResource(R.drawable.bg_backgroud_until)
            dialogUntilBinding.tvOz.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            dialogUntilBinding.rlOz.setBackgroundResource(R.drawable.bg_backgroud_until_blue)

            selectedCapacity = dialogUntilBinding.tvMl.text as String

        }
        dialogUntilBinding.rlMl.setOnClickListener {
            dialogUntilBinding.tvOz.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            dialogUntilBinding.rlOz.setBackgroundResource(R.drawable.bg_backgroud_until)
            dialogUntilBinding.tvMl.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            dialogUntilBinding.rlMl.setBackgroundResource(R.drawable.bg_backgroud_until_blue)

            selectedCapacity = dialogUntilBinding.tvOz.text as String
        }

        dialogUntilBinding.tvOk.setOnClickListener {
            binding.tvUnit.text = "$selectedWeightUnit, $selectedCapacity"
            dialog.dismiss()
        }

        dialogUntilBinding.tvCancel.setOnClickListener {
            dialog.dismiss()
        }

    }
}