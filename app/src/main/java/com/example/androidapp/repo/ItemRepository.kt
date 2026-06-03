package com.example.androidapp.repo

import com.example.androidapp.data.Item
import com.example.androidapp.data.ItemDao
import kotlinx.coroutines.flow.Flow

class ItemRepository(private val dao: ItemDao) {
    fun getAllItems(): Flow<List<Item>> = dao.getAll()

    suspend fun insert(item: Item): Long = dao.insert(item)

    suspend fun update(item: Item) = dao.update(item)

    suspend fun delete(item: Item) = dao.delete(item)
}
