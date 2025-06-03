package com.azysmn.suweorajamuapps.repostitory

import com.azysmn.suweorajamuapps.data.dao.BarangDao
import com.azysmn.suweorajamuapps.data.dao.TransaksiDao
import com.azysmn.suweorajamuapps.data.model.Barang
import com.azysmn.suweorajamuapps.data.model.Transaksi
import com.azysmn.suweorajamuapps.data.model.TransaksiBarang
import javax.inject.Inject

class SuweOraJamuRepository @Inject constructor(
    private val barangDao: BarangDao,
    private val transaksiDao: TransaksiDao
) {
    suspend fun insertBarang(barang: Barang) = barangDao.insertBarang(barang)
    suspend fun updateBarang(barang: Barang) = barangDao.updateBarang(barang)
    suspend fun deleteBarang(barang: Barang) = barangDao.deleteBarang(barang)
    suspend fun getAllBarang(): List<Barang> = barangDao.getAllBarang()

    suspend fun insertTransaksi(transaksi: Transaksi) = transaksiDao.insertTransaksi(transaksi)
    suspend fun updateTransaksi(transaksi: Transaksi) = transaksiDao.updateTransaksi(transaksi)
    suspend fun deleteTransaksi(transaksi: Transaksi) = transaksiDao.deleteTransaksi(transaksi)
    suspend fun getAllTransaksiWithBarang(): List<TransaksiBarang> = transaksiDao.getAllTransaksiWithBarang()

}