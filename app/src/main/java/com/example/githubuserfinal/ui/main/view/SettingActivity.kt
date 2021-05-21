package com.example.githubuserfinal.ui.main.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.githubuserfinal.data.local.AlarmReceiver
import com.example.githubuserfinal.data.local.SettingPreference
import com.example.githubuserfinal.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding
    private lateinit var alarmReceiver: AlarmReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        alarmReceiver = AlarmReceiver()
    }

    private fun setupUI() {
        binding.settingBack.setOnClickListener { finish() }
        val pref = SettingPreference(this)
        binding.settingNotify.isChecked = pref.getNotify()
        binding.settingNotify.setOnCheckedChangeListener { _, isChecked ->
            pref.setNotify(isChecked)
            when(isChecked){
                true -> alarmReceiver.setNotify(this)
                false -> alarmReceiver.cancelNotify(this)
            }
        }
    }
}