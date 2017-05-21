package com.imogene.android.carcase.client;

import android.support.annotation.NonNull;

import com.imogene.android.carcase.exception.RequestException;

import java.io.IOException;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Admin on 14.04.2017.
 */

public abstract class BaseApiManager {

    private final ApiCore apiCore;

    public BaseApiManager(){
        apiCore = createApiCore();
    }

    @NonNull
    protected abstract ApiCore createApiCore();

    protected final <T> T createApi(Class<T> clazz){
        return apiCore.createApi(clazz);
    }

    protected final OkHttpClient getClient(){
        return apiCore.getClient();
    }

    protected <T> T executeCall(Call<T> call) throws RequestException{
        Response<T> response;
        try {
            response = call.execute();
        }catch (IOException e){
            throw new RequestException(e);
        }
        if(response.isSuccessful()){
            return response.body();
        }else {
            int errorCode = response.code();
            throw new RequestException(errorCode);
        }
    }
}
