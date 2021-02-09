package com.example.vendafacil.helpers

import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


interface CallbackAsync<T> {
    fun onComplete(result: T)
    fun onException(e: Exception?)
}

suspend fun <T> awaitCallback(block: (CallbackAsync<T>) -> Unit) : T =
    suspendCancellableCoroutine { cont ->
        block(object : CallbackAsync<T> {
            override fun onComplete(result: T) = cont.resume(result)
            override fun onException(e: Exception?) {
                e?.let { cont.resumeWithException(it) }
            }
        })
    }