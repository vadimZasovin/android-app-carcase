package com.imogene.android.carcase.client.live

import androidx.lifecycle.LiveData
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Created by Admin on 22.11.2017.
 */
class LiveDataCallAdapterFactory : CallAdapter.Factory() {

    override fun get(returnType: Type,
                     annotations: Array<out Annotation>,
                     retrofit: Retrofit) : CallAdapter<*, *>? {
        val rawType = getRawType(returnType)
        if(rawType !== LiveData::class.java){
            return null
        }

        if(returnType !is ParameterizedType){
            throw IllegalStateException("LiveData return type must be parametrized.")
        }

        val liveDataType = getParameterUpperBound(0, returnType)
        val rawLiveDataType = getRawType(liveDataType)
        if(rawLiveDataType !== Resource::class.java){
            return null
        }

        if(liveDataType !is ParameterizedType){
            throw IllegalStateException("Resource must be parametrized")
        }

        val resourceType = getParameterUpperBound(0, liveDataType)
        return LiveDataCallAdapter<Nothing>(resourceType)
    }
}