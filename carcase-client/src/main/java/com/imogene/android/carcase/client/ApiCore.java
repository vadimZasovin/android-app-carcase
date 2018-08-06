package com.imogene.android.carcase.client;


import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import androidx.annotation.NonNull;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Admin on 14.04.2017.
 */

public class ApiCore {

    private final Retrofit retrofit;
    private final OkHttpClient client;

    private ApiCore(Builder builder){
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

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

        SSLSocketFactory sslSocketFactory = builder.sslSocketFactory;
        X509TrustManager trustManager = builder.trustManager;
        if(sslSocketFactory != null && trustManager != null){
            clientBuilder.sslSocketFactory(sslSocketFactory, trustManager);
        }

        client = clientBuilder.build();

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
        retrofitBuilder
                .baseUrl(builder.baseUrl)
                .client(client)
                .build();

        List<Converter.Factory> converterFactories = builder.converterFactories;
        if(converterFactories != null && !converterFactories.isEmpty()){
            for(Converter.Factory factory : converterFactories){
                retrofitBuilder.addConverterFactory(factory);
            }
        }

        List<CallAdapter.Factory> callAdapterFactories = builder.callAdapterFactories;
        if(callAdapterFactories != null && !callAdapterFactories.isEmpty()){
            for (CallAdapter.Factory factory : callAdapterFactories){
                retrofitBuilder.addCallAdapterFactory(factory);
            }
        }

        retrofit = retrofitBuilder.build();
    }

    public <T> T createApi(Class<T> clazz){
        return retrofit.create(clazz);
    }

    public OkHttpClient getClient(){
        return client;
    }

    public static final class Builder{

        private String baseUrl;
        private long readTimeout;
        private TimeUnit readTimeoutTimeUnit;
        private long writeTimeout;
        private TimeUnit writeTimeoutTimeUnit;
        private List<Interceptor> interceptors;
        private List<Interceptor> networkInterceptors;
        private List<Converter.Factory> converterFactories;
        private SSLSocketFactory sslSocketFactory;
        private X509TrustManager trustManager;
        private List<CallAdapter.Factory> callAdapterFactories;

        public Builder(){
            readTimeout = 15;
            readTimeoutTimeUnit = TimeUnit.SECONDS;
            writeTimeout = 10;
            writeTimeoutTimeUnit = TimeUnit.SECONDS;
        }

        public Builder baseUrl(@NonNull String baseUrl){
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder enableLogging(HttpLoggingInterceptor.Logger logger){
            HttpLoggingInterceptor interceptor;
            if(logger != null){
                interceptor = new HttpLoggingInterceptor(logger);
            }else {
                interceptor = new HttpLoggingInterceptor();
            }
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            return addInterceptor(interceptor);
        }

        public Builder enableLogging(){
            return enableLogging(null);
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

        public Builder sslSocketFactory(SSLSocketFactory factory, X509TrustManager trustManager){
            this.sslSocketFactory = factory;
            this.trustManager = trustManager;
            return this;
        }

        public Builder addCallAdapterFactory(CallAdapter.Factory factory){
            ensureCallAdapterFactories();
            callAdapterFactories.add(factory);
            return this;
        }

        private void ensureCallAdapterFactories(){
            if(callAdapterFactories == null){
                callAdapterFactories = new ArrayList<>();
            }
        }

        public ApiCore build(){
            return new ApiCore(this);
        }
    }
}