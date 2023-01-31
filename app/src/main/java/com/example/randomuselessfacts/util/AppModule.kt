package com.example.randomuselessfacts.util

import android.content.Context
import androidx.room.Room
import com.example.randomuselessfacts.api.ApiReferences
import com.example.randomuselessfacts.api.FactsApi
import com.example.randomuselessfacts.database.FactDao
import com.example.randomuselessfacts.database.FactsDatabase
import com.example.randomuselessfacts.repo.Repository
import com.example.randomuselessfacts.repo.RepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHttpInterceptor() =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor) =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

    @Provides
    @Singleton
    fun provideApi(okHttpClient: OkHttpClient): FactsApi = Retrofit.Builder()
        .baseUrl(ApiReferences.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(FactsApi::class.java)

    @Provides
    @Singleton
    fun provideRepo(factsApi: FactsApi, factDao: FactDao):Repository = RepositoryImp(factsApi,factDao)

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        FactsDatabase::class.java,
        "Facts.db"
    ).build()

    @Provides
    @Singleton
    fun provideDao(database: FactsDatabase) = database.getDao()
}