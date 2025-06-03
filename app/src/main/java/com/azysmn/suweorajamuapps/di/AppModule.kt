package com.azysmn.suweorajamuapps.di

import android.content.Context
import androidx.room.Room
import com.azysmn.suweorajamuapps.data.dao.BarangDao
import com.azysmn.suweorajamuapps.data.dao.TransaksiDao
import com.azysmn.suweorajamuapps.data.db.AppDatabase
import com.azysmn.suweorajamuapps.repostitory.SuweOraJamuRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "suweorajamu_db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideBarangDao(db: AppDatabase): BarangDao = db.barangDao()

    @Provides
    fun provideTransaksiDao(db: AppDatabase): TransaksiDao = db.transaksiDao()

    @Provides
    @Singleton
    fun provideRepository(
        barangDao: BarangDao,
        transaksiDao: TransaksiDao
    ): SuweOraJamuRepository {
        return SuweOraJamuRepository(barangDao, transaksiDao)
    }
}
