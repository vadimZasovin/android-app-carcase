package com.imogene.android.carcase.client.live

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.Observer
import android.os.AsyncTask

/**
 * Created by Admin on 18.10.2017.
 */
abstract class NetworkBoundResource<T> {

    private val _liveData = MediatorLiveData<Resource<T>>()

    val liveData : LiveData<Resource<T>> by lazy(LazyThreadSafetyMode.NONE) {
        initiate()
        _liveData
    }

    private fun initiate(){
        dispatchResource(Resource.loading(Source.CACHE))
        val cacheSource = loadFromCache()
        _liveData.addSource(cacheSource){
            _liveData.removeSource(cacheSource)
            if(shouldRefresh(it)){
                refresh(cacheSource)
            }else{
                _liveData.addSource(cacheSource){
                    dispatchResource(Source.CACHE, it)
                }
            }
        }
    }

    protected abstract fun loadFromCache() : LiveData<T>

    protected abstract fun shouldRefresh(data: T?) : Boolean

    private fun refresh(cacheSource: LiveData<T>){
        val networkSource = loadFromNetwork()
        _liveData.addSource(cacheSource){
            dispatchResource(Resource.loading(Source.NETWORK, it))
        }
        _liveData.addSource(networkSource){
            if(it == null || it.status == Status.LOADING){
                return@addSource
            }
            _liveData.removeSource(cacheSource)
            _liveData.removeSource(networkSource)
            if(it.status == Status.SUCCESS){
                val data = it.data
                if(data != null){
                    saveResultAndReInit(data)
                }
            }else{
                onRefreshFailed()
                @Suppress("UnnecessaryVariable")
                val errorResource = it
                _liveData.addSource(cacheSource){
                    dispatchResource(Resource.from(errorResource, it))
                }
            }
        }
    }

    protected abstract fun loadFromNetwork() : LiveData<Resource<T>>

    @SuppressLint("StaticFieldLeak")
    private fun saveResultAndReInit(data: T){
        object : AsyncTask<Unit, Unit, Unit>(){

            override fun doInBackground(vararg params: Unit?) {
                saveData(data)
            }

            override fun onPostExecute(result: Unit?) {
                val cacheSource = loadFromCache()
                _liveData.addSource(cacheSource, object : Observer<T> {

                    private var firstChange = true

                    override fun onChanged(value: T?) {
                        val source = if(firstChange){
                            firstChange = false
                            Source.NETWORK
                        }else{
                            Source.CACHE
                        }
                        dispatchResource(source, value)
                    }
                })
            }
        }.execute()
    }

    private fun dispatchResource(resource: Resource<T>){
        _liveData.value = resource
    }

    private fun dispatchResource(source: Source, data: T?){
        if(data != null){
            dispatchResource(Resource.success(source, data))
        }
    }

    protected abstract fun saveData(data: T)

    protected open fun onRefreshFailed(){}
}