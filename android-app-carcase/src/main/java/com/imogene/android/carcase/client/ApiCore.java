package com.imogene.android.carcase.client;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Admin on 14.04.2017.
 */

public class ApiCore {

    private final Retrofit retrofit;

    private ApiCore(Builder builder){
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

        if(builder.loggingEnabled){
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            clientBuilder.addInterceptor(loggingInterceptor);
        }

        List<Interceptor> interceptors = builder.interceptors;
        if(interceptors != null && !interceptors.isEmpty()){
            for(Interceptor interceptor : interceptors){
                clientBuilder.addInterceptor(interceptor);
            }
        }

        List<Interceptor> networkInterceptors = builder.networkInterceptors;
        if(networkInterceptors != null && !networkInterceptors.isEmpty()){
            for(Interceptor interceptor : networkInterceptors){
                clientBuilder.addNetworkInterceptor(interceptor);
            }
        }

        clientBuilder.readTimeout(builder.readTimeout, builder.readTimeoutTimeUnit);
        clientBuilder.writeTimeout(builder.writeTimeout, builder.writeTimeoutTimeUnit);

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
        retrofitBuilder
                .baseUrl(builder.baseUrl)
                .client(clientBuilder.build())
                .build();

        List<Converter.Factory> converterFactories = builder.converterFactories;
        if(converterFactories != null && !converterFactories.isEmpty()){
            for(Converter.Factory factory : converterFactories){
                retrofitBuilder.addConverterFactory(factory);
            }
        }

        retrofit = retrofitBuilder.build();
    }

    public <T> T createApi(Class<T> clazz){
        return retrofit.create(clazz);
    }

    public static final class Builder{

        private String baseUrl;
        private boolean loggingEnabled;
        private long readTimeout;
        private TimeUnit readTimeoutTimeUnit;
        private long writeTimeout;
        private TimeUnit writeTimeoutTimeUnit;
        private List<Interceptor> interceptors;
        private List<Interceptor> networkInterceptors;
        private List<Converter.Factory> converterFactories;

        public Builder(){
            loggingEnabled = true;
            readTimeout = 15;
            readTimeoutTimeUnit = TimeUnit.SECONDS;
            writeTimeout = 10;
            writeTimeoutTimeUnit = TimeUnit.SECONDS;
        }

        public Builder baseUrl(@NonNull String baseUrl){
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder loggingEnabled(boolean loggingEnabled){
            this.loggingEnabled = loggingEnabled;
            return this;
        }

        public Builder readTimeout(long timeout, TimeUnit timeUnit){
            this.readTimeout = timeout;
            this.readTimeoutTimeUnit = timeUnit;
            return this;
        }

        public Builder writeTimeout(long timeout, TimeUnit timeUnit){
            this.writeTimeout = timeout;
            this.writeTimeoutTimeUnit = timeUnit;
            return this;
        }

        public Builder addInterceptor(Interceptor interceptor){
            ensureInterceptors();
            interceptors.add(interceptor);
            return this;
        }

        private void ensureInterceptors(){
            if(interceptors == null){
                interceptors = new ArrayList<>();
            }
        }

        public Builder addNetworkInterceptor(Interceptor interceptor){
            ensureNetworkInterceptors();
            networkInterceptors.add(interceptor);
            return this;
        }

        private void ensureNetworkInterceptors(){
            if(networkInterceptors == null){
                networkInterceptors = new ArrayList<>();
            }
        }

        public Builder addConverterFactory(Converter.Factory factory){
            ensureConverterFactories();
            converterFactories.add(factory);
            return this;
        }

        private void ensureConverterFactories(){
            if(converterFactories == null){
                converterFactories = new ArrayList<>();
            }
        }

        public Builder addGsonConverterFactory(Gson gson){
            GsonConverterFactory factory = GsonConverterFactory.create(gson);
            return addConverterFactory(factory);
        }

        public Builder addGsonConverterFactory(){
            return addGsonConverterFactory(new Gson());
        }

        public Builder addScalarsConverterFactory(){
            ScalarsConverterFactory factory = ScalarsConverterFactory.create();
            return addConverterFactory(factory);
        }

        public ApiCore build(){
            return new ApiCore(this);
        }
    }
}