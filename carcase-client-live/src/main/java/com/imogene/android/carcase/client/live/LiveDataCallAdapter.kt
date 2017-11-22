package com.imogene.android.carcase.client.live

import android.arch.lifecycle.LiveData
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

/**
 * Created by Admin on 22.11.2017.
 */
internal class LiveDataCallAdapter<R>(private val resourceType: Type) :
        CallAdapter<R, LiveData<Resource<R>>> {

    override fun responseType() = resourceType

    override fun adapt(call: Call<R>) = call.asLiveData()
}