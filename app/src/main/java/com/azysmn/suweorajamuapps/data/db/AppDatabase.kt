package com.azysmn.suweorajamuapps.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.azysmn.suweorajamuapps.data.dao.BarangDao
import com.azysmn.suweorajamuapps.data.dao.TransaksiDao
import com.azysmn.suweorajamuapps.data.model.Barang
import com.azysmn.suweorajamuapps.data.model.Transaksi

@Database(entities = [Barang::class, Transaksi::class],
    version = 2,
    exportSchema = true
)
abstract class AppDatabase :  RoomDatabase() {
    abstract fun barangDao(): BarangDao

    abstract fun transaksiDao(): TransaksiDao
}