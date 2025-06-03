package com.azysmn.suweorajamuapps.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azysmn.suweorajamuapps.data.model.Barang
import com.azysmn.suweorajamuapps.data.model.Transaksi
import com.azysmn.suweorajamuapps.data.model.TransaksiBarang
import com.azysmn.suweorajamuapps.repostitory.SuweOraJamuRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransaksiViewModel @Inject constructor(
    private val repository: SuweOraJamuRepository
): ViewModel() {

    private val _barangList = MutableLiveData<List<Barang>>()
    val barangList: LiveData<List<Barang>> = _barangList

    private val _transaksiList = MutableLiveData<List<TransaksiBarang>>()
    val transaksiList: LiveData<List<TransaksiBarang>> = _transaksiList

    init {
        loadBarang()
        loadTransaksi()
    }

    fun loadBarang() {
        viewModelScope.launch {
            _barangList.value = repository.getAllBarang()
        }
    }

    fun loadTransaksi() {
        viewModelScope.launch {
            _transaksiList.value = repository.getAllTransaksiWithBarang()
        }
    }

    fun addTransaksi(transaksi: Transaksi) {
        viewModelScope.launch {
            repository.insertTransaksi(transaksi)
            loadTransaksi()
        }
    }

    fun updateTransaksi(transaksi: Transaksi) {
        viewModelScope.launch {
            repository.updateTransaksi(transaksi)
            loadTransaksi()
        }
    }

    fun deleteTransaksi(transaksi: Transaksi) {
        viewModelScope.launch {
            repository.deleteTransaksi(transaksi)
            loadTransaksi()
        }
    }
}