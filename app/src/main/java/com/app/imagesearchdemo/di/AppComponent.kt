package com.app.imagesearchdemo.di

import com.app.imagesearchdemo.ImageSearchDemo
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,ActivityBindingModule::class,
        ViewModelModule::class,ApiModule::class]
)

interface AppComponent : AndroidInjector<ImageSearchDemo> {
    @Component.Factory
    abstract class Builder : AndroidInjector.Factory<ImageSearchDemo>
}


