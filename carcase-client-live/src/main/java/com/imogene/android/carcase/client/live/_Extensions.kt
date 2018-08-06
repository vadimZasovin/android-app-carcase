package com.imogene.android.carcase.client.live

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.imogene.android.carcase.client.ApiCore
import com.imogene.android.carcase.client.BaseApiManager
import com.imogene.android.carcase.commons.exception.RequestException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Admin on 17.10.2017.
 */

internal fun <T> Call<T>.asLiveData() : LiveData<Resource<T>> {
    if(isCanceled || isExecuted){
        throw IllegalStateException("Cancelled or executed Call can " +
                "not be represented as a LiveData.")
    }

    val result = MutableLiveData<Resource<T>>()
    enqueue(object : Callback<T>{

        override fun onFailure(call: Call<T>, t: Throwable) {
            result.postValue(error(t))
        }

        override fun onResponse(call: Call<T>, response: Response<T>) {
            if(response.isSuccessful){
                result.postValue(success(response.body()))
            }else{
                val errorCode = response.code()
                val error = RequestException(errorCode)
                result.postValue(error(error))
            }
        }
    })
    result.postValue(loading())
    return result
}

private fun <T> success(data: T) = Resource.success(Source.NETWORK, data)

private fun error(error: Throwable) = Resource.error(Source.NETWORK, error, null)

private fun loading() = Resource.loading(Source.NETWORK, null)

fun <T> BaseApiManager.enqueueCall(call: Call<T>) = call.asLiveData()

fun ApiCore.Builder.addLiveDataCallAdapterFactory() : ApiCore.Builder
        = addCallAdapterFactory(LiveDataCallAdapterFactory())