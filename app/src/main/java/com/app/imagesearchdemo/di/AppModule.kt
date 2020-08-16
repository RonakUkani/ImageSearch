package com.app.imagesearchdemo.di

import android.content.Context
import com.app.imagesearchdemo.ImageSearchDemo
import dagger.Module
import dagger.Provides

@Module
class AppModule {

    @Provides
    fun provideContext(application: ImageSearchDemo): Context {
        return application.applicationContext
    }

}