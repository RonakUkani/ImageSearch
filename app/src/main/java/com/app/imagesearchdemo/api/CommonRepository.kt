package com.app.imagesearchdemo.api

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.app.imagesearchdemo.data.ImageData
import com.app.imagesearchdemo.di.AuthenticationLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class CommonRepository @Inject constructor(
    context: Context,
    private val apiService: ApiService,
    authenticationLiveData: AuthenticationLiveData<Unit>) :
    BaseDataSource(context, authenticationLiveData) {

    fun getImageList(
        viewModelScope: CoroutineScope,
        string: String,
        imageDataSuccess: MutableLiveData<AppResponse<List<ImageData>>>,
        imageDataFailure: MutableLiveData<String>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                apiService.getImageList(string)
            }.fold(
                {
                    imageDataSuccess.postValue(it)
                }, {
                    handleError(it) { error ->
                        imageDataFailure.postValue(error)
                    }
                })
        }
    }

}