package com.example.bevigilosintdemo.ui

import android.os.Bundle
import android.view.LayoutInflater
import com.example.bevigilosintdemo.R
import com.example.bevigilosintdemo.databinding.ActivityHomeBinding

class HomeActivity : BaseActivity() {

    lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}