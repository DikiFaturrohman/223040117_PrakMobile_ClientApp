package com.example.e_waste.di

import android.content.Context
import androidx.room.Room
import com.example.e_waste.data.api.ApiService
import com.example.e_waste.data.dao.EWasteDao
import com.example.e_waste.data.dao.OtpDao
import com.example.e_waste.data.dao.UserDao
import com.example.e_waste.data.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient // Ditambahkan
import okhttp3.logging.HttpLoggingInterceptor // Ditambahkan
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "ewaste_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }

    @Provides
    @Singleton
    fun provideOtpDao(appDatabase: AppDatabase): OtpDao {
        return appDatabase.otpDao()
    }

    @Provides
    @Singleton
    fun provideEWasteDao(appDatabase: AppDatabase): EWasteDao {
        return appDatabase.eWasteDao()
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        // --- Ditambahkan: Konfigurasi OkHttpClient dengan Logging Interceptor ---
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            // Set level ke NONE jika sudah production atau tidak ingin banyak log
            level = HttpLoggingInterceptor.Level.BODY // Untuk melihat detail request dan response di Logcat
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            // Anda bisa menambahkan interceptor lain di sini jika diperlukan
            // Misalnya untuk menambahkan token Authorization secara otomatis ke semua request
            .build()
        // --- Akhir penambahan OkHttpClient ---

        // --- ↓↓↓ GANTI URL DI BAWAH INI SESUAI SKENARIOMU ↓↓↓ ---

        // PILIH SALAH SATU SESUAI KEBUTUHANMU, dan hapus/komen yang tidak dipakai:

        // Untuk Emulator Android:
        val baseUrl = "http://10.0.2.2:8000/api/"

        // Untuk Perangkat Fisik (HP/Tablet Asli) - GANTI IP-nya!:
        // val baseUrl = "http://ALAMAT_IP_LOKAL_SERVER_ANDA:8000/api/" // <-- CONTOH, GANTI IP INI

        // --- ↑↑↑ SAMPAI SINI ↑↑↑ ---

        return Retrofit.Builder()
            .baseUrl(baseUrl) // Menggunakan variabel baseUrl yang sudah dipilih
            .client(client) // Menggunakan OkHttpClient yang sudah dikonfigurasi
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}