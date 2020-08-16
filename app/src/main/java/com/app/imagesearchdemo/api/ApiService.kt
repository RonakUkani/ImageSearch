package com.app.imagesearchdemo.api

import com.app.imagesearchdemo.data.ImageData
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {


    @GET(ApiEndpoints.IMAGE_SEARCH)
    suspend fun getImageList(@Query(ApiParameters.QUERY_PARAM) string: String): AppResponse<List<ImageData>>
}