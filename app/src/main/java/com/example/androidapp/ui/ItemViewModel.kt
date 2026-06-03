package com.example.androidapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.androidapp.data.AppDatabase
import com.example.androidapp.data.Item
import com.example.androidapp.repo.ItemRepository
import kotlinx.coroutines.launch

class ItemViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ItemRepository

    val items = MutableLiveDataEx<List<Item>>()

    init {
        val dao = AppDatabase.getInstance(application).itemDao()
        repository = ItemRepository(dao)
        repository.getAllItems().asLiveData().observeForever { list ->
            items.postValue(list)
        }
    }

    fun insert(item: Item) = viewModelScope.launch {
        repository.insert(item)
    }

    fun update(item: Item) = viewModelScope.launch {
        repository.update(item)
    }

    fun delete(item: Item) = viewModelScope.launch {
        repository.delete(item)
    }
}

// Small helper LiveData wrapper so we can postValue easily
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MutableLiveDataEx<T> : MutableLiveData<T>() {
    public override fun postValue(value: T) {
        super.postValue(value)
    }
}
