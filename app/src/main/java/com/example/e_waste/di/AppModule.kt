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
        return Retrofit.Builder()
            .baseUrl("https://api.ewaste-app.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}