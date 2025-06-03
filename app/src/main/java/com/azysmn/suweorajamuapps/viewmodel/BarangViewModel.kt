package com.azysmn.suweorajamuapps.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azysmn.suweorajamuapps.data.model.Barang
import com.azysmn.suweorajamuapps.repostitory.SuweOraJamuRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BarangViewModel @Inject constructor(
    private val repository: SuweOraJamuRepository
) : ViewModel() {
    private val _barangList = MutableLiveData<List<Barang>>()

    val barangList: LiveData<List<Barang>> = _barangList

    init {
        loadBarang()
    }

    fun loadBarang() {
        viewModelScope.launch {
            _barangList.value = repository.getAllBarang()
        }
    }

    fun insertBarang(barang: Barang) {
        viewModelScope.launch {
            repository.insertBarang(barang)
            loadBarang()
        }
    }

    fun updateBarang(barang: Barang) {
        viewModelScope.launch {
            repository.updateBarang(barang)
            loadBarang()
        }
    }

    fun deleteBarang(barang: Barang) {
        viewModelScope.launch {
            repository.deleteBarang(barang)
            loadBarang()
        }
    }

}