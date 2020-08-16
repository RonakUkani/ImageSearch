package com.app.imagesearchdemo.api

import android.content.Context
import com.app.imagesearchdemo.R
import com.app.imagesearchdemo.di.AuthenticationLiveData
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException

open class BaseDataSource(
    private val context: Context,
    private val authenticationLiveData: AuthenticationLiveData<Unit>
) {
    fun handleError(it: Throwable, callback: (String) -> Unit = {}) {
        when (it) {
            is ConnectException -> {
                callback.invoke(context.getString(R.string.error_message_unable_to_connect))
            }
            is SocketTimeoutException -> {
                callback.invoke(context.getString(R.string.error_message_unable_to_connect))
            }
            is HttpException -> {
                callback.invoke(context.getString(R.string.error_message_unable_to_connect))
                if (it.code() == 401 || it.code() == 403) {
                    /**
                     * Here 401 for unauthorized Purpose
                     */
                    authenticationLiveData.postValue((Unit))
                }
            }
            else -> {
                callback.invoke(context.getString(R.string.error_something_went_wrong))
            }
        }
    }


}


