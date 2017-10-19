package com.imogene.android.carcase.client.live

/**
 * Created by Admin on 17.10.2017.
 */
enum class Source{
    NETWORK, CACHE
}

enum class Status{
    LOADING, SUCCESS, ERROR
}

data class Resource<out T> internal constructor(val status: Status, val source: Source,
                                                val data: T?, val error: Throwable?){

    internal companion object {

        fun <T> loading(source: Source, data: T? = null)
                = Resource(Status.LOADING, source, data, null)

        fun <T> success(source: Source, data: T)
                = Resource(Status.SUCCESS, source, data, null)

        fun <T> error(source: Source, error: Throwable, data: T? = null)
                = Resource(Status.ERROR, source, data, error)

        fun <T> from(from: Resource<T>, data: T?)
                = Resource(from.status, from.source, data, from.error)
    }
}