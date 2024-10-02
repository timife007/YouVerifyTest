package com.timife.youverifytest.domain

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Resource<T>(val data:T? = null, val message:String? = null){
    class Success<T>(data: T?): Resource<T>(data)
    class Error<T>(message: String?,data: T?= null): Resource<T>(data,message)
    class Loading<T>(val isLoading:Boolean = false): Resource<T>(null)
}
