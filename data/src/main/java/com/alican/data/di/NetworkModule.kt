package com.alican.data.di

import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import androidx.room.Room
import com.alican.data.BuildConfig
import com.alican.data.data.local.AppDatabase
import com.alican.data.data.remote.ApiService
import com.alican.data.data.remote.ApiServiceImpl
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.CertificatePinner
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient = HttpClient(OkHttp) {
        defaultRequest {
            url(BuildConfig.BASE_URL)
            header("Authorization", "Bearer ${BuildConfig.API_TOKEN}")
        }

        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }

        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Log.d("HTTP RESPONSE", message)
                }
            }
            level = LogLevel.BODY
        }

        engine {
            config {
                if (!BuildConfig.DEBUG) {
                    getCertificatePinner()
                }
            }
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 30_000
            connectTimeoutMillis = 30_000
            socketTimeoutMillis = 30_000
        }
    }


    @Provides
    @Singleton
    fun provideStockDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, BuildConfig.ROOM_DB_NAME)
            .fallbackToDestructiveMigration(false)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(client: HttpClient): ApiService = ApiServiceImpl(client)

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()
}

private fun getCertificatePinner(): CertificatePinner {
    val host = BuildConfig.BASE_URL.toUri().host ?: BuildConfig.BASE_URL.replace("https://", "")
    return CertificatePinner.Builder()
        .add(host, BuildConfig.CERTIFICATE_PIN)
        .build()
}