package com.example.waterreminder.ui.composite_screen.home

import android.annotation.SuppressLint
import android.app.Activity
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
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.waterreminder.R
import com.example.waterreminder.database.WaterReminderEntity
import com.example.waterreminder.databinding.LayoutDialogEditBinding
import com.example.waterreminder.databinding.LayoutHomeBinding
import com.example.waterreminder.database.cup.SwitchCup
import com.example.waterreminder.helper.PreferenceHelper
import com.example.waterreminder.ui.composite_screen.home.switchcup.SwitchCupActivity
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.TimeZone
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(){
    private val viewModel:HomeViewModel by viewModels()

    @Inject
    lateinit var preferenceHelper: PreferenceHelper
    @Inject
    lateinit var adapter: HomeAdapter

    private var selectedData: String? = null
    private var selectedImageDialog: Int? = null

    private val calendar = Calendar.getInstance()

    private lateinit var binding: LayoutHomeBinding

    private var listWaterReminderUnit = mutableListOf<WaterReminderEntity>()

    private lateinit var dialogMainBinding:LayoutDialogEditBinding

    private var selectedImageResId: Int? = null
    private var currentPercent : Int = 0
    private var sumQuantityDrinkWater : Int = 0
    private var date : String? = null
    private var intImage: Int? = null
    private var dailyWater:Int = 0
    private var title:String = ""
    companion object {
        private const val REQUEST_CODE = 100
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutHomeBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initChange()
        initView()
        observe()
        setListeners()

        // Khôi phục giá trị currentPercent từ preferenceHelper
        currentPercent = preferenceHelper.getInt(PreferenceHelper.PREF_CURRENT_PERCENT)
        binding.semiCircleArcProgressBar.setPercent(currentPercent)
        sumQuantityDrinkWater = preferenceHelper.getInt(PreferenceHelper.PREF_NUMBER_DRINK_WATER)
        binding.tvClickNumber.text = "$sumQuantityDrinkWater"
        if(currentPercent > 97){
            binding.imgHeart.setImageResource(R.drawable.ic_heart_selected)
        }else{
            binding.imgHeart.setImageResource(R.drawable.ic_drought)
        }

    }

    private fun observe() {
        viewModel.getWaterReminder()
        viewModel.listWaterReminder.observe(viewLifecycleOwner){
            initAdapter(it)
        }
    }
    private fun setListeners() {
        adapter.clickListener.onItemClick = {
            showDialogBox(it)
        }

        adapter.clickListener.onDeleteClick = {
            viewModel.deleteWaterReminder(it.checkedDate)
            listWaterReminderUnit.remove(it)
            val total = it.quantityWater.dropLast(2).trim()
            val sum = total.toInt()

            currentPercent -= (100/(dailyWater/sum)) - 1
            sumQuantityDrinkWater -= sum
            binding.tvClickNumber.text = "$sumQuantityDrinkWater"
            binding.semiCircleArcProgressBar.setPercent(currentPercent)


        }
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun showDialogBox(waterReminderEntity: WaterReminderEntity) {
        dialogMainBinding = LayoutDialogEditBinding.inflate(layoutInflater);

        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(dialogMainBinding.root)
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
        // change color
        changeColor()
        // dialog timepicker
        dialogTimePicker()

        dialogMainBinding.imgGlass.setImageResource(waterReminderEntity.image)
        dialogMainBinding.tvClock.text = waterReminderEntity.time

        val total = waterReminderEntity.quantityWater.dropLast(2).trim()
        val sum = total.toInt()

        dialogMainBinding.tv12.text = "${sum/4} ml"
        dialogMainBinding.tv22.text = "${sum*2/4} ml"
        dialogMainBinding.tv32.text = "${sum*3/4} ml"
        dialogMainBinding.tv42.text = "$sum ml"
        date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
        selectedImageDialog = waterReminderEntity.image



        // click ok
        dialogMainBinding.tvOk.setOnClickListener {
            val newWaterReminder = selectedImageDialog?.let { it1 ->
                WaterReminderEntity(it1, dialogMainBinding.tvClock.text as String,selectedData!!,waterReminderEntity.checkedDate)
            }
            if (newWaterReminder != null) {
                viewModel.updateWaterReminder(newWaterReminder)
            }
            if(selectedData != null &&!(selectedData.equals(waterReminderEntity.quantityWater))){
                val count = selectedData!!.dropLast(2).trim().toInt()
                sumQuantityDrinkWater -= (sum - count)
                currentPercent -= (100/(dailyWater/(sum-count))) - 1
                binding.semiCircleArcProgressBar.setPercent(currentPercent)

            }


            dialog.dismiss()
        }
        // click cancel
        dialogMainBinding.tvCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun dialogTimePicker() {
        
        val timePicker = TimePickerDialog.OnTimeSetListener { timePicker, hourOfDay, minute ->

            calendar.set(Calendar.HOUR_OF_DAY,hourOfDay)
            calendar.set(Calendar.MINUTE,minute)
            calendar.timeZone = TimeZone.getDefault()
            
            updateTimeLabel(calendar)
        }

        dialogMainBinding.tvClock.setOnClickListener {
            TimePickerDialog(requireContext(),timePicker,calendar.get(Calendar.HOUR_OF_DAY)
                ,calendar.get(Calendar.MINUTE),false).show()
        }


    }

    private fun updateTimeLabel(calendar: Calendar) {
        val format = SimpleDateFormat("hh:mm aa")
        val time = format.format(calendar.time)
        dialogMainBinding.tvClock.text = time
    }

    @SuppressLint("ResourceAsColor")
    private fun changeColor() {
        dialogMainBinding.imgClick.setOnClickListener {
            dialogMainBinding.imgClick.setImageResource(R.drawable.custom_circle_selected)
            dialogMainBinding.tv12.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
            dialogMainBinding.imgClick2.setImageResource(R.drawable.custom_circle)
            dialogMainBinding.tv22.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color2))
            dialogMainBinding.imgClick3.setImageResource(R.drawable.custom_circle)
            dialogMainBinding.tv32.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color2))
            dialogMainBinding.imgClick4.setImageResource(R.drawable.custom_circle)
            dialogMainBinding.tv42.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color2))
            selectedData = dialogMainBinding.tv12.text as String?

        }
        dialogMainBinding.imgClick2.setOnClickListener {
            dialogMainBinding.imgClick2.setImageResource(R.drawable.custom_circle_selected)
            dialogMainBinding.tv22.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
            dialogMainBinding.imgClick.setImageResource(R.drawable.custom_circle)
            dialogMainBinding.tv12.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color2))
            dialogMainBinding.imgClick3.setImageResource(R.drawable.custom_circle)
            dialogMainBinding.tv32.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color2))
            dialogMainBinding.imgClick4.setImageResource(R.drawable.custom_circle)
            dialogMainBinding.tv42.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color2))
            selectedData = dialogMainBinding.tv22.text as String?
            }

        dialogMainBinding.imgClick3.setOnClickListener {
            dialogMainBinding.imgClick3.setImageResource(R.drawable.custom_circle_selected)
            dialogMainBinding.tv32.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
            dialogMainBinding.imgClick.setImageResource(R.drawable.custom_circle)
            dialogMainBinding.tv12.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color2))
            dialogMainBinding.imgClick2.setImageResource(R.drawable.custom_circle)
            dialogMainBinding.tv22.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color2))
            dialogMainBinding.imgClick4.setImageResource(R.drawable.custom_circle)
            dialogMainBinding.tv42.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color2))
            selectedData = dialogMainBinding.tv32.text as String?
        }
        dialogMainBinding.imgClick4.setOnClickListener {
            dialogMainBinding.imgClick4.setImageResource(R.drawable.custom_circle_selected)
            dialogMainBinding.tv42.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
            dialogMainBinding.imgClick.setImageResource(R.drawable.custom_circle)
            dialogMainBinding.tv12.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color2))
            dialogMainBinding.imgClick2.setImageResource(R.drawable.custom_circle)
            dialogMainBinding.tv22.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color2))
            dialogMainBinding.imgClick3.setImageResource(R.drawable.custom_circle)
            dialogMainBinding.tv32.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color2))
            selectedData = dialogMainBinding.tv42.text as String?
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initAdapter(it: List<WaterReminderEntity>) {
        //adapter
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.reverseLayout = true;
        layoutManager.stackFromEnd = true;
        binding.rcv.layoutManager = layoutManager
        binding.rcv.adapter = adapter
        adapter.setData(it)
        adapter.notifyDataSetChanged()
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun initView() {

        val quantity = preferenceHelper.getInt(PreferenceHelper.PREF_QUANTITY_WATER)
        dailyWater = preferenceHelper.getInt(PreferenceHelper.PREF_DAILY_WATER)
        val wakeUpTimeHour = preferenceHelper.getString(PreferenceHelper.PREF_WAKE_UP_TIME_HOUR)
        val wakeUpTimeMinute = preferenceHelper.getString(PreferenceHelper.PREF_WAKE_UP_TIME_MINUTE)

        intImage = preferenceHelper.getInt(PreferenceHelper.SELECTED_IMAGE_RES_ID_KEY)
        title = preferenceHelper.getString(PreferenceHelper.SELECTED_CUP_KEY).toString()
        if(preferenceHelper.getString(PreferenceHelper.SELECTED_CUP_KEY).toString().isEmpty()){
            if (quantity > 100){
                binding.tvMl.text = "200 ml"
                binding.tvQuantityClock.text = "200 ml"
                binding.imgGlass.setImageResource(R.drawable.ic_cup_200ml)
                selectedImageResId = R.drawable.ic_cup_200ml
            }else{
                binding.tvMl.text = "100 ml"
                binding.tvQuantityClock.text = "100 ml"
                binding.imgGlass.setImageResource(R.drawable.ic_cup_100ml)
                selectedImageResId = R.drawable.ic_cup_100ml
            }
        }else{
            binding.imgGlass.setImageResource(intImage!!)
            binding.imgConvert.setImageResource(intImage!!)
            binding.tvMl.text = title
            binding.tvQuantityClock.text = title
        }

        binding.tvTimeClockNext.text = "$wakeUpTimeHour:$wakeUpTimeMinute"

        binding.tvQuantity.text = "/${dailyWater}ml"

        // click
        binding.imgGlass.setOnClickListener {
            // sum quantity drink water
            val total:String = (binding.tvMl.text as String).dropLast(2).trim()
            val sum:Int  = total.toInt()
            sumQuantityDrinkWater += sum
            binding.tvClickNumber.text = "$sumQuantityDrinkWater"
            preferenceHelper.setInt(PreferenceHelper.PREF_NUMBER_DRINK_WATER,sumQuantityDrinkWater)


            // progress
            val count:Int = (100/(dailyWater/sum)) - 1
                    currentPercent += count
            binding.semiCircleArcProgressBar.setPercent(currentPercent)
            if(currentPercent > 97) binding.imgHeart.setImageResource(R.drawable.ic_heart_selected)

            // add data recyclerview
            val quantityWater:String = binding.tvMl.text as String
            // get time
            val currentTime = Calendar.getInstance().time
            val timeFormat = SimpleDateFormat("hh:mm aa")
            val formattedTime = timeFormat.format(currentTime)
            date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())

            val newWaterReminder = selectedImageResId?.let { it1 -> WaterReminderEntity(it1,formattedTime,quantityWater,date!!) }
            if (newWaterReminder != null) {
                viewModel.insertWaterReminder(newWaterReminder)
            }

        }

    }

    private fun initChange() {
        binding.linearLayout3.setOnClickListener {

            val intent = Intent(requireContext(), SwitchCupActivity::class.java)
            intent.putExtra("data",selectedImageResId)
            intent.putExtra("data2",binding.tvMl.text)
            startActivityForResult(intent,REQUEST_CODE)
        }
    }

    @Deprecated("Deprecated in Java")
    @SuppressLint("DiscouragedApi")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val switchCup = data?.getSerializableExtra("selectedCup") as SwitchCup
            binding.tvMl.text = switchCup.quantityWater
            binding.tvQuantityClock.text = switchCup.quantityWater
            val image =  if (switchCup.isSelected) {
                    // Lấy tên ảnh được chọn
                    val imageName =
                        binding.root.context.resources.getResourceEntryName(switchCup.image)
                    imageName.replace("_unselected", "")
                } else {
                    // Lấy tên ảnh không được chọn
                    binding.root.context.resources.getResourceEntryName(R.drawable.images_glass)
                }.let { imageName ->
                    // Lấy ID ảnh tương ứng
                    val imageResId = binding.root.context.resources.getIdentifier(
                        imageName,
                        "drawable",
                        binding.root.context.packageName
                    )
                    imageResId
                }
            selectedImageResId = image
            binding.imgGlass.setImageResource(image)
            binding.imgConvert.setImageResource(image)

            preferenceHelper.setString(PreferenceHelper.SELECTED_CUP_KEY, switchCup.quantityWater.toString())
            preferenceHelper.setInt(PreferenceHelper.SELECTED_IMAGE_RES_ID_KEY, selectedImageResId!!)
        }
    }


    override fun onPause() {
        super.onPause()
        preferenceHelper.setInt(PreferenceHelper.PREF_CURRENT_PERCENT, currentPercent)
    }
}