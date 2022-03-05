package com.example.campus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView


class MainActivity : AppCompatActivity() {
    lateinit var textView:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView = findViewById(R.id.textHello)
        textView.apply {
            setOnClickListener {
                var intent: Intent = Intent(this@MainActivity, FragmentTabHostActivity::class.java)
                startActivity(intent)
            }
        }
    }

}