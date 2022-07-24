package com.singularitycoder.tenthousandhours

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.singularitycoder.tenthousandhours.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.setupUI()
    }

    private fun ActivityMainBinding.setupUI() {
        cardAddSkill.setOnClickListener {
            
        }
    }
}