package com.god.kotlin.net

interface OnResult<T> {
    fun onSucceed(response: T)
    fun onFailure(error: Error)
}
