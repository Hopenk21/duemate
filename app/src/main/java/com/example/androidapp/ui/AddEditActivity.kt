package com.example.androidapp.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.androidapp.R
import com.example.androidapp.data.Item
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class AddEditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit)

        val titleField = findViewById<TextInputEditText>(R.id.input_title)
        val descField = findViewById<TextInputEditText>(R.id.input_description)
        val saveBtn = findViewById<MaterialButton>(R.id.btn_save)

        saveBtn.setOnClickListener {
            val title = titleField.text?.toString()?.trim()
            val desc = descField.text?.toString()?.trim()
            if (title.isNullOrEmpty()) {
                titleField.error = "Required"
                return@setOnClickListener
            }

            val item = Item(title = title, description = desc)
            val data = Intent()
            data.putExtra("item_title", item.title)
            data.putExtra("item_description", item.description)
            setResult(Activity.RESULT_OK, data)
            finish()
        }
    }
}
