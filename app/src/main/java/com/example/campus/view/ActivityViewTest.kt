package com.example.campus.view

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.campus.R
import com.example.campus.view.course.SaveDialog
import com.google.android.material.tabs.TabLayout

class ActivityViewTest : AppCompatActivity() {

    private val titleArray = arrayOf("Messages","Activities")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_view_test)
        findViewById<TabLayout>(R.id.layout_view_test_tab)?.apply {
            titleArray.forEach {
                this.addTab(this.newTab().setText(it))
            }
        }
        findViewById<TextView>(R.id.btn_test_dialog)?.apply {
            setOnClickListener {
                val dialog = SaveDialog(savedInstanceState,null)
                    dialog.showDialog(this@ActivityViewTest)
            }
        }
    }
}