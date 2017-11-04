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
            result.value = error(t)
        }

        override fun onResponse(call: Call<T>, response: Response<T>) {
            if(response.isSuccessful){
                result.value = success(response.body())
            }else{
                val errorCode = response.code()
                val error = RequestException(errorCode)
                result.value = error(error)
            }
        }
    })

    if(sendLoadingEvent){
        result.value = loading()
    }
    return result
}

private fun <T> success(data: T) = Resource.success(Source.NETWORK, data)

private fun error(error: Throwable) = Resource.error(Source.NETWORK, error, null)

private fun loading() = Resource.loading(Source.NETWORK, null)