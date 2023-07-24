package com.example.waterreminder.ui.composite_screen.home.switchcup

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import com.example.waterreminder.R
import com.example.waterreminder.base.BaseActivity
import com.example.waterreminder.databinding.ActivitySwitchCupBinding
import com.example.waterreminder.databinding.LayoutDialogCustomBinding
import com.example.waterreminder.database.cup.SwitchCup
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SwitchCupActivity : BaseActivity<ActivitySwitchCupBinding>() {
    private val viewModel:SwitchCupViewModel by viewModels()

    @Inject
    lateinit var adapter: SwitchCupAdapter


    private lateinit var dialogMainBinding:LayoutDialogCustomBinding
    private var selectedImageResource: Int = 0
    private var quantity: String = ""


    override fun initView() {
        initAdapter()
        setListener()
    }


    override fun setBinding(layoutInflater: LayoutInflater): ActivitySwitchCupBinding {
        return ActivitySwitchCupBinding.inflate(layoutInflater)
    }


    private fun initAdapter() {

        val str1 = intent.getStringExtra("data2")

        val switchCup = mutableListOf(
            SwitchCup(R.drawable.ic_cup_100ml_unselected,"100 ml",),
            SwitchCup(R.drawable.ic_cup_200ml_unselected, "200 ml"),
            SwitchCup(R.drawable.ic_cup_300ml_unselected, "300 ml"),
            SwitchCup(R.drawable.ic_cup_400ml_unselected, "400 ml"),
            SwitchCup(R.drawable.ic_cup_500ml_unselected, "500 ml"),
            SwitchCup(R.drawable.ic_cup_1000ml_unselected, "1000 ml"),
            SwitchCup(R.drawable.ic_cup_customize_unselected, "Customize")

        )
            for (cup in switchCup) {
                if(cup.quantityWater == str1){
                    cup.isSelected = true
                    viewModel.insertSwitchCup(cup)
                }else{
                    viewModel.insertSwitchCup(cup)
                }

            }






        val layoutManager = GridLayoutManager(this,3)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter

        viewModel.getSwitchCup()
        viewModel.listSwitchCup.observe(this){
            adapter.setData(it)
        }

        // back
        binding.tvCancel.setOnClickListener {
            finish()
        }
        binding.tvOk.setOnClickListener {
            // Chuyển dữ liệu cốc đã chọn về Activity trước đó
            val selectedCup = viewModel.listSwitchCup.value?.find { it.isSelected }
            if (selectedCup != null) {
                val resultIntent = Intent()
                resultIntent.putExtra("selectedCup", selectedCup)
                setResult(Activity.RESULT_OK, resultIntent)
            } else {
                setResult(Activity.RESULT_CANCELED)
            }
            finish()

        }
    }

    private fun setListener() {
        adapter.onClick.onItemClick = { switchCup ->
            switchCup.isSelected = !switchCup.isSelected
            viewModel.updateWaterSwitchCup(switchCup)
        }

        adapter.onClick.onItemClickCus = {
            showDialogBox(it)
        }
        adapter.onClick.onItemDelete ={
            viewModel.deleteSwitchCup(it.quantityWater)
        }
    }

    private fun showDialogBox(switchCup: SwitchCup) {
        dialogMainBinding = LayoutDialogCustomBinding.inflate(layoutInflater);

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(dialogMainBinding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        // Thiết lập kích thước của Dialog
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window?.attributes)
        val widthInDp = 300
        val widthInPx = (widthInDp * applicationContext.resources.displayMetrics.density).toInt()
        layoutParams.width = widthInPx
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        // Áp dụng các thay đổi vào Dialog
        dialog.window?.attributes = layoutParams
        // click image
        onClickImage(switchCup)
        dialogMainBinding.tvOk.setOnClickListener {
            if(dialogMainBinding.edtEnter.text.isEmpty()){
                dialogMainBinding.edtEnter.backgroundTintList = ContextCompat.getColorStateList(dialogMainBinding.root.context, R.color.green)
            }else{
                if(selectedImageResource == 0) selectedImageResource = R.drawable.ic_cup_customize_unselected
                quantity = dialogMainBinding.edtEnter.text.toString()+" ml"

                val imageResourceName = resources.getResourceEntryName(selectedImageResource)
                val unselectedImageResourceName = "${imageResourceName}_unselected"
                val unselectedImageResource = resources.getIdentifier(unselectedImageResourceName, "drawable", packageName)
                val newSwitchCup = SwitchCup(unselectedImageResource,quantity,true,true)
                viewModel.insertSwitchCup(newSwitchCup)
                dialog.dismiss()
            }
        }

        dialogMainBinding.tvCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }

    @SuppressLint("DiscouragedApi")
    private fun onClickImage(switchCup: SwitchCup) {
        selectedImageResource  = R.drawable.ic_cup_customize
         fun updateSelectedImage(imageView: ImageView, drawableResource: Int) {
            dialogMainBinding.img1.setImageResource(R.drawable.ic_cup_100ml_unselected)
            dialogMainBinding.img2.setImageResource(R.drawable.ic_cup_200ml_unselected)
            dialogMainBinding.img3.setImageResource(R.drawable.ic_cup_300ml_unselected)
            dialogMainBinding.img4.setImageResource(R.drawable.ic_cup_400ml_unselected)
            dialogMainBinding.img5.setImageResource(R.drawable.ic_cup_500ml_unselected)
            dialogMainBinding.img6.setImageResource(R.drawable.ic_cup_1000ml_unselected)
            dialogMainBinding.imgCustom.setImageResource(R.drawable.ic_cup_customize_unselected)
            imageView.setImageResource(drawableResource)
             selectedImageResource = drawableResource
        }

        dialogMainBinding.img1.setOnClickListener {
            updateSelectedImage(dialogMainBinding.img1, R.drawable.ic_cup_100ml)
        }

        dialogMainBinding.img2.setOnClickListener {
            updateSelectedImage(dialogMainBinding.img2, R.drawable.ic_cup_200ml)
        }

        dialogMainBinding.img3.setOnClickListener {
            updateSelectedImage(dialogMainBinding.img3, R.drawable.ic_cup_300ml)
        }

        dialogMainBinding.img4.setOnClickListener {
            updateSelectedImage(dialogMainBinding.img4, R.drawable.ic_cup_400ml)
        }

        dialogMainBinding.img5.setOnClickListener {
            updateSelectedImage(dialogMainBinding.img5, R.drawable.ic_cup_500ml)
        }

        dialogMainBinding.img6.setOnClickListener {
            updateSelectedImage(dialogMainBinding.img6, R.drawable.ic_cup_1000ml)
        }

        dialogMainBinding.imgCustom.setOnClickListener {
            updateSelectedImage(dialogMainBinding.imgCustom, R.drawable.ic_cup_customize)
        }
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, SwitchCupActivity::class.java)
        }
    }
}

