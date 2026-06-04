package com.example.androidapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidapp.R
import com.example.androidapp.data.Item
import com.example.androidapp.ui.AddEditActivity
import com.example.androidapp.ui.ItemAdapter
import com.example.androidapp.ui.ItemViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: ItemViewModel
    private lateinit var adapter: ItemAdapter

    private val addEditLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            val title = data?.getStringExtra("item_title")
            val desc = data?.getStringExtra("item_description")
            val itemId = data?.getIntExtra("item_id", 0) ?: 0
            if (!title.isNullOrEmpty()) {
                if (itemId != 0) {
                    // Update existing item
                    val item = Item(id = itemId, title = title, description = desc)
                    viewModel.update(item)
                } else {
                    // Insert new item
                    val item = Item(title = title, description = desc)
                    viewModel.insert(item)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(ItemViewModel::class.java)

        val rv = findViewById<RecyclerView>(R.id.recycler_view)
        rv.layoutManager = LinearLayoutManager(this)
        adapter = ItemAdapter { item ->
            val intent = Intent(this, AddEditActivity::class.java)
            intent.putExtra("item_id", item.id)
            intent.putExtra("item_title", item.title)
            intent.putExtra("item_description", item.description)
            addEditLauncher.launch(intent)
        }
        rv.adapter = adapter

        // Swipe to delete
        val touchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.bindingAdapterPosition
                val item = adapter.currentList.getOrNull(pos)
                if (item != null) {
                    viewModel.delete(item)
                    Snackbar.make(rv, "${item.title} deleted", Snackbar.LENGTH_SHORT).show()
                }
            }
        })
        touchHelper.attachToRecyclerView(rv)

        val fab = findViewById<FloatingActionButton>(R.id.fab_add)
        fab.setOnClickListener {
            val intent = Intent(this, AddEditActivity::class.java)
            addEditLauncher.launch(intent)
        }

        viewModel.items.observe(this) { list ->
            adapter.submitList(list)
        }
    }
}
