package com.imogene.android.carcase.client.live

import android.annotation.SuppressLint
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer

/**
 * Created by Admin on 18.10.2017.
 */
abstract class NetworkBoundResource<T> {

    private val _liveData = MediatorLiveData<Resource<T>>()

    val liveData : LiveData<Resource<T>> by lazy(LazyThreadSafetyMode.NONE) {
        initiate()
        _liveData
    }

    private lateinit var cacheSource : LiveData<T>

    private fun initiate(){
        dispatchResource(Resource.loading(Source.CACHE))
        cacheSource = loadFromCache()
        _liveData.addSource(cacheSource){
            _liveData.removeSource(cacheSource)
            if(shouldRefresh(it)){
                refresh()
            } else if (isObservingCacheEnabled){
                _liveData.addSource(cacheSource){
                    dispatchResource(Source.CACHE, it)
                }
            }
        }
    }

    protected abstract fun loadFromCache() : LiveData<T>

    protected abstract fun shouldRefresh(data: T?) : Boolean

    private fun refresh(){
        val networkSource = loadFromNetwork()
        _liveData.addSource(cacheSource){
            if(!isObservingCacheDuringRefreshEnabled){
                _liveData.removeSource(cacheSource)
            }
            dispatchResource(Resource.loading(Source.NETWORK, it))
        }
        _liveData.addSource(networkSource){
            if(it == null || it.status == Status.LOADING){
                return@addSource
            }
            if(isObservingCacheDuringRefreshEnabled){
                _liveData.removeSource(cacheSource)
            }
            _liveData.removeSource(networkSource)
            if(it.status == Status.SUCCESS){
                val data = it.data
                if(data != null){
                    saveResultAndReInit(data)
                }
            }else{
                onRefreshFailed()
                if(isObservingCacheEnabled){
                    @Suppress("UnnecessaryVariable")
                    val errorResource = it
                    _liveData.addSource(cacheSource){
                        dispatchResource(Resource.from(errorResource, it))
                    }
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
                cacheSource = loadFromCache()
                _liveData.addSource(cacheSource, object : Observer<T> {

                    private var firstTrigger = true

                    override fun onChanged(value: T?) {
                        if(firstTrigger){
                            dispatchResource(Source.NETWORK, value)
                            if(!isObservingCacheAfterRefreshEnabled){
                                _liveData.removeSource(cacheSource)
                            }
                            firstTrigger = false
                        }else if(isObservingCacheAfterRefreshEnabled){
                            dispatchResource(Source.CACHE, value)
                        }
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

    /*
     Indicates whether the _liveData already has any value.
     */
    private val isInitialized get() = _liveData.value != null

    /**
     * Status of the resource. It is an error to access
     * this property when the [liveData] has not been
     * accessed yet.
     */
    val currentStatus : Status
        get() {
            val resource = _liveData.value
            return resource?.status ?:
                    throw IllegalStateException(
                            "liveData has no value to " +
                            "take status from.")
        }

    /**
     * Source of the resource. It is an error to access
     * this property when the [liveData] has not been
     * accessed yet.
     */
    val currentSource : Source
        get() {
            val resource = _liveData.value
            return resource?.source ?:
                    throw IllegalStateException(
                            "liveData has no value " +
                            "to take source from.")
        }

    /**
     * Indicates whether this resource is in process
     * of loading data from the network.
     */
    val isRefreshing
        get() = isInitialized
                && currentStatus == Status.LOADING
                && currentSource == Source.NETWORK

    /**
     * Controls whether the [liveData] can be updated
     * with values from the cache during loading data
     * from the network.
     *
     * It is enabled by default.
     *
     * If the value of this property changed during
     * refresh, the [liveData] is changed too by
     * adding or removing the proper cache source
     * LiveData.
     */
    var isObservingCacheDuringRefreshEnabled = true
        set(value) {
            if(value != field){
                // update backing field
                field = value
                if(isRefreshing){
                    // update _liveData sources only if
                    // a new value assigned and data is
                    // loading from the network now

                    // remove old cache source
                    _liveData.removeSource(cacheSource)
                    if(value){
                        _liveData.addSource(cacheSource){
                            dispatchResource(Resource.loading(Source.NETWORK, it))
                        }
                    }
                }
            }
        }

    /**
     * Controls whether the [liveData] can be updated
     * with values from the cache after loading data
     * from the network is finished.
     *
     * It is enabled by default.
     *
     * If the value of this property changed after
     * the data successfully loaded from the network,
     * the [liveData] is changed too by adding or
     * removing the proper cache source LiveData.
     */
    var isObservingCacheAfterRefreshEnabled = true
        set(value) {
            if(value != field){
                // update backing field
                field = value
                if(!isInitialized){
                    return
                }
                // update _liveData sources only if
                // a new value assigned to the property
                // and the data is successfully loaded
                // from the network.
                if(currentStatus == Status.SUCCESS && currentSource == Source.NETWORK){
                    // remove old cache source
                    _liveData.removeSource(cacheSource)
                    if(value){
                        _liveData.addSource(cacheSource){
                            dispatchResource(Source.CACHE, it)
                        }
                    }
                }
            }
        }

    /**
     * Controls whether the [liveData] can be updated
     * with values from cache. Note however, that the
     * data will be still loaded from cache initially.
     *
     * It is enabled by default.
     *
     * Changing the value of this property also affects
     * [isObservingCacheDuringRefreshEnabled] and
     * [isObservingCacheAfterRefreshEnabled] properties.
     */
    var isObservingCacheEnabled = true
        set(value) {
            if(value != field){
                // update backing field
                field = value
                // assign new value to this properties as well
                isObservingCacheDuringRefreshEnabled = value
                isObservingCacheAfterRefreshEnabled = value
                if(!isInitialized){
                    return
                }
                // update _liveData's cache source LiveData
                // only if current status and source are not
                // appropriate to logic of properties updated
                // above
                if(!isRefreshing){
                    if(!(currentStatus == Status.SUCCESS && currentSource == Source.NETWORK)){
                        // remove old cache source
                        _liveData.removeSource(cacheSource)
                        if(value){
                            _liveData.addSource(cacheSource){
                                dispatchResource(Source.CACHE, it)
                            }
                        }
                    }
                }
            }
        }

    /**
     * Starts loading the data from the network if
     * the [liveData] already has some value and the
     * data is not loading now. If the [liveData]
     * has no value the whole process of loading
     * is initiated: loading from cache and then
     * from network.
     */
    fun invalidate(){
        if(!isInitialized){
            // if this resource is not initialized,
            // we just init liveData property
            liveData
        }else if(!isRefreshing){
            _liveData.removeSource(cacheSource)
            refresh()
        }
    }
}