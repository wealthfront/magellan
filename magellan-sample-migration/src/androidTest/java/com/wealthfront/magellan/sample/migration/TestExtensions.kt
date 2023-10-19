@file:Suppress("UNCHECKED_CAST")

package com.wealthfront.magellan.sample.migration

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.stubbing.LenientStubber
import org.mockito.stubbing.OngoingStubbing
import org.mockito.verification.VerificationMode

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
