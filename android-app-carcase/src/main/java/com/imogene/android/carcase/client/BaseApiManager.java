package com.imogene.android.carcase.client;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.imogene.android.carcase.exception.RequestException;

import java.io.IOException;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Admin on 14.04.2017.
 */

public abstract class BaseApiManager {

    private ApiCore apiCore;

    protected BaseApiManager(){
        apiCore = createApiCore();
    }

    @Nullable
    protected abstract ApiCore createApiCore();

    protected final void setApiCore(@NonNull ApiCore apiCore){
        this.apiCore = apiCore;
    }

    protected final <T> T createApi(Class<T> clazz){
        checkApiCoreSpecified();
        return apiCore.createApi(clazz);
    }

    private void checkApiCoreSpecified(){
        if(apiCore == null){
            throw new IllegalStateException(
                    "ApiCore must be specified either by implementing createApiCore method or using setApiCore method");
        }
    }

    protected final OkHttpClient getClient(){
        checkApiCoreSpecified();
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
