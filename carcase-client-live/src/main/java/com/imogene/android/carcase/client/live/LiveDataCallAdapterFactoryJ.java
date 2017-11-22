package com.imogene.android.carcase.client.live;

import android.arch.lifecycle.LiveData;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import retrofit2.CallAdapter;
import retrofit2.Retrofit;

/**
 * Created by Admin on 22.11.2017.
 */

public class LiveDataCallAdapterFactoryJ extends CallAdapter.Factory {

    @Override
    public CallAdapter<?, ?> get(Type returnType,
                                 Annotation[] annotations,
                                 Retrofit retrofit) {
        Class<?> rawType = getRawType(returnType);
        if(rawType != LiveData.class){
            return null;
        }

        if(!(returnType instanceof ParameterizedType)){
            throw new IllegalStateException("LiveData return type must be parametrized.");
        }

        Type liveDataType = getParameterUpperBound(0, (ParameterizedType) returnType);
        Class<?> rawLiveDataType = getRawType(liveDataType);
        if(rawLiveDataType != Resource.class){
            return null;
        }

        if(!(liveDataType instanceof ParameterizedType)){
            throw new IllegalStateException("Resource must be parametrized");
        }

        Type resourceType = getParameterUpperBound(0, (ParameterizedType) liveDataType);
        return new LiveDataCallAdapter(resourceType);
    }
}
