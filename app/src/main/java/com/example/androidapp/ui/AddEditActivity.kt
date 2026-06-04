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
        val cancelBtn = findViewById<MaterialButton>(R.id.btn_cancel)

        // If editing an existing item, prefill fields
        val existingId = intent.getIntExtra("item_id", 0)
        val isEdit = existingId != 0
        if (isEdit) {
            supportActionBar?.title = "Edit Item"
            val existingTitle = intent.getStringExtra("item_title")
            val existingDesc = intent.getStringExtra("item_description")
            titleField.setText(existingTitle)
            descField.setText(existingDesc)
        } else {
            supportActionBar?.title = "Add Item"
        }

        cancelBtn.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }

        saveBtn.setOnClickListener {
            val title = titleField.text?.toString()?.trim()
            val desc = descField.text?.toString()?.trim()
            if (title.isNullOrEmpty()) {
                titleField.error = "Required"
                return@setOnClickListener
            }

            val data = Intent()
            data.putExtra("item_title", title)
            data.putExtra("item_description", desc)
            if (existingId != 0) data.putExtra("item_id", existingId)
            setResult(Activity.RESULT_OK, data)
            finish()
        }
    }
}
