package com.example.waterreminder.ui.composite_screen.history

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.waterreminder.R
import com.example.waterreminder.databinding.LayoutHistoryBinding
import com.example.waterreminder.databinding.LayoutHomeBinding
import com.example.waterreminder.helper.PreferenceHelper
import com.example.waterreminder.helper.PreferenceHelper.Companion.PREF_QUANTITY_WATER
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.LargeValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class HistoryFragment : Fragment() {
    private val viewModel: HistoryViewModel by viewModels()

    @Inject
    lateinit var preferenceHelper: PreferenceHelper

    private lateinit var binding: LayoutHistoryBinding
    private var showMonthView = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutHistoryBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListener()
        observe()
    }

    private fun observe() {
       val maxQuantity =  preferenceHelper.getInt(PREF_QUANTITY_WATER)
        viewModel.getWaterReminder()
        viewModel.listWaterReminder.observe(viewLifecycleOwner) { waterReminders ->
            val mapDateToTotalQuantity = mutableMapOf<String, Float>()

            // Duyệt danh sách waterReminders để tính tổng số lượng nước của mỗi ngày và giá trị tổng lượng nước tối đa
            for (waterReminder in waterReminders) {
                val quantity = waterReminder.quantityWater.replace(" ml", "").trim().toFloat()
                val checkedDate = waterReminder.checkedDate.substring(0, 10)

                if (mapDateToTotalQuantity.containsKey(checkedDate)) {
                    val currentTotalQuantity = mapDateToTotalQuantity[checkedDate] ?: 0f
                    mapDateToTotalQuantity[checkedDate] = currentTotalQuantity + quantity
                } else {
                    mapDateToTotalQuantity[checkedDate] = quantity
                }

            }

            val entries = mutableListOf<BarEntry>()

            // Duyệt mapDateToTotalQuantity để tính tỷ lệ phần trăm và tạo danh sách BarEntry
            mapDateToTotalQuantity.forEach { (checkedDate, totalQuantity) ->
                val dateInDays = checkedDate.substring(8).toFloat()
                val percentage = (totalQuantity / maxQuantity) * 100f
                entries.add(BarEntry(dateInDays, percentage))
            }

            val isMonthView = showMonthView
            initChart(binding.chartBar, entries, isMonthView)
        }
    }

    private fun setListener() {
        binding.rl1.setOnClickListener {
            binding.rl1.setBackgroundResource(R.drawable.bg_backgroud_until)
            binding.tvMonth.setTextColor(requireContext().getColor(R.color.black))
            binding.rl2.setBackgroundResource(R.drawable.bg_backgroud_until_blue)
            binding.tvYear.setTextColor(requireContext().getColor(R.color.white))
            // Update chart view to show month view
            showMonthView = true
            updateXAxisMonthView(binding.chartBar)
            binding.chartBar.animateY(1000)
            binding.chartBar.invalidate()
        }

        binding.rl2.setOnClickListener {
            binding.rl2.setBackgroundResource(R.drawable.bg_backgroud_until)
            binding.tvYear.setTextColor(requireContext().getColor(R.color.black))
            binding.rl1.setBackgroundResource(R.drawable.bg_backgroud_until_blue)
            binding.tvMonth.setTextColor(requireContext().getColor(R.color.white))
            // Update chart view to show year view
            showMonthView = false
            updateXAxisYearView(binding.chartBar)
            binding.chartBar.animateY(1000)
            binding.chartBar.invalidate()
        }
    }

    private fun initChart(
        chart: BarChart,entries: List<BarEntry>, isMonthView: Boolean) {
        chart.description.isEnabled = false
        chart.setPinchZoom(false)
        chart.setDrawBarShadow(false)
        chart.setDrawGridBackground(false)
        chart.setGridBackgroundColor(0)
        chart.isScaleYEnabled = false
        chart.extraBottomOffset = 30.0f
        chart.extraTopOffset = 8.0f
        chart.legend.isEnabled = false
        chart.axisRight.isEnabled = false
        chart.setScaleMinima(7f, 1f)
        chart.setVisibleXRangeMaximum(10f)
        chart.setFitBars(true)

        val xAxis = chart.xAxis
        xAxis.typeface = ResourcesCompat.getFont(requireContext(), R.font.montserrat_regular)
        xAxis.textColor = Color.parseColor("#323232")
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textSize = 10f
        xAxis.setCenterAxisLabels(false)
        xAxis.setDrawGridLines(false)
        xAxis.isEnabled = true
        xAxis.axisLineColor = Color.parseColor("#B8BDBE")
        xAxis.yOffset = 8f

        val leftAxis = chart.axisLeft
        leftAxis.typeface = ResourcesCompat.getFont(requireContext(), R.font.montserrat_regular)
        leftAxis.textColor = Color.parseColor("#323232")
        leftAxis.textSize = 8f
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        leftAxis.valueFormatter = PercentFormatter() // Sử dụng PercentFormatter để định dạng nhãn là phần trăm
        leftAxis.enableGridDashedLine(5.0f, 5.0f, 0.0f)
        leftAxis.isEnabled = true
        leftAxis.labelCount = 6 // SL nhan trên truc y
        leftAxis.axisMaximum = 100f
        leftAxis.axisLineColor = Color.parseColor("#00000000")
        leftAxis.xOffset = 20f
        chart.xAxis.axisMinimum = -0.3f
        chart.xAxis.axisMaximum = 50f


        if (isMonthView) {
            updateXAxisMonthView(chart)
        } else {
            updateXAxisYearView(chart)
        }

        val data = BarData()
        val dataSet = BarDataSet(entries, "")
        dataSet.setDrawValues(false)
        data.addDataSet(dataSet)
        chart.data = data

        chart.animateY(1000)
        chart.invalidate()
    }
    private fun updateXAxisMonthView(chart: BarChart) {
        val listDaysInMonth: MutableList<String> = mutableListOf()

        // Lặp từ ngày 1 đến 30 (hoặc số ngày tương ứng trong tháng) và thêm vào danh sách các ngày trong tháng
        for (dayOfMonth in 1..30) {
            listDaysInMonth.add(dayOfMonth.toString())
        }

        val xAxis = chart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(listDaysInMonth)
    }

    // Hàm cập nhật hiển thị trục X theo năm
    private fun updateXAxisYearView(chart: BarChart) {
        val listMonths: MutableList<String> = mutableListOf()

        // Lặp từ tháng 1 đến tháng 12 và thêm vào danh sách các tháng
        for (monthOfYear in 1..12) {
            listMonths.add(monthOfYear.toString())
        }

        val xAxis = chart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(listMonths)
    }

}