package com.example.waterreminder.base

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<T : ViewBinding> : AppCompatActivity() {
    protected lateinit var binding: T
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getInflatedLayout(layoutInflater))

        initView()
 }

   abstract fun initView()

   abstract fun setBinding(layoutInflater: LayoutInflater): T

   private fun getInflatedLayout(inflater: LayoutInflater): View {
      binding = setBinding(inflater)
      return binding.root
   }
    fun <T> navigateTo(dest: Class<T>) {
        startActivity(Intent(this, dest))
    }
}