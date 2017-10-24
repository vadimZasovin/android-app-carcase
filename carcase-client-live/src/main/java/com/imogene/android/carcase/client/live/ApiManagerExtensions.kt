package com.imogene.android.carcase.client.live

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.imogene.android.carcase.client.BaseApiManager
import com.imogene.android.carcase.commons.exception.RequestException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Admin on 17.10.2017.
 */

fun <T> BaseApiManager.enqueueCall(call: Call<T>, sendLoadingEvent: Boolean = true) : LiveData<Resource<T>>{
    val result = MutableLiveData<Resource<T>>()

    call.enqueue(object : Callback<T>{

        override fun onFailure(call: Call<T>, t: Throwable) {
            result.value = networkError(t)
        }

        override fun onResponse(call: Call<T>, response: Response<T>) {
            if(response.isSuccessful){
                result.value = networkSuccess(response.body())
            }else{
                val errorCode = response.code()
                val error = RequestException(errorCode)
                result.value = networkError(error)
            }
        }
    })

    if(sendLoadingEvent){
        result.value = networkLoading()
    }
    return result
}

private fun <T> networkSuccess(data: T) = Resource.success(Source.NETWORK, data)

private fun networkError(error: Throwable) = Resource.error(Source.NETWORK, error, null)

private fun networkLoading() = Resource.loading(Source.NETWORK, null)