package com.example.activitytest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.activitytest.databinding.ActivityFourthBinding
import com.example.activitytest.databinding.ActivitySecondBinding

class FourthActivity : AppCompatActivity() {

    lateinit var binding: ActivityFourthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFourthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.returnButton.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra("return_extra", "从FourthActivity返回的数据")
        setResult(RESULT_OK, intent)
        finish()
    }
}