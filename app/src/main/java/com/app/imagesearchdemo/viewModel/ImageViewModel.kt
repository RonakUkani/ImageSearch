package com.app.imagesearchdemo.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.imagesearchdemo.api.AppResponse
import com.app.imagesearchdemo.api.CommonRepository
import com.app.imagesearchdemo.data.ImageData
import javax.inject.Inject

class ImageViewModel @Inject constructor(private val commonRepository: CommonRepository) :
    ViewModel() {

     val imageDataSuccess = MutableLiveData<AppResponse<List<ImageData>>>()
     val imageDataFailure = MutableLiveData<String>()

    fun getImageList(string: String) {
        commonRepository.getImageList(viewModelScope,string,imageDataSuccess,imageDataFailure)
    }

}