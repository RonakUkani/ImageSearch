package com.app.imagesearchdemo.di

import com.app.imagesearchdemo.ui.activity.ImageListActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = [ImageModule::class])
    internal abstract fun mainActivity(): ImageListActivity



}