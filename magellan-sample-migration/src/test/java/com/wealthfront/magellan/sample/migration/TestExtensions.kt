@file:Suppress("UNCHECKED_CAST")

package com.wealthfront.magellan.sample.migration

/*
fun <T> LenientStubber.coWhen(block: suspend CoroutineScope.() -> T): OngoingStubbing<T> =
  runBlocking {
    this@coWhen.`when`(block())
  }

fun <T> coWhen(block: suspend CoroutineScope.() -> T): OngoingStubbing<T> =
  runBlocking {
    `when`(block())
  }

fun <T> coVerify(mock: T, block: suspend CoroutineScope.(T) -> Unit) {
  runBlocking {
    block(verify(mock))
  }
}

fun <T> coVerify(mock: T, mode: VerificationMode, block: suspend CoroutineScope.(T) -> Unit) {
  runBlocking {
    block(verify(mock, mode))
  }
}
*/