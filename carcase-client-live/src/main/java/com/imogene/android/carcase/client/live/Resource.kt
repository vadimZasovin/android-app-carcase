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

data class Resource<out T> (val status: Status, val source: Source,
                            val data: T?, val error: Throwable?){

    companion object {

        internal fun <T> loading(source: Source, data: T? = null)
                = Resource(Status.LOADING, source, data, null)

        internal fun <T> success(source: Source, data: T)
                = Resource(Status.SUCCESS, source, data, null)

        internal fun <T> error(source: Source, error: Throwable, data: T? = null)
                = Resource(Status.ERROR, source, data, error)

        fun <X, Y> from(source: Resource<X>, data: Y?) =
                Resource(source.status, source.source, data, source.error)
    }
}

inline fun <X, Y> Resource<X>.map(map: (X) -> Y?) : Resource<Y> {
    val data = this.data
    val newData = if(data == null){
        null
    }else{
        map(data)
    }
    return Resource.from(this, newData)
}