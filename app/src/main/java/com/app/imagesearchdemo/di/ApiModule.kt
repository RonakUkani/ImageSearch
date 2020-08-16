package com.app.imagesearchdemo.di

import android.content.Context
import com.app.imagesearchdemo.BuildConfig
import com.app.imagesearchdemo.R
import com.app.imagesearchdemo.api.ApiService
import com.app.imagesearchdemo.api.CommonRepository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class ApiModule {

    @Singleton
    @Provides
    fun provideTailorService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideAuthenticationLiveData(): AuthenticationLiveData<Unit> {
        return AuthenticationLiveData()
    }

    @Singleton
    @Provides
    fun provideTailorCommonRepository(
        context: Context,
        apiService: ApiService,
        authenticationLiveData: AuthenticationLiveData<Unit>
    ): CommonRepository {
        return CommonRepository(context, apiService, authenticationLiveData)
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        context: Context,
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
        @Named("header") headerInterceptor: Interceptor,
        @Named("log") logInterceptor: Interceptor
    ): Retrofit {
        return Retrofit.Builder()
            .client(
                okHttpClient.newBuilder()
                    .addInterceptor(headerInterceptor)
                    .addInterceptor(logInterceptor)
                    .build()
            )
            .baseUrl(context.getString(R.string.apiUrl))
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    @Named("log")
    fun provideHttpLoggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else HttpLoggingInterceptor.Level.NONE
        }
    }

    @Provides
    @Singleton
    @Named("header")
    fun provideHeaderInterceptor(): Interceptor {
        return Interceptor { chain ->
            val build = chain.request().newBuilder()
                .addHeader("Authorization", "Client-ID 137cda6b5008a7c")
                .build()
            chain.proceed(build)
        }
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().setVersion(1.0).create()
    }

    @Provides
    @Singleton
    fun provideGsonConverter(gson: Gson): GsonConverterFactory {
        return GsonConverterFactory.create(gson)
    }

    @Singleton
    @Provides
    fun provideSimpleOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(2, TimeUnit.MINUTES)
            .readTimeout(2, TimeUnit.MINUTES)
            .writeTimeout(2, TimeUnit.MINUTES)
            .build()
    }

}