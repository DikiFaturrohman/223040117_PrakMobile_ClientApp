package com.example.e_waste.di

import android.content.Context
import androidx.room.Room
import com.example.e_waste.data.api.ApiService
import com.example.e_waste.data.api.AuthInterceptor
import com.example.e_waste.data.dao.EWasteDao
import com.example.e_waste.data.dao.UserDao
import com.example.e_waste.data.database.AppDatabase
import com.example.e_waste.data.repository.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
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
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideUserDao(appDatabase: AppDatabase): UserDao = appDatabase.userDao()

    @Provides
    @Singleton
    fun provideEWasteDao(appDatabase: AppDatabase): EWasteDao = appDatabase.eWasteDao()

    // ================== PERBAIKAN DI SINI ==================
    @Provides
    @Singleton
    // Tambahkan @ApplicationContext context: Context sebagai parameter
    fun provideSessionManager(@ApplicationContext context: Context): SessionManager {
        // Teruskan context saat membuat SessionManager
        return SessionManager(context)
    }
    // ========================================================

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/api/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}