package com.alican.data.di


import android.content.Context
import androidx.room.Room
import com.alican.data.BuildConfig
import com.alican.data.data.local.AppDatabase
import com.alican.data.data.remote.WebService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
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
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttp(
        loggingInterceptor: HttpLoggingInterceptor,
    ): okhttp3.Call.Factory {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .callTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(providesOkhttpInterceptor())
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun providesOkhttpInterceptor(): Interceptor {
        return Interceptor { chain: Interceptor.Chain ->
            val original: Request = chain.request()
            val requestBuilder: Request.Builder = original.newBuilder()
                .addHeader("accept", "application/json")
                .header("Authorization", "Bearer ${BuildConfig.API_TOKEN}")
            val request: Request = requestBuilder.build()
            chain.proceed(request)
        }
    }

    @Provides
    @Singleton
    fun provideWebService(
        callFactory: okhttp3.Call.Factory
    ): WebService = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .callFactory(callFactory)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(WebService::class.java)

    @Provides
    @Singleton
    fun provideStockDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, BuildConfig.ROOM_DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }
}
