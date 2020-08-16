package com.app.imagesearchdemo.di

import androidx.lifecycle.ViewModel
import com.app.imagesearchdemo.viewModel.ImageViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Module where classes needed for app launch are defined.
 */
@Module
internal abstract class ImageModule {

    /**
     * The ViewModels are created by Dagger in a map. Via the @ViewModelKey, we define that we
     * want to get a [ImageViewModel] class.
     */
    @Binds
    @IntoMap
    @ViewModelKey(ImageViewModel::class)
    internal abstract fun bindLaunchViewModel(viewModel: ImageViewModel): ViewModel
}