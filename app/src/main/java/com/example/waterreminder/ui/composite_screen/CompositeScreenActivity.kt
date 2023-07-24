package com.example.waterreminder.ui.composite_screen

import android.app.NotificationManager
import android.view.LayoutInflater
import androidx.viewpager2.widget.ViewPager2
import com.example.waterreminder.R
import com.example.waterreminder.base.BaseActivity
import com.example.waterreminder.databinding.ActivityCompositeScreenBinding
import com.example.waterreminder.ui.composite_screen.history.HistoryFragment
import com.example.waterreminder.ui.composite_screen.home.HomeFragment
import com.example.waterreminder.ui.composite_screen.settings.SettingsFragment
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CompositeScreenActivity : BaseActivity<ActivityCompositeScreenBinding>() {

    override fun initView() {
        initTab()
        // Tắt thông báo
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(1)
    }

    override fun setBinding(layoutInflater: LayoutInflater): ActivityCompositeScreenBinding {
        return ActivityCompositeScreenBinding.inflate(layoutInflater)
    }

    private fun initTab() {

        val compositeScreenAdapter = CompositeScreenAdapter(supportFragmentManager)
        compositeScreenAdapter.addFragment(HomeFragment(), "Home")
        compositeScreenAdapter.addFragment(HistoryFragment(), "History")
        compositeScreenAdapter.addFragment(SettingsFragment(), "Settings")



        binding.viewPager2.adapter = compositeScreenAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager2)

        val tabs = binding.tabLayout
        tabs.getTabAt(0)!!.setIcon(R.drawable.ic_water_screen)
        tabs.getTabAt(1)!!.setIcon(R.drawable.ic_history)
        tabs.getTabAt(2)!!.setIcon(R.drawable.ic_setting)


    }

}
