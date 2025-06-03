package com.azysmn.suweorajamuapps.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.azysmn.suweorajamuapps.data.model.Barang

@Dao
interface BarangDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBarang(barang: Barang)

    @Update
    suspend fun updateBarang(barang: Barang)

    @Delete
    suspend fun deleteBarang(barang: Barang)

    @Query("SELECT * FROM barang")
    suspend fun getAllBarang(): List<Barang>

}